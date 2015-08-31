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
import at.jku.tk.hiesmair.gv.parlament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parlament.entities.Politician;
import at.jku.tk.hiesmair.gv.parlament.entities.discussion.Discussion;
import at.jku.tk.hiesmair.gv.parlament.entities.discussion.DiscussionSpeech;
import at.jku.tk.hiesmair.gv.parlament.entities.discussion.SpeechType;
import at.jku.tk.hiesmair.gv.parlament.entities.session.Session;
import at.jku.tk.hiesmair.gv.parlament.entities.session.SessionChairMan;
import at.jku.tk.hiesmair.gv.parlament.period.protocol.ProtocolUtil;
import at.jku.tk.hiesmair.gv.parlament.politician.transformer.PoliticianTransformer;

/**
 * Transforms the Protocols of a Session into a session object
 * 
 * @author Markus
 *
 */
public class SessionTransformer {

	private static final int SPEECH_TIME_TOLERANCE_IN_MS = 60000 * 10; // 10 min

	private static final String NBSP_STRING = Character.toString((char) 160);

	private static final Logger logger = Logger.getLogger(SessionTransformer.class.getSimpleName());

	private static final String DATE_FORMAT_PATTERN = "dd.MM.yyyy HH:mm";

	protected final Pattern sessionNrPattern;
	protected final Pattern startEndDatePattern;
	protected final Pattern discussionTypePattern;
	protected final Pattern speechBeginPattern;

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
				.compile("[\\wäüöÄÜÖ]+, (\\d+)\\.\\s([\\wäüöÄÜÖ]+) (\\d{4}):\\s+(\\d+)\\.(\\d+).+ (\\d+)\\.(\\d+).*Uhr");
		discussionTypePattern = Pattern.compile("Einzelredezeitbeschränkung:\\s+((?:\\d+)|.)\\s+min\\s+(.+)");
		speechBeginPattern = Pattern.compile("(\\d{1,2})\\.\\d{1,2}");

		politicianTransformer = new PoliticianTransformer();
		cache = DataCache.getInstance();
	}

	public Session getSession(LegislativePeriod period, Document index, Document protocol) throws Exception {
		protocol = ProtocolUtil.filterPageBreaks(protocol);
		String protocolText = protocol.text().replaceAll(NBSP_STRING, " ");

		Session session = new Session();
		session.setSessionNr(getSessionNr(protocolText));

		logger.debug("transforming session " + session.getSessionNr() + " of period " + period.getPeriod());

		session.setPeriod(period);
		session.setStartDate(getStartDate(protocolText));
		session.setEndDate(getEndDate(protocolText));
		session.setPoliticians(getPoliticians(index, protocol));
		session.setChairMen(getChairMen(protocol, session));
		session.setDiscussions(getDiscussions(index, protocol, session));

		return session;
	}

	/**
	 * returns the session number extracted from the HTML by regular expression
	 * 
	 * @param protocolHtml
	 * @return
	 */
	protected Integer getSessionNr(String protocolText) {
		Matcher matcher = sessionNrPattern.matcher(protocolText);

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

	protected Date getStartDate(String protocolText) {
		Matcher matcher = startEndDatePattern.matcher(protocolText);

		if (matcher.find()) {
			String day = matcher.group(1);
			String month = matcher.group(2);
			Integer monthIndex = getMonthIndex(month);
			String year = matcher.group(3);
			String hourStart = matcher.group(4);
			String minuteStart = matcher.group(5);

			return getDate(day, monthIndex, year, hourStart, minuteStart);
		}
		logger.debug("start date not found");

		return null;
	}

	protected Integer getMonthIndex(String month) {
		Integer monthIndex = 1 + monthNames.indexOf(month);
		if (monthIndex == 0) {
			if (month.equals("Januar")) {
				monthIndex = 1;
			}
			else if (month.equals("Feber")) {
				monthIndex = 2;
			}
			else {
				logger.debug("unknown month-index");
				monthIndex = 1;
			}
		}
		return monthIndex;
	}

	protected Date getEndDate(String protocolText) {
		Matcher matcher = startEndDatePattern.matcher(protocolText);

		if (matcher.find()) {
			String day = matcher.group(1);
			String month = matcher.group(2);
			Integer monthIndex = getMonthIndex(month);
			String year = matcher.group(3);
			String hourStart = matcher.group(6);
			String minuteStart = matcher.group(7);

			return getDate(day, monthIndex, year, hourStart, minuteStart);
		}
		logger.debug("end date not found");

		return null;
	}

	protected Date getDate(String day, Integer monthIndex, String year, String hourStart, String minuteStart) {
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_PATTERN);

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

		Elements links = getPoliticianLinks(document);
		for (Element link : links) {
			Politician politician = getPolitician(link.attr("href"));
			if (politician != null) {
				politicians.add(politician);
			}
		}

		return politicians;
	}

	protected Elements getPoliticianLinks(Element document) {
		return document.select("a[href*=WWER/PAD]");
	}

	protected Politician getPolitician(String href) throws Exception {
		String url = Settings.BASE_URL + href;
		if (!href.endsWith("index.shtml")) {
			url += "index.shtml";
		}

		Politician politician = cache.getPolitician(url);
		if (politician == null) {
			politician = politicianTransformer.getPolitician(url);
		}
		return politician;
	}

	protected List<Discussion> getDiscussions(Document index, Document protocol, Session session) throws Exception {
		List<Discussion> discussions = new ArrayList<Discussion>();

		Elements headers = index.select("h3");
		int order = 1;
		for (Element header : headers) {
			String text = header.text().replaceAll(NBSP_STRING, " ");

			if (isTopicRelevant(text)) {
				Discussion discussion = getDiscussion(header, text, session, order);
				discussions.add(discussion);
				order++;
			}
		}

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH.mm");

		if (discussions.size() > 0) {
			int found = 0;

			Elements speechBeginnings = getSpeechBeginTags(protocol);
			for (Element speechBegin : speechBeginnings) {
				String timeStr = speechBegin.text().replace(NBSP_STRING, " ").trim();
				Matcher m = speechBeginPattern.matcher(timeStr);
				Date time = null;
				if (m.find()) {
					try {
						time = timeFormat.parse(m.group(0));
					} catch (ParseException pe) {
					}
				}

				if (time != null) {
					Element speechPart = speechBegin.nextElementSibling();
					if (speechPart == null) {
						speechPart = speechBegin.parent().nextElementSibling().child(0);
					}
					if (speechPart != null) {
						for (; speechPart.select("a[href]").isEmpty();) {
							Element nextSibling = speechPart.nextElementSibling();
							if (nextSibling == null) {
								speechPart = speechPart.parent().nextElementSibling().child(0);
							}
							else {
								speechPart = nextSibling;
							}
						}
						String firstText = speechPart.text();

						Elements links = getPoliticianLinks(speechPart);
						if (links.size() > 0) {
							Politician politician = getPolitician(links.get(0).attr("href"));
							int indexOfColon = firstText.indexOf(":");
							if (indexOfColon != -1) {

								String speechText = firstText.substring(indexOfColon + 1).trim();

								speechPart = speechPart.nextElementSibling();
								for (; speechPart != null && !speechPart.className().equals("RE"); speechPart = speechPart
										.nextElementSibling()) {
									speechText += "\n\n" + speechPart.text();
								}

								for (Discussion discussion : discussions) {
									for (DiscussionSpeech speech : discussion.getSpeeches()) {
										if (speech.getPolitician().equals(politician)
												&& isTimeForSpeechCorrect(time, speech) && speech.getText() == null) {

											found++;
											speech.setText(speechText);
											break;
										}
									}
								}
							}
							else {
								logger.debug("no colon " + firstText);
							}
						}
						else {
							logger.debug("no link " + firstText);
						}
					}
				}
				else{
					logger.debug("unable to parse start time");
				}
			}

			int speechCnt = 0;
			for (Discussion discussion : discussions) {
				speechCnt += discussion.getSpeeches().size();
			}

			if (speechCnt != found) {
				logger.debug("not all speechtexts found in protocol: found " + found + " of " + speechCnt);
			}
		}

		return discussions;
	}

	protected Elements getSpeechBeginTags(Document protocol) {
		return protocol.select("p.RB");
	}

	protected boolean isTimeForSpeechCorrect(Date time, DiscussionSpeech speech) {
		if (speech.getStartTime() == null) {
			return false;
		}
		return time.getTime() >= speech.getStartTime().getTime() - SPEECH_TIME_TOLERANCE_IN_MS
				&& time.getTime() <= speech.getStartTime().getTime() + SPEECH_TIME_TOLERANCE_IN_MS;
	}

	protected Discussion getDiscussion(Element header, String text, Session session, int order) throws Exception {
		Discussion discussion = new Discussion();
		discussion.setSession(session);
		discussion.setTopic(text);
		discussion.setOrder(order);

		Element nextElement = header.nextElementSibling();
		discussion.setType(getDiscussionType(header, nextElement, discussion));

		nextElement = getNextTableElement(nextElement);

		if (nextElement != null && nextElement.tagName().equals("table")) {
			List<DiscussionSpeech> speeches = getDiscussionSpeeches(discussion, nextElement);
			discussion.setSpeeches(speeches);
		}

		return discussion;
	}

	protected List<DiscussionSpeech> getDiscussionSpeeches(Discussion discussion, Element tableElement)
			throws Exception {
		List<DiscussionSpeech> speeches = new ArrayList<DiscussionSpeech>();

		Elements rows = tableElement.select("tbody tr");
		for (Element tr : rows) {
			Elements tds = tr.select("td");

			if (!tds.get(1).text().contains("n. anw.")) {
				DiscussionSpeech speech = getDiscussionSpeech(discussion, tds);
				speeches.add(speech);
			}
		}
		return speeches;
	}

	protected DiscussionSpeech getDiscussionSpeech(Discussion discussion, Elements tds) throws Exception {
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

		DiscussionSpeech speech = new DiscussionSpeech();
		speech.setDiscussion(discussion);

		Elements links = getPoliticianLinks(tds.get(2));
		if (links != null && links.size() >= 1) {
			String href = links.first().attr("href");
			speech.setPolitician(getPolitician(href));
		}
		else {
			logger.warn("no politician link in speech");
		}

		String speechType = tds.get(4).text();
		String speechOrder = tds.get(0).text().trim();
		try {
			speech.setOrder(Integer.parseInt(speechOrder));
		} catch (NumberFormatException nfe) {
			logger.debug("speech order nr invalid");
		}
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
		return speech;
	}

	protected Element getNextTableElement(Element nextElement) {
		while (nextElement != null) {
			String tagName = nextElement.tagName();
			String text = nextElement.text();

			if (tagName.equals("h3")) {
				if (isTopicRelevant(text)) {
					break;
				}
				else {
					nextElement = nextElement.nextElementSibling();
					if (nextElement == null) {
						break;
					}
					nextElement = nextElement.nextElementSibling();
				}
			}
			else if (tagName.equals("table")) {
				break;
			}
			else {
				nextElement = nextElement.nextElementSibling();
			}
		}
		return nextElement;
	}

	protected String getDiscussionType(Element header, Element nextElement, Discussion discussion) {
		String descriptionText = Jsoup.parse(header.nextSibling().toString()).text().replaceAll(NBSP_STRING, " ");

		if (nextElement != null && nextElement.tag().toString().equals("a")) {
			descriptionText += " " + nextElement.text().replaceAll(NBSP_STRING, " ");
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

	protected List<SessionChairMan> getChairMen(Document protocol, Session session) throws Exception {
		List<SessionChairMan> chairMenList = new ArrayList<SessionChairMan>();

		int position = 1;

		Elements chairMenElements = protocol.select("p:matches(^Vorsitzender?:.*)");
		if (chairMenElements.size() == 1) {
			Elements chairMenLinks = getPoliticianLinks(chairMenElements.first());
			for (Element chairMenLink : chairMenLinks) {
				String href = chairMenLink.attr("href");
				Politician politician = getPolitician(href);
				chairMenList.add(new SessionChairMan(position, politician, session));
				position++;

			}
		}
		else {
			logger.debug("no chairmen tag found");
		}

		return chairMenList;
	}

	// private Element getBeginningOfSessionElement(Document protocol) {
	// Elements sbs = protocol.select("p.SB");
	// for (Element sb : sbs) {
	// if (sb.text().contains("Beginn der Sitzung")) {
	// return sb;
	// }
	// }
	//
	// Elements spans = protocol.select("span");
	// for (Element el : spans) {
	// if (el.text().startsWith("Beginn der Sitzung")) {
	// return el.parent();
	// }
	// }
	// logger.debug("beginning of session not found");
	// return null;
	// }
}
