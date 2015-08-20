package at.jku.tk.hiesmair.gv.parlament.analysis.session;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;

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
	protected final List<String> monthNames;

	public AustrianParliamentSessionExtractor() {
		monthNames = Arrays.asList("Jänner", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember");

		sessionNrPattern = Pattern.compile("(\\d+)\\.\\sSitzung\\sdes\\sNationalrates");
		startEndDatePattern = Pattern.compile("\\w+, (\\d+)\\. (\\w+) (\\d{4}):\\s+(\\d+)\\.(\\d+).+ (\\d+)\\.(\\d+).*Uhr");
	}

	@Override
	public Session getSession(Document document) {
		String html = document.html();

		Session session = new Session();
		session.setSessionNr(getSessionNr(html));
		session.setStartDate(getStartDate(html));
		session.setEndDate(getEndDate(html));

		return session;
	}

	/**
	 * returns the session number extracted from the HTML by regular expression
	 * 
	 * @param html
	 * @return
	 */
	protected Integer getSessionNr(String html) {
		Matcher matcher = sessionNrPattern.matcher(html);

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

	protected Date getStartDate(String html) {
		Matcher matcher = startEndDatePattern.matcher(html);
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

	protected Date getEndDate(String html) {
		Matcher matcher = startEndDatePattern.matcher(html);
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
}
