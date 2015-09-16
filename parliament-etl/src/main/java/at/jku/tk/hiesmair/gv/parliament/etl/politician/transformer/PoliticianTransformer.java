package at.jku.tk.hiesmair.gv.parliament.etl.politician.transformer;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import at.jku.tk.hiesmair.gv.parliament.cache.DataCache;
import at.jku.tk.hiesmair.gv.parliament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parliament.entities.club.ParliamentClub;
import at.jku.tk.hiesmair.gv.parliament.entities.mandate.EuropeanParliamentMember;
import at.jku.tk.hiesmair.gv.parliament.entities.mandate.FederalChancellor;
import at.jku.tk.hiesmair.gv.parliament.entities.mandate.FederalCouncilMember;
import at.jku.tk.hiesmair.gv.parliament.entities.mandate.FederalCouncilPresident;
import at.jku.tk.hiesmair.gv.parliament.entities.mandate.FederalCouncilVicePresident;
import at.jku.tk.hiesmair.gv.parliament.entities.mandate.FederalMinister;
import at.jku.tk.hiesmair.gv.parliament.entities.mandate.FederalPresident;
import at.jku.tk.hiesmair.gv.parliament.entities.mandate.FederalViceChancellor;
import at.jku.tk.hiesmair.gv.parliament.entities.mandate.Mandate;
import at.jku.tk.hiesmair.gv.parliament.entities.mandate.NationalCouncilMember;
import at.jku.tk.hiesmair.gv.parliament.entities.mandate.NationalCouncilPresident;
import at.jku.tk.hiesmair.gv.parliament.entities.mandate.StateSecretary;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Name;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.PoliticianName;
import at.jku.tk.hiesmair.gv.parliament.etl.AbstractTransformer;
import at.jku.tk.hiesmair.gv.parliament.etl.politician.extractor.feed.PoliticianFeedItem;

import com.frequal.romannumerals.Converter;

@Component
public class PoliticianTransformer extends AbstractTransformer {

	private static final String DATE_PATTERN = "dd.MM.yyyy";

	private static final Logger logger = Logger.getLogger(PoliticianTransformer.class.getSimpleName());

	protected static final Pattern NAME_PATTERN = Pattern
			.compile("^((?:[\\wöäüÖÄÜß]+\\..?(?:\\(FH\\))?)*\\s)?((?:[\\wöäüÖÄÜß,-\\.]+(?:\\s.\\.)?\\s?)+)\\s([^\\s,(\\.:]+)$");;
	protected static final Pattern MAIDEN_NAME_PATTERN = Pattern
			.compile("^bis (\\d+\\.\\d+\\.\\d{4}): ((?:[\\wöäüÖÄÜß]+\\..?(?:\\(FH\\))?)*\\s)?((?:[\\wöäüÖÄÜß,-\\.]+(?:\\s.\\.)?\\s?)+)\\s([^\\s,()\\.:]+)$");
	protected static final Pattern BIRTHDATE_PATTERN = Pattern.compile("Geb.:\\s(\\d+\\.\\d+\\.\\d{4})");
	protected static final Pattern MANDATE_PATTERN = Pattern
			.compile("([^(,]*)(?:\\(([^\\.]+)\\.(?:.([^\\.]+)\\.)?\\sGP\\))?,? ?([^\\d]+)?\\s(\\d+\\.\\d+\\.\\d{4})( .)?\\s?(?:(\\d+\\.\\d+\\.\\d{4}))?");

	protected final Converter romanNrConverter;
	protected final DataCache cache;

	protected final String parliamentWebsiteUrl;

	@Inject
	public PoliticianTransformer(DataCache cache, @Value("${parliament.websiteurl}") String parliamentWebsiteUrl) {
		this.cache = cache;
		this.parliamentWebsiteUrl = parliamentWebsiteUrl;

		this.romanNrConverter = new Converter();
	}

	public Politician getPolitician(String url) throws IOException {
		PoliticianFeedItem item = new PoliticianFeedItem();

		if (!url.startsWith(parliamentWebsiteUrl)) {
			url = parliamentWebsiteUrl + url;
		}
		if (!url.endsWith("index.shtml")) {
			url += "index.shtml";
		}
		
		Politician politician = cache.getPolitician(url);
		if (politician != null) {
			return politician;
		}
		
		item.setUrl(new URL(url));
		item.setTitle(url.replaceAll("[ \\/\\.\\(\\):]", ""));

		return getPolitician(item);
	}

	public Politician getPoliticianByName(String title, String firstName, String surName, Date date) {
		Set<Politician> politiciansWithMandatesAtDate = cache.getPoliticians().values().stream()
				.filter(p -> !p.getMandatesAt(date).isEmpty()).collect(Collectors.toSet());
		
		Set<Politician> matchingPoliticians = getPoliticiansWithSurName(surName, date, politiciansWithMandatesAtDate);

		if (matchingPoliticians.isEmpty()) {
			String surNameWithoutSpecialChars = StringUtils.stripAccents(surName);
			matchingPoliticians = getPoliticiansWithSurName(surNameWithoutSpecialChars, date, politiciansWithMandatesAtDate);
		}

		if (matchingPoliticians.isEmpty()) {
			String[] surNames = surName.split("[^\\wöäüÖÄÜß]");
			for (String surNamePart : surNames) {
				matchingPoliticians = getPoliticiansWithSurName(surNamePart, date, politiciansWithMandatesAtDate);

				if (!matchingPoliticians.isEmpty()) {
					break;
				}
			}
		}

		if (matchingPoliticians.size() == 1) {
			return matchingPoliticians.iterator().next();
		}

		if (matchingPoliticians.size() > 1) {
			Set<Politician> matchingPoliticiansWithFirstName = getPoliticiansWithFirstName(firstName, date, matchingPoliticians);

			if (matchingPoliticiansWithFirstName.size() == 0) {
				String[] firstNames = firstName.split(" ");
				if (firstNames.length > 1) {
					for (String firstNamePart : firstNames) {
						Set<Politician> matchingPoliticiansWithFirstNamePart = getPoliticiansWithFirstNameContaining(date, matchingPoliticians, firstNamePart);

						if (matchingPoliticiansWithFirstNamePart.size() == 1) {
							return matchingPoliticiansWithFirstNamePart.iterator().next();
						}
					}
				}
			}
			if (matchingPoliticiansWithFirstName.size() > 1) {
				matchingPoliticiansWithFirstName = getPoliticiansWithTitle(title, date, matchingPoliticiansWithFirstName);
			}
			if (matchingPoliticiansWithFirstName.size() == 1) {
				return matchingPoliticiansWithFirstName.iterator().next();
			}
		}

		return null;
	}

	protected Set<Politician> getPoliticiansWithTitle(String title, Date date,
			Set<Politician> matchingPoliticiansWithFirstName) {
		return matchingPoliticiansWithFirstName.stream()
				.filter(p -> p.getNameAt(date).getTitle().equals(title)).collect(Collectors.toSet());
	}

	protected Set<Politician> getPoliticiansWithFirstNameContaining(Date date, Set<Politician> matchingPoliticians,
			String firstNamePart) {
		return matchingPoliticians.stream()
				.filter(p -> p.getNameAt(date).getFirstName().contains(firstNamePart))
				.collect(Collectors.toSet());
	}

	protected Set<Politician> getPoliticiansWithFirstName(String firstName, Date date,
			Set<Politician> matchingPoliticians) {
		return matchingPoliticians.stream()
				.filter(p -> p.getNameAt(date).getFirstName().equals(firstName)).collect(Collectors.toSet());
	}

	protected Set<Politician> getPoliticiansWithSurName(String surName, Date date,
			Set<Politician> politiciansWithMandatesAtDate) {
		return politiciansWithMandatesAtDate.stream()
				.filter(p -> p.getNameAt(date).getSurName().equals(surName)).collect(Collectors.toSet());
	}

	public Politician getPolitician(PoliticianFeedItem feedItem) throws IOException {
		Document document = feedItem.getIndexDocument();
		return getPolitician(feedItem.getUrl().toString(), document);
	}

	public Elements getPoliticianLinks(Element document) {
		return document.select("a[href*=WWER/PAD]");
	}

	protected Politician getPolitician(String url, Document document) {
		Politician politician = cache.getPolitician(url);
		if (politician != null) {
			return politician;
		}
		
		logger.debug("transforming politician " + url);

		String fullText = document.text();

		politician = new Politician();
		politician.setId(url);
		politician.setName(getName(document));
		politician.setBirthDate(getBirthDate(fullText));
		politician.setMandates(getMandates(politician, document));

		PoliticianName previousName = getPreviousName(politician, document);
		if (previousName != null) {
			politician.setPreviousNames(Arrays.asList(previousName));
		}

		cache.putPolitician(politician);

		return politician;
	}

	private PoliticianName getPreviousName(Politician politician, Document document) {
		Elements headers = document.getElementsByTag("h1");
		if (!headers.isEmpty()) {
			Element header = headers.stream().findFirst().get();
			Element maidenNameElement = header.nextElementSibling();
			if (maidenNameElement != null) {
				maidenNameElement = maidenNameElement.nextElementSibling();
				if (!maidenNameElement.children().isEmpty()) {
					maidenNameElement = maidenNameElement.child(0);
					String maidenFullName = maidenNameElement.text().replaceAll(NBSP_STRING, " ");

					if (maidenFullName.length() > 2) {
						maidenFullName = maidenFullName.substring(1, maidenFullName.length() - 1);
						String[] parts = maidenFullName.split(",");
						maidenFullName = parts[0];

						Matcher m = MAIDEN_NAME_PATTERN.matcher(maidenFullName);
						if (m.find()) {
							SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

							PoliticianName name = new PoliticianName();
							name.setPolitician(politician);
							if (m.group(2) != null) {
								name.setTitle(m.group(2).trim());
							}
							if (m.group(3) != null) {
								name.setFirstName(m.group(3).trim());
							}
							if (m.group(4) != null) {
								name.setSurName(m.group(4).trim());
							}
							if (parts.length > 1) {
								name.setTitleAfter(parts[1].trim());
							}

							try {
								name.setValidUntil(sdf.parse(m.group(1)));
							} catch (ParseException e) {
							}

							return name;
						}
					}
				}
			}
		}
		return null;
	}

	private Name getName(Document document) {
		String fullName = getFullName(document);
		Name name = getName(fullName);

		if (name != null) {
			return name;
		}
		else {
			logger.info("politician-name not found");
		}
		return new Name();
	}

	protected Name getName(String fullName) {
		String[] parts = fullName.split(",");
		fullName = parts[0];
		Matcher m = NAME_PATTERN.matcher(fullName);

		if (m.find()) {
			Name name = new Name();
			if (parts.length > 1) {
				name.setTitleAfter(parts[1]);
			}
			if (m.group(1) != null) {
				name.setTitle(m.group(1).trim());
			}
			if (m.group(2) != null) {
				name.setFirstName(m.group(2).trim());
			}
			if (m.group(3) != null) {
				name.setSurName(m.group(3).trim());
			}
			return name;
		}
		return null;
	}

	protected String getFullName(Document document) {
		Elements headers = document.getElementsByTag("h1");
		if (!headers.isEmpty()) {
			Element header = headers.stream().findFirst().get();

			String text = header.text();
			text = text.replaceAll(NBSP_STRING, " ");
			return text;
		}
		return "";
	}

	private Date getBirthDate(String fullText) {
		Matcher m = BIRTHDATE_PATTERN.matcher(fullText);
		if (m.find()) {
			String birthDateString = m.group(1);
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
				return dateFormat.parse(birthDateString);
			} catch (ParseException pe) {
				logger.info("invalid birthdate");
			}
		}

		logger.info("birthdate of politician not found");
		return null;
	}

	private Set<Mandate> getMandates(Politician politician, Document document) {
		Set<Mandate> mandates = new HashSet<Mandate>();

		Elements headers = document.getElementsByTag("h4");
		Element polMandateHeader = headers.stream().filter(h -> h.text().contains("Mandate")).findFirst().get();

		Elements polMandate = polMandateHeader.nextElementSibling().children();
		for (Element polMandat : polMandate) {
			String text = polMandat.text().replaceAll(NBSP_STRING, " ");
			Matcher m = MANDATE_PATTERN.matcher(text);
			if (m.find()) {
				String description = m.group(1).trim();
				String periodFrom = m.group(2);
				String periodTo = m.group(3);
				String clubShortName = m.group(4);
				String from = m.group(5);
				Boolean dateRange = m.group(6) != null;
				String to = m.group(7);

				Mandate mandate = getMandate(politician, description, periodFrom, periodTo, clubShortName, from, to,
						dateRange);

				mandates.add(mandate);
			}
			else {
				logger.info("mandate item not recognized: " + text);
			}
		}

		return mandates;
	}

	private Mandate getMandate(Politician politician, String description, String periodFrom, String periodTo,
			String clubShortName, String from, String to, Boolean isDateRange) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);

		Mandate mandate = null;
		if (description.contains("Abgeordnete")) {
			if (description.contains(" zum Nationalrat")) {
				mandate = new NationalCouncilMember();
				((NationalCouncilMember) mandate).setClub(getClub(clubShortName.trim()));
				((NationalCouncilMember) mandate).setPeriods(getPeriods(((NationalCouncilMember) mandate), periodFrom,
						periodTo));
			}
		}
		else if (description.contains("Mitglied des Bundesrates")) {
			mandate = new FederalCouncilMember();
			((FederalCouncilMember) mandate).setClub(getClub(clubShortName.trim()));
		}
		else if (description.contains("Vizepräsident des Bundesrates")
				|| description.contains("Vizepräsidentin des Bundesrates")) {
			mandate = new FederalCouncilVicePresident();
		}
		else if (description.contains("Präsident des Bundesrates")
				|| description.contains("Präsidentin des Bundesrates")) {
			mandate = new FederalCouncilPresident();
		}
		else if (description.contains("Staatssekretär")){
			mandate = new StateSecretary();
			
			String department = description.replaceAll("Staatssekretär(in)? im (Bundesministerium für )?", "");
			((StateSecretary) mandate).setDepartment(department);
		}
		else if (description.contains("Bundesminister")) {
			mandate = new FederalMinister();

			String department;
			int indexOfFor = description.indexOf("für");
			if (indexOfFor != -1) {
				department = description.substring(indexOfFor + 4);
			}
			else if (description.contains("Bundesministerin")) {
				department = description.substring(16).trim();
			}
			else {
				department = description.substring(14).trim();
			}
			((FederalMinister) mandate).setDepartment(department);
		}
		else if (description.contains("Bundespräsident")) {
			mandate = new FederalPresident();
		}
		else if (description.contains("Vizekanzler")) {
			mandate = new FederalViceChancellor();
		}
		else if (description.contains("Bundeskanzler")) {
			mandate = new FederalChancellor();
		}
		else if (description.contains("Europäisches Parlament")) {
			mandate = new EuropeanParliamentMember();
			((EuropeanParliamentMember) mandate).setClub(getClub(clubShortName.trim()));
		}
		else if (description.contains("Präsident des Nationalrates")
				|| description.contains("Präsidentin des Nationalrates")) {
			mandate = new NationalCouncilPresident();

			Integer position = 1;
			if (description.contains("Zweite")) {
				position = 2;
			}
			else if (description.contains("Dritte")) {
				position = 3;
			}

			((NationalCouncilPresident) mandate).setPosition(position);
		}
		else {
			mandate = new Mandate();
		}

		mandate.setDescription(description);
		mandate.setPolitician(politician);
		try {
			Date dateFrom = dateFormat.parse(from);
			mandate.setValidFrom(dateFrom);
			if (isDateRange) {
				if (to != null) {
					mandate.setValidUntil(dateFormat.parse(to));
				}
			}
			else {
				mandate.setValidUntil(dateFrom);
			}
		} catch (ParseException ex) {
			logger.info("invalid date " + ex.getMessage());
		}

		return mandate;
	}

	private Set<LegislativePeriod> getPeriods(NationalCouncilMember member, String periodFromStr, String periodToStr) {
		Set<LegislativePeriod> periods = new HashSet<LegislativePeriod>();

		try {
			Integer periodFrom = romanNrConverter.toNumber(periodFromStr);
			LegislativePeriod period = getLegislativePeriod(periodFrom);
			period.getNationalCouncilMembers().add(member);
			periods.add(period);

			if (periodToStr != null) {
				Integer periodTo = romanNrConverter.toNumber(periodToStr);
				for (Integer periodNr = periodFrom + 1; periodNr <= periodTo; periodNr++) {
					LegislativePeriod nextPeriod = getLegislativePeriod(periodNr);
					nextPeriod.getNationalCouncilMembers().add(member);
					periods.add(nextPeriod);
				}
			}
		} catch (ParseException e) {
			logger.info("roman number convertion error" + e.getMessage());
		}

		return periods;
	}

	protected LegislativePeriod getLegislativePeriod(Integer periodFrom) {
		LegislativePeriod period = cache.getLegislativePeriod(periodFrom);
		if (period == null) {
			period = new LegislativePeriod(periodFrom);
			cache.putLegislativePeriod(period);
		}
		return period;
	}

	protected ParliamentClub getClub(String clubShortName) {
		ParliamentClub club = cache.getClub(clubShortName);
		if (club == null) {
			club = new ParliamentClub();
			club.setShortName(clubShortName);
			cache.putClub(club);
		}
		return club;
	}

}
