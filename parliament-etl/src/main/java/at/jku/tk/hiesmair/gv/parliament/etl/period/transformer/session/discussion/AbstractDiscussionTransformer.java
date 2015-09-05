package at.jku.tk.hiesmair.gv.parliament.etl.period.transformer.session.discussion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import at.jku.tk.hiesmair.gv.parliament.entities.discussion.Discussion;
import at.jku.tk.hiesmair.gv.parliament.entities.discussion.DiscussionSpeech;
import at.jku.tk.hiesmair.gv.parliament.entities.discussion.SpeechType;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;
import at.jku.tk.hiesmair.gv.parliament.entities.session.Session;
import at.jku.tk.hiesmair.gv.parliament.etl.AbstractTransformer;
import at.jku.tk.hiesmair.gv.parliament.etl.politician.transformer.PoliticianTransformer;

public abstract class AbstractDiscussionTransformer extends AbstractTransformer implements DiscussionTransformer {

	private static final Logger logger = Logger.getLogger(AbstractDiscussionTransformer.class.getSimpleName());

	protected static final int SPEECH_TIME_TOLERANCE_IN_MS = 60000 * 10; // 10
																			// min

	protected static final Pattern SPEECH_BEGIN_PATTERN = Pattern.compile("(\\d{1,2})\\.\\d{1,2}");
	protected static final Pattern DISCUSSION_TYPE_PATTERN = Pattern
			.compile("Einzelredezeitbeschränkung:\\s+((?:\\d+)|.)\\s+min\\s+(.+)");

	protected static final List<String> TOPIC_EXCEPTIONS = Arrays.asList("Sitzung des Nationalrates",
			"Sitzungsunterbrechung", "Verhandlungsgegenstände Suchhilfen", "Blockredezeit der Debatte",
			"Blockredezeit der Sitzung");

	protected final PoliticianTransformer politicianTransformer;

	@Inject
	public AbstractDiscussionTransformer(PoliticianTransformer politicianTransformer) {
		this.politicianTransformer = politicianTransformer;
	}

	@Override
	public List<Discussion> getDiscussions(Document index, Document protocol, Session session) throws Exception {
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

		if (discussions.size() > 0) {
			discussions = setSpeechTexts(protocol, discussions);
			checkIfAllSpeechTextsWereFound(discussions);
		}

		return discussions;
	}

	protected List<Discussion> setSpeechTexts(Document protocol, List<Discussion> discussions) throws Exception {
		Elements speechBeginnings = getSpeechBeginElements(protocol);
		for (Element speechBegin : speechBeginnings) {
			Date time = getBeginTime(speechBegin);

			if (time != null) {
				Element speechPartElement = getFirstSpeechTextElement(speechBegin);

				if (speechPartElement != null) {
					Politician politician = getPoliticianOfSpeech(speechPartElement);
					if (politician != null) {
						String speechText = getSpeechText(speechPartElement);
						if (speechText != null) {
							setSpeechText(discussions, time, politician, speechText);
						}
						else {
							logger.debug("no colon " + speechPartElement);
						}
					}
					else {
						logger.debug("did not find politician: " + speechPartElement);
					}
				}
				else {
					logger.debug("speechPart-Tag is null");
				}
			}
			else {
				logger.info("unable to parse start time");
			}
		}

		return discussions;
	}

	protected void checkIfAllSpeechTextsWereFound(List<Discussion> discussions) {
		int speechCnt = discussions.stream().mapToInt(d -> d.getSpeeches().size()).sum();
		int speechesWithTexts = Long
				.valueOf(
						discussions
								.stream()
								.mapToLong(
										d -> d.getSpeeches().stream()
												.filter(sp -> sp.getText() != null || sp.getStartTime() == null)
												.count()).sum()).intValue();

		if (speechCnt != speechesWithTexts) {
			logger.warn("not all speechtexts found in protocol: found " + speechesWithTexts + " of " + speechCnt);
		}
	}

	protected Date getBeginTime(Element speechBeginElement) {
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH.mm");

		String timeStr = speechBeginElement.text().replace(NBSP_STRING, " ").trim();
		Matcher m = SPEECH_BEGIN_PATTERN.matcher(timeStr);
		Date time = null;
		if (m.find()) {
			try {
				time = timeFormat.parse(m.group(0));
			} catch (ParseException pe) {
			}
		}
		return time;
	}

	protected void setSpeechText(List<Discussion> discussions, Date time, Politician politician, String speechText) {
		for (Discussion discussion : discussions) {
			for (DiscussionSpeech speech : discussion.getSpeeches()) {
				if (speech.getPolitician().equals(politician) && isTimeForSpeechCorrect(time, speech)
						&& speech.getText() == null) {
					speech.setText(speechText);
					return;
				}
			}
		}
		logger.debug("did not find corresponding speech (" + politician.getSurName() + ", " + time);
	}

	protected abstract Politician getPoliticianOfSpeech(Element firstSpeechTextElement) throws Exception;

	/**
	 * returns the speech text
	 * 
	 * @param speechPartElement
	 * @return
	 */
	protected abstract String getSpeechText(Element speechPartElement);

	/**
	 * Gets element that contains the first part of the actual text of the
	 * speech
	 * 
	 * @param speechBeginElement
	 * @return
	 */
	protected abstract Element getFirstSpeechTextElement(Element speechBegin);

	/**
	 * Get speech begin-elements that contain the time aka. 12.08
	 * 
	 * @param protocol
	 * @return
	 */
	protected abstract Elements getSpeechBeginElements(Document protocol);

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

		Elements links = politicianTransformer.getPoliticianLinks(tds.get(2));
		if (links != null && links.size() >= 1) {
			String href = links.first().attr("href");
			speech.setPolitician(politicianTransformer.getPolitician(href));
		}
		else {
			logger.warn("no politician link in speech");
		}

		String speechType = tds.get(4).text();
		String speechOrder = tds.get(0).text().trim();
		try {
			speech.setOrder(Integer.parseInt(speechOrder));
		} catch (NumberFormatException nfe) {
			logger.info("speech order nr invalid");
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
			logger.info("discussion date parse error: " + e.getMessage());
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

		Matcher m = DISCUSSION_TYPE_PATTERN.matcher(descriptionText);
		if (m.find()) {
			return m.group(2).trim();
		}

		logger.info("did not find discussionType");
		return null;
	}

	protected boolean isTopicRelevant(String text) {
		return !TOPIC_EXCEPTIONS.stream().anyMatch(te -> text.contains(te));
	}

}
