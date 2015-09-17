package at.jku.tk.hiesmair.gv.parliament.db.loader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import at.jku.tk.hiesmair.gv.parliament.db.DiscussionRepository;
import at.jku.tk.hiesmair.gv.parliament.db.DiscussionSpeechRepository;
import at.jku.tk.hiesmair.gv.parliament.db.LegislativePeriodRepository;
import at.jku.tk.hiesmair.gv.parliament.db.MandateRepository;
import at.jku.tk.hiesmair.gv.parliament.db.ParliamentClubRepository;
import at.jku.tk.hiesmair.gv.parliament.db.PoliticianNameRepository;
import at.jku.tk.hiesmair.gv.parliament.db.PoliticianRepository;
import at.jku.tk.hiesmair.gv.parliament.db.SessionChairManRepository;
import at.jku.tk.hiesmair.gv.parliament.db.SessionRepository;
import at.jku.tk.hiesmair.gv.parliament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parliament.entities.discussion.Discussion;
import at.jku.tk.hiesmair.gv.parliament.entities.discussion.speech.DiscussionSpeech;
import at.jku.tk.hiesmair.gv.parliament.entities.mandate.CouncilMember;
import at.jku.tk.hiesmair.gv.parliament.entities.mandate.Mandate;
import at.jku.tk.hiesmair.gv.parliament.entities.mandate.NationalCouncilMember;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.PoliticianName;
import at.jku.tk.hiesmair.gv.parliament.entities.session.Session;
import at.jku.tk.hiesmair.gv.parliament.entities.session.SessionChairMan;

@Component
public class ParliamentDatabaseLoader {

	@Inject
	private PoliticianRepository politicianRepository;

	@Inject
	private PoliticianNameRepository nameRepository;

	@Inject
	private MandateRepository mandateRepository;

	@Inject
	private ParliamentClubRepository clubRepository;

	@Inject
	private SessionRepository sessionRepository;

	@Inject
	private SessionChairManRepository sessionChairManRepository;

	@Inject
	private LegislativePeriodRepository periodRepository;

	@Inject
	private DiscussionRepository discussionRepository;

	@Inject
	private DiscussionSpeechRepository speechRepository;

	public Politician loadPolitician(Politician politician) {
		Set<Mandate> mandates = politician.getMandates();
		List<PoliticianName> prevNames = politician.getPreviousNames();

		politician.setMandates(new HashSet<Mandate>());
		politician.setPreviousNames(new ArrayList<PoliticianName>());

		Politician politicianInDb = politicianRepository.save(politician);

		politicianInDb.setMandates(loadMandates(mandates, politicianInDb));
		politicianInDb.setPreviousNames(loadPoliticianNames(prevNames));

		return politicianInDb;
	}

	private List<PoliticianName> loadPoliticianNames(List<PoliticianName> names) {
		List<PoliticianName> namesInDb = new ArrayList<PoliticianName>();

		for (PoliticianName name : names) {
			namesInDb.add(loadPoliticianName(name));
		}

		return namesInDb;
	}

	private PoliticianName loadPoliticianName(PoliticianName name) {
		PoliticianName nameInDb = nameRepository.findByPoliticianAndValidUntil(name.getPolitician(),
				name.getValidUntil());

		if (nameInDb == null) {
			nameInDb = nameRepository.save(name);
		}
		else {
			nameInDb.setName(name.getName());
			nameInDb = nameRepository.save(nameInDb);
		}

		return nameInDb;
	}

	public void loadPoliticianIfNotExists(Politician politician) {
		if (!politicianRepository.exists(politician.getId())) {
			loadPolitician(politician);
		}
	}

	protected Mandate loadMandate(Mandate mandate) {
		Mandate mandateInDb = mandateRepository.findByPoliticianAndDescriptionAndValidFrom(mandate.getPolitician(),
				mandate.getDescription(), mandate.getValidFrom());

		if (mandate instanceof CouncilMember) {
			clubRepository.save(((CouncilMember) mandate).getClub());
		}

		if (mandateInDb == null) {
			loadPoliticianIfNotExists(mandate.getPolitician());

			if (mandate instanceof NationalCouncilMember) {
				Set<LegislativePeriod> periods = ((NationalCouncilMember) mandate).getPeriods();

				((NationalCouncilMember) mandate).setPeriods(loadPeriods(periods, false));
			}
			mandateInDb = mandateRepository.save(mandate);
		}
		else {
			mandateInDb.setValidUntil(mandate.getValidUntil());
			if (mandate instanceof CouncilMember) {
				((CouncilMember) mandateInDb).setClub(((CouncilMember) mandate).getClub());
			}

			if (mandate instanceof NationalCouncilMember) {
				Set<LegislativePeriod> periods = ((NationalCouncilMember) mandate).getPeriods();

				((NationalCouncilMember) mandateInDb).setPeriods(loadPeriods(periods, false));
			}
			mandateInDb = mandateRepository.save(mandateInDb);
		}

		return mandateInDb;
	}

	public Set<NationalCouncilMember> loadNationalCouncilMembers(Set<NationalCouncilMember> ncms) {
		Set<NationalCouncilMember> ncmsInDb = new HashSet<NationalCouncilMember>();

		for (NationalCouncilMember ncm : ncms) {
			ncmsInDb.add((NationalCouncilMember) loadMandate(ncm));
		}

		return ncmsInDb;
	}

	public Set<Mandate> loadMandates(Set<Mandate> mandates, Politician politician) {
		Set<Mandate> mandatesInDb = new HashSet<Mandate>();

		for (Mandate mandate : mandates) {
			mandate.setPolitician(politician);
			mandatesInDb.add(loadMandate(mandate));
		}

		return mandatesInDb;
	}

	public Set<LegislativePeriod> loadPeriods(Set<LegislativePeriod> periods, boolean shouldLoadSessions) {
		Set<LegislativePeriod> periodsInDb = new HashSet<LegislativePeriod>();

		for (LegislativePeriod period : periods) {
			periodsInDb.add(loadPeriod(period, shouldLoadSessions));
		}

		return periodsInDb;
	}

	public LegislativePeriod loadPeriod(LegislativePeriod period) {
		return loadPeriod(period, true);
	}

	protected LegislativePeriod loadPeriod(LegislativePeriod period, boolean shouldLoadSessions) {
		List<Session> sessions = period.getSessions();
		Set<NationalCouncilMember> nationalCouncilMembers = period.getNationalCouncilMembers();

		period.setSessions(new ArrayList<Session>());
		period.setNationalCouncilMembers(new HashSet<NationalCouncilMember>());

		LegislativePeriod periodInDb = periodRepository.findOne(period.getPeriod());
		if (periodInDb == null) {
			periodInDb = periodRepository.save(period);
		}

		if (shouldLoadSessions) {
			sessions = loadSessions(sessions, periodInDb);
		}

		// TODO check if nationalcouncilmembers are set on the other side
		// (should be)

		periodInDb.setSessions(sessions);
		periodInDb.setNationalCouncilMembers(nationalCouncilMembers);
		period.setSessions(sessions);
		period.setNationalCouncilMembers(nationalCouncilMembers);

		return periodInDb;
	}

	protected List<Session> loadSessions(List<Session> sessions, LegislativePeriod period) {
		List<Session> sessionsInDb = new ArrayList<Session>();

		for (Session s : sessions) {
			s.setPeriod(period);
			sessionsInDb.add(loadSession(s));
		}

		return sessionsInDb;
	}

	private Session loadSession(Session session) {
		Session sessionInDb = sessionRepository.findByPeriodAndSessionNr(session.getPeriod(), session.getSessionNr());

		List<SessionChairMan> chairMen = session.getChairMen();
		List<Discussion> discussions = session.getDiscussions();
		Set<NationalCouncilMember> presentNationalCouncilMembers = session.getPresentNationalCouncilMembers();
		Set<NationalCouncilMember> absentNationalCouncilMembers = session.getAbsentNationalCouncilMembers();

		session.setChairMen(new ArrayList<SessionChairMan>());
		session.setDiscussions(new ArrayList<Discussion>());
		session.setPresentNationalCouncilMembers(new HashSet<NationalCouncilMember>());
		session.setAbsentNationalCouncilMembers(new HashSet<NationalCouncilMember>());

		if (sessionInDb == null) {
			sessionInDb = sessionRepository.save(session);
		}
		
		chairMen = loadSessionChairMen(chairMen, sessionInDb);
		discussions = loadDiscussions(discussions, sessionInDb);
		presentNationalCouncilMembers = loadNationalCouncilMembers(presentNationalCouncilMembers);
		absentNationalCouncilMembers = loadNationalCouncilMembers(absentNationalCouncilMembers);

		sessionInDb.setChairMen(chairMen);
		sessionInDb.setDiscussions(discussions);
		sessionInDb.setPresentNationalCouncilMembers(presentNationalCouncilMembers);
		sessionInDb.setAbsentNationalCouncilMembers(absentNationalCouncilMembers);

		sessionInDb.setStartDate(session.getStartDate());
		sessionInDb.setEndDate(session.getEndDate());
		sessionInDb.setSessionTitle(session.getSessionTitle());

		// to save the councilmember-associations
		sessionInDb = sessionRepository.save(sessionInDb);
		return sessionInDb;
	}

	protected List<SessionChairMan> loadSessionChairMen(List<SessionChairMan> chairMen, Session session) {
		List<SessionChairMan> chairMenInDb = new ArrayList<SessionChairMan>();

		for (SessionChairMan scm : chairMen) {
			scm.setSession(session);
			chairMenInDb.add(loadSessionChairMan(scm));
		}

		return chairMenInDb;
	}

	private SessionChairMan loadSessionChairMan(SessionChairMan sessionChairMan) {
		SessionChairMan chairManInDb = sessionChairManRepository.findBySessionAndPosition(sessionChairMan.getSession(),
				sessionChairMan.getPosition());

		loadPoliticianIfNotExists(sessionChairMan.getPolitician());
		
		if (chairManInDb == null) {
			chairManInDb = sessionChairManRepository.save(sessionChairMan);
		}
		else {
			chairManInDb.setPolitician(sessionChairMan.getPolitician());
			chairManInDb = sessionChairManRepository.save(chairManInDb);
		}
		return chairManInDb;
	}

	protected List<Discussion> loadDiscussions(List<Discussion> discussions, Session session) {
		List<Discussion> discussionsInDb = new ArrayList<Discussion>();

		for (Discussion d : discussions) {
			d.setSession(session);
			discussionsInDb.add(loadDiscussion(d));
		}

		return discussionsInDb;
	}

	private Discussion loadDiscussion(Discussion discussion) {
		Discussion discussionInDb = discussionRepository.findBySessionAndOrder(discussion.getSession(),
				discussion.getOrder());

		if (discussionInDb == null) {
			List<DiscussionSpeech> speeches = discussion.getSpeeches();

			discussion.setSpeeches(new ArrayList<DiscussionSpeech>());

			discussionInDb = discussionRepository.save(discussion);
			discussionInDb.setSpeeches(loadSpeeches(speeches));
		}
		else{
			discussionInDb.setTopic(discussion.getTopic());
			discussionInDb.setType(discussion.getType());

			discussionInDb.setSpeeches(loadSpeeches(discussion.getSpeeches()));
			discussionInDb = discussionRepository.save(discussionInDb);
		}
		return discussionInDb;
	}

	protected List<DiscussionSpeech> loadSpeeches(List<DiscussionSpeech> speeches) {
		List<DiscussionSpeech> speechesInDb = new ArrayList<DiscussionSpeech>();

		for (DiscussionSpeech s : speeches) {
			speechesInDb.add(loadDiscussionSpeech(s));
		}

		return speechesInDb;
	}

	private DiscussionSpeech loadDiscussionSpeech(DiscussionSpeech speech) {
		DiscussionSpeech speechInDb = speechRepository.findByDiscussionAndOrder(speech.getDiscussion(),
				speech.getOrder());

		loadPoliticianIfNotExists(speech.getPolitician());
		
		if (speechInDb == null) {
			speechInDb = speechRepository.save(speech);
		}
		else {
			speechInDb.setText(speech.getText());
			speechInDb.setStartTime(speech.getStartTime());
			speechInDb.setEndTime(speech.getEndTime());
			speechInDb.setType(speech.getType());
			speechInDb = speechRepository.save(speechInDb);
		}
		return speechInDb;
	}

}
