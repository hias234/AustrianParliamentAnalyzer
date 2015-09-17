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
import at.jku.tk.hiesmair.gv.parliament.entities.session.Session;
import at.jku.tk.hiesmair.gv.parliament.entities.session.SessionChairMan;

@Component
public class ParliamentDatabaseLoader {

	@Inject
	private PoliticianRepository politicianRepository;

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

	public void loadPolitician(Politician politician) {
		Set<Mandate> mandates = politician.getMandates();

		politician.setMandates(new HashSet<Mandate>());

		Politician politicianInDb = politicianRepository.findOne(politician.getId());
		if (politicianInDb == null) {
			politicianRepository.save(politician);
		}
		mandates.forEach(m -> loadMandate(m));

		politician.setMandates(mandates);
	}

	public void loadPoliticianIfNotExists(Politician politician) {
		if (!politicianRepository.exists(politician.getId())) {
			loadPolitician(politician);
		}
	}

	protected Mandate loadMandate(Mandate mandate) {
		Mandate mandateInDb = mandateRepository.findByPoliticianAndDescriptionAndValidFrom(mandate.getPolitician(),
				mandate.getDescription(), mandate.getValidFrom());

		if (mandateInDb == null){
			loadPoliticianIfNotExists(mandate.getPolitician());
	
			if (mandate instanceof CouncilMember) {
				clubRepository.save(((CouncilMember) mandate).getClub());
			}
	
			if (mandate instanceof NationalCouncilMember) {
				Set<LegislativePeriod> periods = ((NationalCouncilMember) mandate).getPeriods();
	
				periods.forEach(p -> loadPeriod(p, false));
			}
			mandateInDb = mandateRepository.save(mandate);
		}
		
		return mandateInDb;
	}
	
	public Set<NationalCouncilMember> loadNationalCouncilMembers(Set<NationalCouncilMember> ncms){
		Set<NationalCouncilMember> ncmsInDb = new HashSet<NationalCouncilMember>();
		
		for (NationalCouncilMember ncm : ncms){
			ncmsInDb.add((NationalCouncilMember) loadMandate(ncm));
		}
		
		return ncmsInDb;
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
		
		return periodInDb;
	}

	protected List<Session> loadSessions(List<Session> sessions, LegislativePeriod period){
		List<Session> sessionsInDb = new ArrayList<Session>();
		
		for (Session s : sessions){
			s.setPeriod(period);
			sessionsInDb.add(loadSession(s));
		}
		
		return sessionsInDb;
	}
	
	private Session loadSession(Session session) {
		Session sessionInDb = sessionRepository.findByPeriodAndSessionNr(session.getPeriod(), session.getSessionNr());

		if (sessionInDb == null) {
			List<SessionChairMan> chairMen = session.getChairMen();
			List<Discussion> discussions = session.getDiscussions();
			Set<NationalCouncilMember> presentNationalCouncilMembers = session.getPresentNationalCouncilMembers();
			Set<NationalCouncilMember> absentNationalCouncilMembers = session.getAbsentNationalCouncilMembers();

			session.setChairMen(new ArrayList<SessionChairMan>());
			session.setDiscussions(new ArrayList<Discussion>());
			session.setPresentNationalCouncilMembers(new HashSet<NationalCouncilMember>());
			session.setAbsentNationalCouncilMembers(new HashSet<NationalCouncilMember>());

			sessionInDb = sessionRepository.save(session);
			chairMen = loadSessionChairMen(chairMen);
			discussions = loadDiscussions(discussions);
			presentNationalCouncilMembers = loadNationalCouncilMembers(presentNationalCouncilMembers);
			absentNationalCouncilMembers = loadNationalCouncilMembers(absentNationalCouncilMembers);

			sessionInDb.setChairMen(chairMen);
			sessionInDb.setDiscussions(discussions);
			sessionInDb.setPresentNationalCouncilMembers(presentNationalCouncilMembers);
			sessionInDb.setAbsentNationalCouncilMembers(absentNationalCouncilMembers);

			// to save the councilmember-associations
			sessionInDb = sessionRepository.save(sessionInDb);
		}
		return sessionInDb;
	}

	protected List<SessionChairMan> loadSessionChairMen(List<SessionChairMan> chairMen){
		List<SessionChairMan> chairMenInDb = new ArrayList<SessionChairMan>();
		
		for (SessionChairMan scm : chairMen){
			chairMenInDb.add(loadSessionChairMan(scm));
		}
		
		return chairMenInDb;
	}
	
	private SessionChairMan loadSessionChairMan(SessionChairMan sessionChairMan) {
		SessionChairMan chairManInDb = sessionChairManRepository.findBySessionAndPosition(sessionChairMan.getSession(),
				sessionChairMan.getPosition());

		if (chairManInDb == null) {
			loadPoliticianIfNotExists(sessionChairMan.getPolitician());

			chairManInDb = sessionChairManRepository.save(sessionChairMan);
		}
		return chairManInDb;
	}

	protected List<Discussion> loadDiscussions(List<Discussion> discussions){
		List<Discussion> discussionsInDb = new ArrayList<Discussion>();
		
		for (Discussion d : discussions){
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
		return discussionInDb;
	}

	protected List<DiscussionSpeech> loadSpeeches(List<DiscussionSpeech> speeches){
		List<DiscussionSpeech> speechesInDb = new ArrayList<DiscussionSpeech>();
		
		for (DiscussionSpeech s : speeches){
			speechesInDb.add(loadDiscussionSpeech(s));
		}
		
		return speechesInDb;
	}
	
	private DiscussionSpeech loadDiscussionSpeech(DiscussionSpeech speech) {
		DiscussionSpeech speechInDb = speechRepository.findByDiscussionAndOrder(speech.getDiscussion(),
				speech.getOrder());

		if (speechInDb == null) {
			loadPoliticianIfNotExists(speech.getPolitician());

			speechInDb = speechRepository.save(speech);
		}
		return speechInDb;
	}

}
