package at.jku.tk.hiesmair.gv.parlament.period.transformer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import at.jku.tk.hiesmair.gv.parlament.Settings;
import at.jku.tk.hiesmair.gv.parlament.cache.DataCache;
import at.jku.tk.hiesmair.gv.parlament.entities.Politician;
import at.jku.tk.hiesmair.gv.parlament.entities.Session;
import at.jku.tk.hiesmair.gv.parlament.entities.discussion.Discussion;
import at.jku.tk.hiesmair.gv.parlament.entities.discussion.DiscussionSpeech;
import at.jku.tk.hiesmair.gv.parlament.entities.discussion.SpeechType;
import at.jku.tk.hiesmair.gv.parlament.politician.transformer.PoliticianTransformer;

/**
 * Transforms the Protocols of a Session into a session object
 * 
 * @author Markus
 *
 */
public class SessionTransformer {

	private static final Logger logger = Logger.getLogger(SessionTransformer.class.getSimpleName());

	private static final String DATE_FORMAT_PATTERN = "dd.MM.yyyy HH:mm";

	protected final Pattern sessionNrPattern;
	protected final Pattern startEndDatePattern;
	protected final Pattern discussionTypePattern;

	protected final List<String> monthNames;
	protected final List<String> topicExceptions;

	protected PoliticianTransformer politicianTransformer;

	protected DataCache cache;

	public SessionTransformer() {
		monthNames = Arrays.asList("Jänner", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September",
				"Oktober", "November", "Dezember");
		topicExceptions = Arrays.asList("Sitzung des Nationalrates", "Sitzungsunterbrechung",
				"Verhandlungsgegenstände Suchhilfen", "Blockredezeit der Debatte", "Blockredezeit der Sitzung");

		sessionNrPattern = Pattern.compile("(\\d+)\\.\\sSitzung\\sdes\\sNationalrates");
		startEndDatePattern = Pattern
				.compile("[\\wäüöÄÜÖ]+, (\\d+)\\. ([\\wäüöÄÜÖ]+) (\\d{4}):\\s+(\\d+)\\.(\\d+).+ (\\d+)\\.(\\d+).*Uhr");
		discussionTypePattern = Pattern.compile("Einzelredezeitbeschränkung:\\s+((?:\\d+)|.)\\s+min\\s+(.+)");

		politicianTransformer = new PoliticianTransformer();
		cache = DataCache.getInstance();
	}

	public Session getSession(Document index, Document protocol) throws Exception {
		String protocolHtml = protocol.html();

		Session session = new Session();
		session.setSessionNr(getSessionNr(protocolHtml));
		session.setStartDate(getStartDate(protocolHtml));
		session.setEndDate(getEndDate(protocolHtml));
		session.setPoliticians(getPoliticians(index, protocol));
		session.setDiscussions(getDiscussions(index));

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
				logger.debug("invalid session number");
			}
		}
		logger.debug("session number not found");

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
		logger.debug("start date not found");

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
		logger.debug("end date not found");

		return null;
	}

	protected Date getDate(SimpleDateFormat format, String day, Integer monthIndex, String year, String hourStart,
			String minuteStart) {
		try {
			return format.parse(day + "." + monthIndex + "." + year + " " + hourStart + ":" + minuteStart);
		} catch (ParseException pe) {
			logger.debug("invalid dateformat");
		}

		return null;
	}

	protected List<Politician> getPoliticians(Document index, Document protocol) throws Exception {
		Set<Politician> politicians = new HashSet<Politician>();
		politicians.addAll(getPoliticians(index));
		politicians.addAll(getPoliticians(protocol));

		return new ArrayList<Politician>(politicians);
	}

	protected Set<Politician> getPoliticians(Document document) throws Exception {
		Set<Politician> politicians = new HashSet<Politician>();

		Elements links = document.getElementsByTag("a");
		for (Element link : links) {
			String href = link.attr("href");
			if (isPoliticianLink(href)) {
				Politician politician = getPolitician(href);
				if (politician != null) {
					politicians.add(politician);
				}
			}
		}

		return politicians;
	}

	protected boolean isPoliticianLink(String href) {
		return href.startsWith("/WWER/PAD");
	}

	protected Politician getPolitician(String href) throws Exception {
		String url = Settings.BASE_URL + href;

		Politician politician = cache.getPolitician(url);
		if (politician == null) {
			politician = politicianTransformer.getPolitician(url);
		}
		return politician;
	}

	protected List<Discussion> getDiscussions(Document index) throws Exception {
		List<Discussion> discussions = new ArrayList<Discussion>();

		Elements headers = index.select("h3");
		for (Element header : headers) {
			String text = header.text().replaceAll(Character.toString((char) 160), " ");

			if (isTopicRelevant(text)) {
				Discussion discussion = getDiscussion(header, text);

				discussions.add(discussion);
			}
		}

		return discussions;
	}

	protected Discussion getDiscussion(Element header, String text) throws Exception {
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

		Discussion discussion = new Discussion();
		discussion.setTopic(text);

		Element nextElement = header.nextElementSibling();
		discussion.setTopic(getDiscussionType(header, nextElement, discussion));

		for (; nextElement != null && !nextElement.tag().toString().equals("h3")
				&& !nextElement.tag().toString().equals("table"); nextElement = nextElement.nextElementSibling())
			;

		if (nextElement != null && nextElement.tag().toString().equals("table")) {
			List<DiscussionSpeech> speeches = new ArrayList<DiscussionSpeech>();

			Elements rows = nextElement.select("tbody").select("tr");
			for (Element tr : rows) {
				Elements tds = tr.select("td");

				if (!tds.get(1).text().contains("n. anw.")) {
					DiscussionSpeech speech = new DiscussionSpeech();
					speech.setDiscussion(discussion);

					Element td = tds.get(2);
					if (td != null) {
						Elements links = td.select("a");
						if (links != null && links.size() >= 1) {
							String href = links.first().attr("href");
							Politician politician = getPolitician(href);
							speech.setPolitician(politician);
						}
					}

					String speechType = tds.get(4).text();
					speech.setType(SpeechType.getSpeechType(speechType));

					String start = tds.get(5).text();

					try {
						Date startTime = timeFormat.parse(start);
						speech.setStartTime(startTime);

						String duration = tds.get(5).text();
						String[] parts = duration.split(":");
						Integer minutes = Integer.parseInt(parts[0]);
						Integer seconds = Integer.parseInt(parts[1]);

						Calendar endDate = Calendar.getInstance();
						endDate.setTime(startTime);
						endDate.add(Calendar.MINUTE, minutes);
						endDate.add(Calendar.SECOND, seconds);
						speech.setEndTime(endDate.getTime());
					} catch (ParseException | NumberFormatException e) {
						logger.debug("discussion date parse error: " + e.getMessage());
					}

					speeches.add(speech);
				}
			}

			discussion.setSpeeches(speeches);
		}

		return discussion;
	}

	protected String getDiscussionType(Element header, Element nextElement, Discussion discussion) {
		String descriptionText = Jsoup.parse(header.nextSibling().toString()).text()
				.replaceAll(Character.toString((char) 160), " ");

		if (nextElement != null && nextElement.tag().toString().equals("a")) {
			descriptionText += " " + nextElement.text().replaceAll(Character.toString((char) 160), " ");
		}

		Matcher m = discussionTypePattern.matcher(descriptionText);
		if (m.find()) {
			return m.group(2).trim();
		}
		logger.debug("did not find discussionType");
		return null;
	}

	private boolean isTopicRelevant(String text) {
		return !topicExceptions.stream().anyMatch(te -> text.contains(te));
	}
}
