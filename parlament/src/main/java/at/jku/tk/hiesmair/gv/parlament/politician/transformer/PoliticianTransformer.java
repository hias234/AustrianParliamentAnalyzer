package at.jku.tk.hiesmair.gv.parlament.politician.transformer;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import at.jku.tk.hiesmair.gv.parlament.entities.ParliamentData;
import at.jku.tk.hiesmair.gv.parlament.entities.Politician;
import at.jku.tk.hiesmair.gv.parlament.entities.club.ClubMembership;
import at.jku.tk.hiesmair.gv.parlament.entities.club.ParliamentClub;
import at.jku.tk.hiesmair.gv.parlament.politician.extractor.feed.PoliticianFeedItem;

public class PoliticianTransformer {

	protected final Pattern namePattern;
	protected final Pattern mandatePattern;

	public PoliticianTransformer() {
		namePattern = Pattern.compile("((?:[^\\s]+\\.?\\s)*)([^\\s,\\.]+(?:\\s.\\.)?)\\s([^\\s,(\\.]+)");
		mandatePattern = Pattern.compile("([^(,]*)\\([^)]*\\),? ?([^\\s]+)\\s(\\d+\\.\\d+\\.\\d{4})(?: . (\\d+\\.\\d+\\.\\d{4}))?");
	}

	public Politician getPolitician(String url, ParliamentData parliamentData) throws Exception {
		PoliticianFeedItem item = new PoliticianFeedItem();
		item.setUrl(new URL(url));

		return getPolitician(url, parliamentData, item.getIndexDocument());
	}

	protected Politician getPolitician(String url, ParliamentData parliamentData, Document document) {
		Politician politician = new Politician();
		politician.setId(url);
		politician.setFirstName(getFirstName(document));
		politician.setSurName(getSurName(document));
		politician.setTitle(getTitle(document));
		politician.setClubMemberships(getClubMemberships(document, politician, parliamentData));
		
		parliamentData.putPolitician(politician);
		
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

	private List<ClubMembership> getClubMemberships(Document document, Politician politician, ParliamentData parliamentData) {
		List<ClubMembership> memberships = new ArrayList<ClubMembership>();

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

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

				ParliamentClub club = parliamentData.getClub(clubShortName);
				if (club == null) {
					club = new ParliamentClub();
					club.setShortName(clubShortName);
					parliamentData.putClub(club);
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
