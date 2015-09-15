package at.jku.tk.hiesmair.gv.parliament.etl.period.transformer.session;

import java.io.IOException;
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
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import at.jku.tk.hiesmair.gv.parliament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parliament.entities.mandate.NationalCouncilMember;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;
import at.jku.tk.hiesmair.gv.parliament.entities.session.Session;
import at.jku.tk.hiesmair.gv.parliament.entities.session.SessionChairMan;
import at.jku.tk.hiesmair.gv.parliament.etl.AbstractTransformer;
import at.jku.tk.hiesmair.gv.parliament.etl.period.transformer.session.discussion.DiscussionTransformer;
import at.jku.tk.hiesmair.gv.parliament.etl.politician.transformer.PoliticianTransformer;

public abstract class AbstractSessionTransformer extends AbstractTransformer implements SessionTransformer {

	private static final Logger logger = Logger.getLogger(AbstractSessionTransformer.class.getSimpleName());

	protected static final String DATE_TIME_FORMAT_PATTERN = "dd.MM.yyyy HH.mm";
	protected static final String DATE_FORMAT_PATTERN = "dd.MM.yyyy";

	protected static final Pattern SESSION_NR_PATTERN = Pattern.compile("(\\d+)\\.\\sSitzung\\sdes\\sNationalrates");
	protected static final Pattern DATE_PATTERN = Pattern.compile("[\\wäüöÄÜÖ]+[\\.,] (\\d+)\\.\\s([\\wäüöÄÜÖ]+) (\\d{4})");
	protected static final Pattern START_TIME_PATTERN = Pattern.compile("Beginn der Sitzung:? ?(\\d{1,2})(?:[\\.:](\\d+))?\\s");
	protected static final Pattern END_TIME_PATTERN = Pattern.compile("(?:Schluß|Ende|Schluss) der Sitzung:? ?(\\d{1,2})(?:[\\.:](\\d+))?\\s");
	
	protected static final Pattern ABSENT_MEMBERS_PATTERN = Pattern
			.compile("(?:Abgeordneten?r? |: )((?:(?:\\s*[\\wäöüÄÖÜßáé]+\\.( |-)(\\(FH\\))?)*(?:\\s*[\\wäöüÄÖÜßáé-]+)(?:(?:,)|(?: und)|))+)(?:\\.| als verhindert gemeldet\\.)");
	protected static final Pattern ABSENT_MEMBERS_NAME_PATTERN = Pattern
			.compile("^((?:(?:[\\wäöüÄÖÜßáé]+\\.(?: |-))(?:\\(FH\\))?)*)\\s*([\\s\\wäöüÄÖÜßáé-]+)$");

	protected static final List<String> MONTH_NAMES = Arrays.asList("Jänner", "Februar", "März", "April", "Mai",
			"Juni", "Juli", "August", "September", "Oktober", "November", "Dezember");

	protected final PoliticianTransformer politicianTransformer;
	protected final DiscussionTransformer discussionTransformer;

	public AbstractSessionTransformer(PoliticianTransformer politicianTransformer,
			DiscussionTransformer discussionTransformer) {
		this.discussionTransformer = discussionTransformer;
		this.politicianTransformer = politicianTransformer;
	}

	@Override
	public Session getSession(LegislativePeriod period, String sessionTitle, Document index, Document protocol) throws Exception{
		protocol = filterPageBreaks(period.getPeriod(), protocol);

		String protocolText = protocol.text().replaceAll(NBSP_STRING, " ");

		Session session = new Session();
		session.setPeriod(period);
		session.setSessionTitle(sessionTitle);
		Date sessionDate = getDate(protocolText);
		session.setSessionNr(getSessionNr(protocolText));

		logger.info("transforming session " + session.getSessionNr() + " of period " + period.getPeriod() + " at "
				+ sessionDate);

		session.setStartDate(getStartDate(sessionDate, protocolText));
		session.setEndDate(getEndDate(sessionDate, protocolText));
		session.setChairMen(getChairMen(protocol, session));
		session.setDiscussions(discussionTransformer.getDiscussions(index, protocol, session));

		if (sessionDate != null) {
			Set<NationalCouncilMember> membersWhoShouldBePresent = period.getNationalCouncilMembersAt(sessionDate);

			if (membersWhoShouldBePresent.size() != 183) {
				logger.info("members who sould be present: " + membersWhoShouldBePresent.size());
			}

			Set<NationalCouncilMember> absentMembers = getAbsentNationalCouncilMembers(protocol,
					sessionDate, membersWhoShouldBePresent);

			membersWhoShouldBePresent.removeAll(absentMembers);

			session.setAbsentNationalCouncilMembers(absentMembers);
			session.setPresentNationalCouncilMembers(membersWhoShouldBePresent);
		}

		return session;
	}

	protected Document filterPageBreaks(Integer period, Document protocol) {
		protocol.select("hr + table:matches(^Nationalrat,)").remove();
		protocol.getElementsByTag("hr").remove();
		protocol.select("span.threecol").remove();
		protocol.select("a[name*=Seite]").remove();

		return protocol;
	}

	protected Set<NationalCouncilMember> getAbsentNationalCouncilMembers(Document protocol, Date startDate,
			Set<NationalCouncilMember> membersWhoShouldBePresent) {
		Set<NationalCouncilMember> absentMemebers = new HashSet<NationalCouncilMember>();

		Element absentMembersElement = getAbsentMemebersElement(protocol);
		if (absentMembersElement != null) {
			String elementText = getAbsentElementText(absentMembersElement);

			Matcher m = ABSENT_MEMBERS_PATTERN.matcher(elementText);
			if (m.find()) {
				String match = m.group(1);
				String[] names = match.split("((, )|( und )|( sowie ))");
				absentMemebers = getMembersByNames(startDate, names, membersWhoShouldBePresent);
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

	private Set<NationalCouncilMember> getMembersByNames(Date startDate, String[] names,
			Set<NationalCouncilMember> members) {
		Set<NationalCouncilMember> absentMemebers = new HashSet<NationalCouncilMember>();

		for (String name : names) {
			NationalCouncilMember absentMember = getAbsentMemberByTitleAndSurName(startDate, name, members);
			if (absentMember != null) {
				absentMemebers.add(absentMember);
			}
		}

		return absentMemebers;
	}

	private NationalCouncilMember getAbsentMemberByTitleAndSurName(Date startDate, String name,
			Set<NationalCouncilMember> members) {
		name = name.trim().replaceAll(" als verhindert gemeldet", "");
		name = name.replaceAll("unsere? ", "");
		name = name.replaceAll("Herr ", "");
		name = name.replaceAll("Frau ", "");
		name = name.replaceAll("[Dd]ritter? ", "");
		name = name.replaceAll("[Zz]weiter? ", "");
		name = name.replaceAll("((der|die) )?Präsident(in)? ", "");
		name = name.replaceAll("((der|die) )?Klubvorsitzender? ", "");
		name = name.replaceAll("((der|die) )?Abgeordneten?r? ", "");
		name = name.replaceAll("(der )?Klubobmann ", "");

		Matcher m = ABSENT_MEMBERS_NAME_PATTERN.matcher(name.trim());
		if (m.find()) {
			String titles = m.group(1);
			String surName = m.group(2).trim();
			return getAbsentMemberByTitleAndSurName(startDate, titles, surName, members);
		}

		logger.info("name not recognized (pattern did not match): " + name);
		return null;
	}

	private NationalCouncilMember getAbsentMemberByTitleAndSurName(Date startDate, String titles, String surName,
			Set<NationalCouncilMember> members) {
		List<NationalCouncilMember> matchedMembers = getSurNameMatchingMembers(surName, startDate, members);

		if (matchedMembers.isEmpty()) {
			String[] dashParts = surName.split("-");
			if (dashParts.length > 1) {
				for (String dashPart : dashParts) {
					matchedMembers = getSurNameMatchingMembers(dashPart, startDate, members);
					if (matchedMembers.size() == 1) {
						return matchedMembers.get(0);
					}
				}
			}

			String[] nameParts = surName.split(" ");
			if (nameParts.length > 1) {
				matchedMembers = members
						.stream()
						.filter(m -> m.getPolitician().getNameAt(startDate).getSurName()
								.equalsIgnoreCase(nameParts[nameParts.length - 1])).collect(Collectors.toList());

				if (matchedMembers.size() > 1) {
					matchedMembers = matchedMembers
							.stream()
							.filter(m -> m.getPolitician().getNameAt(startDate).getFirstName().toLowerCase()
									.contains(nameParts[0].toLowerCase())).collect(Collectors.toList());
				}
				if (matchedMembers.size() != 1) {
					matchedMembers = members
							.stream()
							.filter(m -> m.getPolitician().getNameAt(startDate).getSurName()
									.equalsIgnoreCase(nameParts[0])).collect(Collectors.toList());

					if (matchedMembers.size() > 1) {
						matchedMembers = matchedMembers
								.stream()
								.filter(m -> m.getPolitician().getNameAt(startDate).getFirstName().toLowerCase()
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
				matchedMembers = matchedMembers.stream()
						.filter(m -> m.getPolitician().getNameAt(startDate).getTitle().isEmpty())
						.collect(Collectors.toList());
				if (matchedMembers.size() == 1) {
					return matchedMembers.get(0);
				}
			}
			else {
				String[] titleArray = titles.split("[ -]");
				for (String title : titleArray) {
					matchedMembers = matchedMembers
							.stream()
							.filter(m -> m.getPolitician().getNameAt(startDate).getTitle().toLowerCase()
									.contains(title.toLowerCase())).collect(Collectors.toList());
					if (matchedMembers.size() == 1) {
						return matchedMembers.get(0);
					}
				}
			}
		}

		logger.info("no member matching name " + titles + " " + surName + " found");
		return null;
	}

	protected List<NationalCouncilMember> getSurNameMatchingMembers(String surName, Date startDate,
			Set<NationalCouncilMember> members) {
		return members.stream()
				.filter(m -> m.getPolitician().getNameAt(startDate).getSurName().equalsIgnoreCase(surName))
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
		Matcher matcher = SESSION_NR_PATTERN.matcher(protocolText);

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

	protected Date getDate(String protocolText){
		Matcher matcher = DATE_PATTERN.matcher(protocolText);

		if (matcher.find()) {
			String day = matcher.group(1);
			String month = matcher.group(2);
			Integer monthIndex = getMonthIndex(month);
			String year = matcher.group(3);

			return getDate(day, monthIndex, year);
		}
		logger.info("date not found");

		return null;
	}
	
	protected Date getDate(String day, Integer monthIndex, String year) {
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_PATTERN);

		try {
			return format.parse(day + "." + monthIndex + "." + year);
		} catch (ParseException pe) {
			logger.info("invalid dateformat");
		}

		return null;
	}
	
	protected Date getStartDate(Date date, String protocolText) {
		Matcher matcher = START_TIME_PATTERN.matcher(protocolText);
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT_PATTERN);
		
		if (matcher.find()) {
			String hours = matcher.group(1);
			String mins = matcher.group(2);
			String startTime = mins == null ? hours + ".00" : hours + "." + mins;

			try {
				return dateTimeFormat.parse(dateFormat.format(date) + " " + startTime);
			}
			catch (ParseException pe){}
		}
		logger.info("start date not found");

		return null;
	}

	protected Integer getMonthIndex(String month) {
		Integer monthIndex = 1 + MONTH_NAMES.indexOf(month);
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

	protected Date getEndDate(Date date, String protocolText) {
		Matcher matcher = END_TIME_PATTERN.matcher(protocolText);
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT_PATTERN);

		if (matcher.find()) {
			String hours = matcher.group(1);
			String mins = matcher.group(2);
			String endTime = mins == null ? hours + ".00" : hours + "." + mins;

			try {
				return dateTimeFormat.parse(dateFormat.format(date) + " " + endTime);
			}
			catch (ParseException pe){}
		}
		logger.info("end date not found");

		return null;
	}

	protected List<SessionChairMan> getChairMen(Document protocol, Session session) throws IOException {
		List<SessionChairMan> chairMenList = new ArrayList<SessionChairMan>();

		int position = 1;

		Elements chairMenElements = protocol.select("p:matches(^Vorsitzender?:.*)");
		if (!chairMenElements.isEmpty()) {
			Elements chairMenLinks = politicianTransformer.getPoliticianLinks(chairMenElements.first());
			for (Element chairMenLink : chairMenLinks) {
				String href = chairMenLink.attr("href");
				Politician politician = politicianTransformer.getPolitician(href);
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
