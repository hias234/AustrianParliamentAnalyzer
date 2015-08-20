package at.jku.tk.hiesmair.gv.parlament.analysis.politician;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import at.jku.tk.hiesmair.gv.parlament.entities.ClubMembership;
import at.jku.tk.hiesmair.gv.parlament.entities.ParliamentClub;
import at.jku.tk.hiesmair.gv.parlament.entities.ParliamentData;
import at.jku.tk.hiesmair.gv.parlament.entities.Politician;

public class AustrianParliamentPoliticianExtractor implements PoliticianExtractor {

	protected final Pattern namePattern;
	protected final Pattern mandatePattern;

	public AustrianParliamentPoliticianExtractor() {
		namePattern = Pattern.compile("((?:[^\\s]+\\.?\\s)*)([^\\s,\\.]+)\\s([^\\s,(\\.]+)");
		mandatePattern = Pattern.compile("([^(,]*)\\([^)]*\\),? ?([^\\s]+)\\s(\\d+\\.\\d+\\.\\d{4})(?: . (\\d+\\.\\d+\\.\\d{4}))?");
	}

	@Override
	public Politician getPolitician(String url, ParliamentData parliamentData) {
		String fileContent = "";
		try {
			fileContent = loadFromWeb(url);
		} catch (IOException ex) {
			return null;
		}

		Document document = Jsoup.parse(fileContent);

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

	/**
	 * Retrieve the file content from web
	 * 
	 * @throws IOException
	 */
	private String loadFromWeb(String url) throws IOException {
		InputStream in = new URL(url).openStream();
		String content = IOUtils.toString(in);

		return content;
	}
}
