package at.jku.tk.hiesmair.gv.parlament.politician.transformer;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import at.jku.tk.hiesmair.gv.parlament.cache.DataCache;
import at.jku.tk.hiesmair.gv.parlament.entities.Politician;
import at.jku.tk.hiesmair.gv.parlament.entities.club.ClubMembership;
import at.jku.tk.hiesmair.gv.parlament.entities.club.ParliamentClub;
import at.jku.tk.hiesmair.gv.parlament.politician.extractor.feed.PoliticianFeedItem;

public class PoliticianTransformer {

	private static final String DATE_PATTERN = "dd.MM.yyyy";

	private static final Logger logger = Logger.getLogger(PoliticianTransformer.class.getSimpleName());
	
	protected final Pattern namePattern;
	protected final Pattern birthDatePattern;
	protected final Pattern mandatePattern;
	
	protected final DataCache cache;

	public PoliticianTransformer() {
		namePattern = Pattern.compile("((?:[^\\s]+\\.?\\s)*)([^\\s,\\.]+(?:\\s.\\.)?)\\s([^\\s,(\\.]+)");
		mandatePattern = Pattern.compile("([^(,]*)\\([^)]*\\),? ?([^\\s]+)\\s(\\d+\\.\\d+\\.\\d{4})(?: . (\\d+\\.\\d+\\.\\d{4}))?");
		birthDatePattern = Pattern.compile("Geb.:\\s(\\d+\\.\\d+\\.\\d{4})");
		
		cache = DataCache.getInstance();
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
		String fullText = document.text();
		
		Politician politician = new Politician();
		politician.setId(url.toString());
		politician.setFirstName(getFirstName(document));
		politician.setSurName(getSurName(document));
		politician.setTitle(getTitle(document));
		politician.setBirthDate(getBirthDate(fullText));
		politician.setClubMemberships(getClubMemberships(document, politician));
		
		cache.putPolitician(politician);
		
		return politician;
	}

	private String getFirstName(Document document) {
		Matcher m = matchName(document);
		if (m.find()) {
			return m.group(2).trim();
		}
		return "";
	}

	private String getSurName(Document document) {
		Matcher m = matchName(document);
		if (m.find()) {
			return m.group(3).trim();
		}
		return "";
	}

	protected Matcher matchName(Document document) {
		Elements headers = document.getElementsByTag("h1");
		Element header = headers.stream().findFirst().get();

		String text = header.text();
		text = text.replaceAll(Character.toString((char) 160), " ");

		Matcher m = namePattern.matcher(text);
		return m;
	}

	private String getTitle(Document document) {
		Matcher m = matchName(document);
		if (m.find()) {
			return m.group(1).trim();
		}
		return "";
	}
	
	private Date getBirthDate(String fullText){
		Matcher m = birthDatePattern.matcher(fullText);
		if (m.find()){
			String birthDateString = m.group(1);
			try{
				SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
				return dateFormat.parse(birthDateString);
			}
			catch(ParseException pe){
				logger.debug("invalid birthdate");
			}
		}
		
		logger.debug("birthdate of politician not found");
		return null;
	}


	private List<ClubMembership> getClubMemberships(Document document, Politician politician) {
		List<ClubMembership> memberships = new ArrayList<ClubMembership>();

		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);

		Elements headers = document.getElementsByTag("h4");
		Element polMandateHeader = headers.stream().filter(h -> h.text().contains("Mandate")).findFirst().get();

		Elements polMandate = polMandateHeader.nextElementSibling().children();
		for (Element polMandat : polMandate) {
			String text = polMandat.text().replaceAll(Character.toString((char) 160), " ");
			Matcher m = mandatePattern.matcher(text);
			if (m.find()) {
				ClubMembership membership = new ClubMembership();

				membership.setFunction(m.group(1).trim());

				String clubShortName = m.group(2);

				ParliamentClub club = cache.getClub(clubShortName);
				if (club == null) {
					club = new ParliamentClub();
					club.setShortName(clubShortName);
					cache.putClub(club);
				}
				membership.setClub(club);
				membership.setPolitician(politician);

				String from = m.group(3);
				String to = m.group(4);

				try {
					membership.setValidFrom(dateFormat.parse(from));
					if (to != null) {
						membership.setValidUntil(dateFormat.parse(to));
					}
				} catch (ParseException ex) {
					// invalid date
				}

				memberships.add(membership);
			}
		}

		return memberships;
	}

}
