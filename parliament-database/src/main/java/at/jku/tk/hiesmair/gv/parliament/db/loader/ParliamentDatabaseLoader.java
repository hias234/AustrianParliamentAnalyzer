package at.jku.tk.hiesmair.gv.parliament.db.loader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import at.jku.tk.hiesmair.gv.parliament.db.repositories.DiscussionRepository;
import at.jku.tk.hiesmair.gv.parliament.db.repositories.DiscussionSpeechRepository;
import at.jku.tk.hiesmair.gv.parliament.db.repositories.DiscussionSpeechSentimentRepository;
import at.jku.tk.hiesmair.gv.parliament.db.repositories.LegislativePeriodRepository;
import at.jku.tk.hiesmair.gv.parliament.db.repositories.MandateRepository;
import at.jku.tk.hiesmair.gv.parliament.db.repositories.ParliamentClubRepository;
import at.jku.tk.hiesmair.gv.parliament.db.repositories.PoliticianNameRepository;
import at.jku.tk.hiesmair.gv.parliament.db.repositories.PoliticianRepository;
import at.jku.tk.hiesmair.gv.parliament.db.repositories.SessionChairManRepository;
import at.jku.tk.hiesmair.gv.parliament.db.repositories.SessionRepository;
import at.jku.tk.hiesmair.gv.parliament.db.repositories.relation.PoliticianAttitudeRelationRepository;
import at.jku.tk.hiesmair.gv.parliament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parliament.entities.club.ParliamentClub;
import at.jku.tk.hiesmair.gv.parliament.entities.discussion.Discussion;
import at.jku.tk.hiesmair.gv.parliament.entities.discussion.speech.DiscussionSpeech;
import at.jku.tk.hiesmair.gv.parliament.entities.discussion.speech.SpeechType;
import at.jku.tk.hiesmair.gv.parliament.entities.discussion.speech.sentiment.DiscussionSpeechSentiment;
import at.jku.tk.hiesmair.gv.parliament.entities.mandate.CouncilMember;
import at.jku.tk.hiesmair.gv.parliament.entities.mandate.Mandate;
import at.jku.tk.hiesmair.gv.parliament.entities.mandate.NationalCouncilMember;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.PoliticianName;
import at.jku.tk.hiesmair.gv.parliament.entities.relation.PoliticianAttitudeRelation;
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

	@Inject
	private DiscussionSpeechSentimentRepository sentimentRepository;

	@Inject
	private PoliticianAttitudeRelationRepository politicianRelationRepository;

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
			loadParliamentClub(((CouncilMember) mandate).getClub());
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

	protected void loadParliamentClub(ParliamentClub club) {
		ParliamentClub clubInDb = clubRepository.findOne(club.getShortName());
		if (clubInDb == null){
			clubInDb = club;
		}
		else{
			clubInDb.setLongName(club.getLongName());
		}
		clubRepository.save(clubInDb);
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
			discussionInDb.setSpeeches(loadSpeeches(speeches, discussionInDb));
		}
		else {
			discussionInDb.setTopic(discussion.getTopic());
			discussionInDb.setType(discussion.getType());

			discussionInDb.setSpeeches(loadSpeeches(discussion.getSpeeches(), discussionInDb));
			discussionInDb = discussionRepository.save(discussionInDb);
		}
		return discussionInDb;
	}

	protected List<DiscussionSpeech> loadSpeeches(List<DiscussionSpeech> speeches, Discussion d) {
		List<DiscussionSpeech> speechesInDb = new ArrayList<DiscussionSpeech>();

		for (DiscussionSpeech s : speeches) {
			s.setDiscussion(d);
			speechesInDb.add(loadDiscussionSpeech(s));
		}

		return speechesInDb;
	}

	private DiscussionSpeech loadDiscussionSpeech(DiscussionSpeech speech) {
		DiscussionSpeech speechInDb = speechRepository.findByDiscussionAndOrder(speech.getDiscussion(),
				speech.getOrder());

		loadPoliticianIfNotExists(speech.getPolitician());

		if (speechInDb == null) {
			List<DiscussionSpeechSentiment> sentiments = speech.getSentiments();
			speech.setSentiments(new ArrayList<DiscussionSpeechSentiment>());
			speechInDb = speechRepository.save(speech);
			speechInDb.setSentiments(loadSentiments(sentiments, speechInDb));
		}
		else {
			speechInDb.setText(speech.getText());
			speechInDb.setStartTime(speech.getStartTime());
			speechInDb.setEndTime(speech.getEndTime());
			speechInDb.setType(speech.getType());
			for (DiscussionSpeechSentiment sentiment : speechInDb.getSentiments()) {
				sentimentRepository.delete(sentiment);
			}
			speechInDb.setSentiments(loadSentiments(speech.getSentiments(), speechInDb));
			speechInDb = speechRepository.save(speechInDb);
		}
		return speechInDb;
	}

	private List<DiscussionSpeechSentiment> loadSentiments(List<DiscussionSpeechSentiment> sentiments,
			DiscussionSpeech speech) {
		List<DiscussionSpeechSentiment> sentimentsInDb = new ArrayList<DiscussionSpeechSentiment>();

		if (sentiments != null) {
			for (DiscussionSpeechSentiment s : sentiments) {
				s.setSpeech(speech);
				sentimentsInDb.add(loadSentiment(s));
			}
		}

		return sentimentsInDb;
	}

	private DiscussionSpeechSentiment loadSentiment(DiscussionSpeechSentiment s) {
		return sentimentRepository.save(s);
	}

	public void updatePoliticianAttitudeRelations() {
		politicianRelationRepository.deleteAll();

		for (Discussion d : discussionRepository.findAll()) {
			List<PoliticianAttitudeRelation> speechAttitudeRelations = getSpeechAttitudeRelations(d);
			politicianRelationRepository.save(speechAttitudeRelations);
		}
	}

	private List<PoliticianAttitudeRelation> getSpeechAttitudeRelations(Discussion discussion) {
		List<PoliticianAttitudeRelation> relations = new ArrayList<PoliticianAttitudeRelation>();

		List<PoliticianAttitude> politicianAttitudeList = getPoliticianAttitudesOfDiscussion(discussion);

		for (int i = 0; i < politicianAttitudeList.size(); i++) {
			PoliticianAttitude pa1 = politicianAttitudeList.get(i);
			for (int j = i + 1; j < politicianAttitudeList.size(); j++) {
				PoliticianAttitude pa2 = politicianAttitudeList.get(j);
				Integer weight;
				if (pa1.getSpeechType() == pa2.getSpeechType()) {
					weight = 1;
				}
				else {
					weight = -1;
				}

				relations.add(new PoliticianAttitudeRelation(pa1.getPolitician(), pa2.getPolitician(), discussion,
						weight));
			}
		}

		return relations;
	}

	private List<PoliticianAttitude> getPoliticianAttitudesOfDiscussion(Discussion discussion) {
		List<DiscussionSpeech> speeches = speechRepository.findByDiscussion(discussion);
		Set<PoliticianAttitude> politicianAttitudes = speeches.stream()
				.filter(sp -> sp.getType() == SpeechType.PRO || sp.getType() == SpeechType.CONTRA)
				.map(sp -> new PoliticianAttitude(sp.getPolitician(), sp.getType())).collect(Collectors.toSet());

		politicianAttitudes.removeIf(pa -> politicianAttitudes.stream()
				.filter(paInner -> paInner.getPolitician().equals(pa.getPolitician())).count() > 1);

		List<PoliticianAttitude> politicianAttitudeList = new ArrayList<ParliamentDatabaseLoader.PoliticianAttitude>();
		politicianAttitudeList.addAll(politicianAttitudes);
		politicianAttitudeList.sort((pa1, pa2) -> pa1.politician.getId().compareTo(pa2.politician.getId()));
		return politicianAttitudeList;
	}

	protected static class PoliticianAttitude {
		private Politician politician;
		private SpeechType speechType;

		public PoliticianAttitude(Politician politician, SpeechType speechType) {
			super();
			this.politician = politician;
			this.speechType = speechType;
		}

		public Politician getPolitician() {
			return politician;
		}

		public void setPolitician(Politician politician) {
			this.politician = politician;
		}

		public SpeechType getSpeechType() {
			return speechType;
		}

		public void setSpeechType(SpeechType speechType) {
			this.speechType = speechType;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((politician == null) ? 0 : politician.hashCode());
			result = prime * result + ((speechType == null) ? 0 : speechType.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PoliticianAttitude other = (PoliticianAttitude) obj;
			if (politician == null) {
				if (other.politician != null)
					return false;
			}
			else
				if (!politician.equals(other.politician))
					return false;
			if (speechType != other.speechType)
				return false;
			return true;
		}

	}
}
