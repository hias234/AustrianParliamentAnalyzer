package at.jku.tk.hiesmair.gv.parlament.analysis.session;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import at.jku.tk.hiesmair.gv.parlament.entities.Politician;
import at.jku.tk.hiesmair.gv.parlament.entities.Session;

/**
 * Implementation for the Protocols of the Austrian Parliament
 * 
 * @author Markus
 *
 */
public class AustrianParliamentSessionExtractor implements SessionExtractor {
	
	private static final String DATE_FORMAT_PATTERN = "dd.MM.yyyy HH:mm";
	
	protected final Pattern sessionNrPattern;
	protected final Pattern startEndDatePattern;
	protected final Pattern namePattern;
	
	protected final List<String> monthNames;

	public AustrianParliamentSessionExtractor() {
		monthNames = Arrays.asList("Jänner", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember");

		sessionNrPattern = Pattern.compile("(\\d+)\\.\\sSitzung\\sdes\\sNationalrates");
		startEndDatePattern = Pattern.compile("\\w+, (\\d+)\\. (\\w+) (\\d{4}):\\s+(\\d+)\\.(\\d+).+ (\\d+)\\.(\\d+).*Uhr");
		namePattern = Pattern.compile("((?:[^\\s]+\\.?\\s)*)([^\\s,\\.]+)\\s([^\\s,(\\.]+)");
	}

	@Override
	public Session getSession(Document index, Document protocol) {
		String protocolHtml = protocol.html();

		Session session = new Session();
		session.setSessionNr(getSessionNr(protocolHtml));
		session.setStartDate(getStartDate(protocolHtml));
		session.setEndDate(getEndDate(protocolHtml));
		session.setPoliticians(getPoliticians(index, protocol)); 

		return session;
	}

	/**
	 * returns the session number extracted from the HTML by regular expression
	 * 
	 * @param protocolHtml
	 * @return
	 */
	protected Integer getSessionNr(String protocolHtml) {
		Matcher matcher = sessionNrPattern.matcher(protocolHtml);

		if (matcher.find()) {
			String sessionNr = matcher.group(1);
			try {
				return Integer.parseInt(sessionNr);
			} catch (NumberFormatException nfe) {
				// invalid sessionNr
			}
		}

		return null;
	}

	protected Date getStartDate(String protocolHtml) {
		Matcher matcher = startEndDatePattern.matcher(protocolHtml);
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_PATTERN);

		if (matcher.find()) {
			String day = matcher.group(1);
			String month = matcher.group(2);
			Integer monthIndex = 1 + monthNames.indexOf(month);
			String year = matcher.group(3);
			String hourStart = matcher.group(4);
			String minuteStart = matcher.group(5);

			return getDate(format, day, monthIndex, year, hourStart, minuteStart);
		}

		return null;
	}

	protected Date getEndDate(String protocolHtml) {
		Matcher matcher = startEndDatePattern.matcher(protocolHtml);
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_PATTERN);

		if (matcher.find()) {
			String day = matcher.group(1);
			String month = matcher.group(2);
			Integer monthIndex = 1 + monthNames.indexOf(month);
			String year = matcher.group(3);
			String hourStart = matcher.group(6);
			String minuteStart = matcher.group(7);

			return getDate(format, day, monthIndex, year, hourStart, minuteStart);
		}

		return null;
	}

	protected Date getDate(SimpleDateFormat format, String day, Integer monthIndex, String year, String hourStart, String minuteStart) {
		try {
			return format.parse(day + "." + monthIndex + "." + year + " " + hourStart + ":" + minuteStart);
		} catch (ParseException pe) {
			// invalid date
		}
		return null;
	}

	protected List<Politician> getPoliticians(Document index, Document protocol){
		Set<Politician> politicians = new HashSet<Politician>();
		politicians.addAll(getPoliticians(index));
		politicians.addAll(getPoliticians(protocol));
		
		return new ArrayList<Politician>(politicians);
	}

	protected Set<Politician> getPoliticians(Document document) {
		Set<Politician> politicians = new HashSet<Politician>();
		
		Elements links = document.getElementsByTag("a");
		for (Element link : links){
			String href = link.attr("href");
			if (href.startsWith("/WWER/PAD")){
				Politician politician = new Politician();
				
				String text = link.text();
				text = text.replaceAll(Character.toString((char) 160), " ");

				Matcher matcher = namePattern.matcher(text);
				if (matcher.find()){
					politician.setTitle(matcher.group(1).trim());
					politician.setFirstName(matcher.group(2).trim());
					politician.setSurName(matcher.group(3).trim());
					
					politicians.add(politician);
				}
			}
		}
		
		return politicians;
	}
}
