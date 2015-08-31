package at.jku.tk.hiesmair.gv.parlament.etl.politician.transformer;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import at.jku.tk.hiesmair.gv.parlament.cache.DataCache;
import at.jku.tk.hiesmair.gv.parlament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parlament.entities.Politician;
import at.jku.tk.hiesmair.gv.parlament.entities.club.ParliamentClub;
import at.jku.tk.hiesmair.gv.parlament.entities.mandate.EuropeanParliamentMember;
import at.jku.tk.hiesmair.gv.parlament.entities.mandate.FederalChancellor;
import at.jku.tk.hiesmair.gv.parlament.entities.mandate.FederalCouncilMember;
import at.jku.tk.hiesmair.gv.parlament.entities.mandate.FederalCouncilPresident;
import at.jku.tk.hiesmair.gv.parlament.entities.mandate.FederalCouncilVicePresident;
import at.jku.tk.hiesmair.gv.parlament.entities.mandate.FederalMinister;
import at.jku.tk.hiesmair.gv.parlament.entities.mandate.FederalPresident;
import at.jku.tk.hiesmair.gv.parlament.entities.mandate.FederalViceChancellor;
import at.jku.tk.hiesmair.gv.parlament.entities.mandate.Mandate;
import at.jku.tk.hiesmair.gv.parlament.entities.mandate.NationalCouncilMember;
import at.jku.tk.hiesmair.gv.parlament.entities.mandate.NationalCouncilPresident;
import at.jku.tk.hiesmair.gv.parlament.etl.AbstractTransformer;
import at.jku.tk.hiesmair.gv.parlament.etl.politician.extractor.feed.PoliticianFeedItem;

import com.frequal.romannumerals.Converter;

@Component
public class PoliticianTransformer extends AbstractTransformer {

	private static final String DATE_PATTERN = "dd.MM.yyyy";

	private static final Logger logger = Logger.getLogger(PoliticianTransformer.class.getSimpleName());

	protected final Pattern namePattern;
	protected final Pattern birthDatePattern;
	protected final Pattern mandatePattern;

	protected final Converter romanNrConverter;

	protected DataCache cache;

	@Inject
	public PoliticianTransformer(DataCache cache) {
		this.cache = cache;
		this.namePattern = Pattern.compile("^((?:[\\wöäüÖÄÜß]+\\..?(?:\\(FH\\))?)*\\s)?((?:[\\wöäüÖÄÜß,-\\.]+(?:\\s.\\.)?\\s?)+)\\s([^\\s,(\\.:]+)$");
		this.mandatePattern = Pattern
				.compile("([^(,]*)(?:\\(([^\\.]+)\\.(?:.([^\\.]+)\\.)?\\sGP\\))?,? ?([^\\d]+)?\\s(\\d+\\.\\d+\\.\\d{4})( .)?\\s?(?:(\\d+\\.\\d+\\.\\d{4}))?");
		this.birthDatePattern = Pattern.compile("Geb.:\\s(\\d+\\.\\d+\\.\\d{4})");

		this.romanNrConverter = new Converter();
	}

	public Politician getPolitician(String url) throws Exception {
		PoliticianFeedItem item = new PoliticianFeedItem();
		item.setUrl(new URL(url));
		item.setTitle(url.replaceAll("[ \\/\\.\\(\\):]", ""));

		return getPolitician(item);
	}

	public Politician getPolitician(PoliticianFeedItem feedItem) throws IOException {
		Document document = feedItem.getIndexDocument();
		return getPolitician(feedItem.getUrl().toString(), document);
	}

	protected Politician getPolitician(String url, Document document) {
		logger.debug("transforming politician " + url);
		
		String fullText = document.text();

		Politician politician = new Politician();
		politician.setId(url);
		politician.setFirstName(getFirstName(document));
		politician.setSurName(getSurName(document));
		politician.setTitle(getTitle(document));
		politician.setTitleAfter(getTitleAfter(document));
		politician.setBirthDate(getBirthDate(fullText));
		politician.setMandates(getMandates(politician, document));

		cache.putPolitician(politician);

		return politician;
	}

	private String getFirstName(Document document) {
		Matcher m = matchName(document);
		if (m.find()) {
			return m.group(2).trim();
		}
		logger.info("firstName not found");
		return "";
	}

	private String getSurName(Document document) {
		Matcher m = matchName(document);
		if (m.find()) {
			return m.group(3).trim();
		}
		logger.info("surName not found");
		return "";
	}

	protected String getTitleAfter(Document document) {
		String fullName = getFullName(document);
		String[] parts = fullName.split(",");
		if (parts.length > 1) {
			return parts[1].trim();
		}
		return "";
	}

	protected Matcher matchName(Document document) {
		String text = getFullName(document);
		text = text.split(",")[0];

		Matcher m = namePattern.matcher(text);
		return m;
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

	private String getTitle(Document document) {
		Matcher m = matchName(document);
		if (m.find()) {
			String title = m.group(1);
			if (title != null) {
				return title.trim();
			}
		}
		return "";
	}

	private Date getBirthDate(String fullText) {
		Matcher m = birthDatePattern.matcher(fullText);
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
			Matcher m = mandatePattern.matcher(text);
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
				((NationalCouncilMember) mandate).setPeriods(getPeriods(((NationalCouncilMember) mandate), periodFrom, periodTo));
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
