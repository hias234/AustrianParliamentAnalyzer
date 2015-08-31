package at.jku.tk.hiesmair.gv.parlament.etl.period.transformer.session;

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
import java.util.stream.Collectors;

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
import at.jku.tk.hiesmair.gv.parlament.entities.mandate.NationalCouncilMember;
import at.jku.tk.hiesmair.gv.parlament.entities.session.Session;
import at.jku.tk.hiesmair.gv.parlament.entities.session.SessionChairMan;
import at.jku.tk.hiesmair.gv.parlament.etl.AbstractTransformer;
import at.jku.tk.hiesmair.gv.parlament.etl.politician.transformer.PoliticianTransformer;

public abstract class AbstractSessionTransformer extends AbstractTransformer {

	private static final Logger logger = Logger.getLogger(AbstractSessionTransformer.class.getSimpleName());

	protected static final int SPEECH_TIME_TOLERANCE_IN_MS = 60000 * 10; // 10
																			// min
	protected static final String DATE_FORMAT_PATTERN = "dd.MM.yyyy HH:mm";

	protected final List<String> monthNames;
	protected final List<String> topicExceptions;

	protected final Pattern sessionNrPattern;
	protected final Pattern startEndDatePattern;
	protected final Pattern discussionTypePattern;
	protected final Pattern speechBeginPattern;
	protected final Pattern absentMembersPattern;
	protected final Pattern absentMembersNamePattern;

	protected PoliticianTransformer politicianTransformer;

	protected DataCache cache;

	public AbstractSessionTransformer(DataCache cache) {
		this.cache = cache;
		this.monthNames = Arrays.asList("Jänner", "Februar", "März", "April", "Mai", "Juni", "Juli", "August",
				"September", "Oktober", "November", "Dezember");
		this.topicExceptions = Arrays.asList("Sitzung des Nationalrates", "Sitzungsunterbrechung",
				"Verhandlungsgegenstände Suchhilfen", "Blockredezeit der Debatte", "Blockredezeit der Sitzung");

		this.sessionNrPattern = Pattern.compile("(\\d+)\\.\\sSitzung\\sdes\\sNationalrates");
		this.startEndDatePattern = Pattern
				.compile("[\\wäüöÄÜÖ]+, (\\d+)\\.\\s([\\wäüöÄÜÖ]+) (\\d{4}):\\s+(\\d+)\\.(\\d+).+ (\\d+)\\.(\\d+).*Uhr");
		this.discussionTypePattern = Pattern.compile("Einzelredezeitbeschränkung:\\s+((?:\\d+)|.)\\s+min\\s+(.+)");
		this.speechBeginPattern = Pattern.compile("(\\d{1,2})\\.\\d{1,2}");
		this.absentMembersPattern = Pattern
				.compile("(?:Abgeordneten?r? |: )((?:(?:\\s*[\\wäöüÄÖÜßáé]+\\.( |-)(\\(FH\\))?)*(?:\\s*[\\wäöüÄÖÜßáé-]+)(?:(?:,)|(?: und)|))+)(?:\\.| als verhindert gemeldet\\.)");
		this.absentMembersNamePattern = Pattern
				.compile("^((?:(?:[\\wäöüÄÖÜßáé]+\\.(?: |-))(?:\\(FH\\))?)*)\\s*([\\s\\wäöüÄÖÜßáé-]+)$");

		politicianTransformer = new PoliticianTransformer(cache);
	}

	public Session getSession(LegislativePeriod period, Document index, Document protocol) throws Exception {
		protocol = filterPageBreaks(protocol);

		String protocolText = protocol.text().replaceAll(NBSP_STRING, " ");

		Session session = new Session();
		session.setPeriod(period);
		session.setSessionNr(getSessionNr(protocolText));
		session.setStartDate(getStartDate(protocolText));

		logger.info("transforming session " + session.getSessionNr() + " of period " + period.getPeriod() + " at "
				+ session.getStartDate());

		session.setEndDate(getEndDate(protocolText));
		session.setChairMen(getChairMen(protocol, session));
		session.setDiscussions(getDiscussions(index, protocol, session));

		if (session.getStartDate() != null) {
			Set<NationalCouncilMember> membersWhoShouldBePresent = period.getNationalCouncilMembersAt(session
					.getStartDate());

			if (membersWhoShouldBePresent.size() != 183) {
				logger.info("members who sould be present: " + membersWhoShouldBePresent.size());
			}

			Set<NationalCouncilMember> absentMembers = getAbsentNationalCouncilMembers(protocol,
					membersWhoShouldBePresent);

			membersWhoShouldBePresent.removeAll(absentMembers);

			session.setAbsentNationalCouncilMembers(absentMembers);
			session.setPresentNationalCouncilMembers(membersWhoShouldBePresent);
		}

		return session;
	}

	protected abstract Document filterPageBreaks(Document protocol);

	protected Set<NationalCouncilMember> getAbsentNationalCouncilMembers(Document protocol,
			Set<NationalCouncilMember> membersWhoShouldBePresent) {
		Set<NationalCouncilMember> absentMemebers = new HashSet<NationalCouncilMember>();

		Element absentMembersElement = getAbsentMemebersElement(protocol);
		if (absentMembersElement != null) {
			String elementText = getAbsentElementText(absentMembersElement);

			Matcher m = absentMembersPattern.matcher(elementText);
			if (m.find()) {
				String match = m.group(1);
				String[] names = match.split("((, )|( und )|( sowie ))");
				absentMemebers = getMembersByNames(names, membersWhoShouldBePresent);
			}
			else {
				logger.warn("no absent memebers found -> pattern did not match");
			}
		}

		return absentMemebers;
	}

	protected String getAbsentElementText(Element absentMembersElement) {
		String elementText = absentMembersElement.text().replaceAll(NBSP_STRING, " ");
		elementText = elementText.replaceAll(SOFT_HYPHEN_STRING, "");
		elementText = elementText.replaceAll(EN_DASH_STRING, "");
		elementText = elementText.replaceAll(EM_DASH_STRING, "");
		elementText = elementText.replaceAll(" ?\\(.{3,}\\) ?", "");
		elementText = elementText.replaceAll(" und jene Abgeordneten, die am Europa-Konvent teilnehmen:", ",");
		elementText = elementText.replaceAll(" ,", ",");

		return elementText;
	}

	private Set<NationalCouncilMember> getMembersByNames(String[] names, Set<NationalCouncilMember> members) {
		Set<NationalCouncilMember> absentMemebers = new HashSet<NationalCouncilMember>();

		for (String name : names) {
			NationalCouncilMember absentMember = getAbsentMemberByTitleAndSurName(name, members);
			if (absentMember != null) {
				absentMemebers.add(absentMember);
			}
		}

		return absentMemebers;
	}

	private NationalCouncilMember getAbsentMemberByTitleAndSurName(String name, Set<NationalCouncilMember> members) {
		name = name.trim().replaceAll(" als verhindert gemeldet", "");
		name = name.replaceAll("unsere ", "");
		name = name.replaceAll("Herr ", "");
		name = name.replaceAll("Frau ", "");
		name = name.replaceAll("[Dd]ritter? ", "");
		name = name.replaceAll("[Zz]weiter? ", "");
		name = name.replaceAll("Präsident(in)? ", "");
		name = name.replaceAll("Klubvorsitzender? ", "");
		name = name.replaceAll("der ", "");
		name = name.replaceAll("die ", "");
		name = name.replaceAll("Abgeordneten?r? ", "");
		name = name.replaceAll("Klubobmann ", "");

		Matcher m = absentMembersNamePattern.matcher(name.trim());
		if (m.find()) {
			String titles = m.group(1);
			String surName = m.group(2).trim();
			return getAbsentMemberByTitleAndSurName(titles, surName, members);
		}

		logger.info("name not recognized (pattern did not match): " + name);
		return null;
	}

	private NationalCouncilMember getAbsentMemberByTitleAndSurName(String titles, String surName,
			Set<NationalCouncilMember> members) {
		List<NationalCouncilMember> matchedMembers = getSurNameMatchingMembers(surName, members);

		if (matchedMembers.size() == 0) {
			String[] dashParts = surName.split("-");
			if (dashParts.length > 1) {
				for (String dashPart : dashParts) {
					matchedMembers = getSurNameMatchingMembers(dashPart, members);
					if (matchedMembers.size() == 1) {
						return matchedMembers.get(0);
					}
				}
			}

			String[] nameParts = surName.split(" ");
			if (nameParts.length > 1) {
				matchedMembers = members.stream()
						.filter(m -> m.getPolitician().getSurName().equalsIgnoreCase(nameParts[nameParts.length - 1]))
						.collect(Collectors.toList());

				if (matchedMembers.size() > 1) {
					matchedMembers = matchedMembers
							.stream()
							.filter(m -> m.getPolitician().getFirstName().toLowerCase()
									.contains(nameParts[0].toLowerCase())).collect(Collectors.toList());
				}
				if (matchedMembers.size() != 1) {
					matchedMembers = members.stream()
							.filter(m -> m.getPolitician().getSurName().equalsIgnoreCase(nameParts[0]))
							.collect(Collectors.toList());

					if (matchedMembers.size() > 1) {
						matchedMembers = matchedMembers
								.stream()
								.filter(m -> m.getPolitician().getFirstName().toLowerCase()
										.contains(nameParts[1].toLowerCase())).collect(Collectors.toList());
					}
				}
			}
		}

		if (matchedMembers.size() == 1) {
			return matchedMembers.get(0);
		}
		if (matchedMembers.size() > 1) {
			if (titles.trim().isEmpty()) {
				matchedMembers = matchedMembers.stream().filter(m -> m.getPolitician().getTitle().isEmpty())
						.collect(Collectors.toList());
				if (matchedMembers.size() == 1) {
					return matchedMembers.get(0);
				}
			}
			else {
				String[] titleArray = titles.split("[ -]");
				for (String title : titleArray) {
					matchedMembers = matchedMembers.stream()
							.filter(m -> m.getPolitician().getTitle().toLowerCase().contains(title.toLowerCase()))
							.collect(Collectors.toList());
					if (matchedMembers.size() == 1) {
						return matchedMembers.get(0);
					}
				}
			}
		}

		logger.info("no member matching name " + titles + " " + surName + " found");
		return null;
	}

	protected List<NationalCouncilMember> getSurNameMatchingMembers(String surName, Set<NationalCouncilMember> members) {
		return members.stream().filter(m -> m.getPolitician().getSurName().equalsIgnoreCase(surName))
				.collect(Collectors.toList());
	}

	private Element getAbsentMemebersElement(Document document) {
		Elements pElements = document.getElementsByTag("p");
		for (Element p : pElements) {
			String text = p.text().toLowerCase();
			if (text.contains("verhindert gemeldet") || text.contains("verhindert für die heutige sitzung gemeldet")) {
				return p;
			}
			else if (text.contains("keine abgeordneten als verhindert")) {
				logger.info("no members absent in this session");
				break;
			}
		}

		logger.info("no absentMember element found");
		return null;
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
				logger.info("invalid session number");
			}
		}

		logger.info("session number not found");
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
		logger.info("start date not found");

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
				logger.info("unknown month-index");
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
		logger.info("end date not found");

		return null;
	}

	protected Date getDate(String day, Integer monthIndex, String year, String hourStart, String minuteStart) {
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_PATTERN);

		try {
			return format.parse(day + "." + monthIndex + "." + year + " " + hourStart + ":" + minuteStart);
		} catch (ParseException pe) {
			logger.info("invalid dateformat");
		}

		return null;
	}

	// protected List<Politician> getPoliticians(Document index, Document
	// protocol) throws Exception {
	// Set<Politician> politicians = new HashSet<Politician>();
	// politicians.addAll(getPoliticians(index));
	// politicians.addAll(getPoliticians(protocol));
	//
	// return new ArrayList<Politician>(politicians);
	// }
	//
	// protected Set<Politician> getPoliticians(Document document) throws
	// Exception {
	// Set<Politician> politicians = new HashSet<Politician>();
	//
	// Elements links = getPoliticianLinks(document);
	// for (Element link : links) {
	// Politician politician = getPolitician(link.attr("href"));
	// if (politician != null) {
	// politicians.add(politician);
	// }
	// }
	//
	// return politicians;
	// }

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

		if (discussions.size() > 0) {
			discussions = setSpeechTexts(protocol, discussions);
		}

		return discussions;
	}

	protected abstract List<Discussion> setSpeechTexts(Document protocol, List<Discussion> discussions) throws Exception;
	
	protected Elements getSpeechBeginTags(Document protocol) {
		return protocol.select("p.RB:matches(\\d{2}\\.\\d{2}.*)");
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

		Matcher m = discussionTypePattern.matcher(descriptionText);
		if (m.find()) {
			return m.group(2).trim();
		}
		
		logger.info("did not find discussionType");
		return null;
	}

	protected boolean isTopicRelevant(String text) {
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
			logger.info("no chairmen tag found");
		}

		return chairMenList;
	}

}
