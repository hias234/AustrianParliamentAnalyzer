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
import at.jku.tk.hiesmair.gv.parliament.entities.mandate.Mandate.MandateId;
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
		Politician politicianInDb = politicianRepository.findOne(politician.getId());
		if (politicianInDb == null) {
			Set<Mandate> mandates = politician.getMandates();

			politician.setMandates(new HashSet<Mandate>());

			politicianRepository.save(politician);
			mandates.forEach(m -> loadMandate(m));

			politician.setMandates(mandates);
		}
	}

	protected void loadMandate(Mandate mandate) {
		loadPolitician(mandate.getPolitician());

		if (mandate instanceof CouncilMember) {
			clubRepository.save(((CouncilMember) mandate).getClub());
		}

		if (mandate instanceof NationalCouncilMember) {
			Set<LegislativePeriod> periods = ((NationalCouncilMember) mandate).getPeriods();

			periods.forEach(p -> loadPeriod(p));
			mandateRepository.save(mandate);
		}
		else {
			mandateRepository.save(mandate);
		}
	}

	public void loadPeriod(LegislativePeriod period) {
		LegislativePeriod periodInDb = periodRepository.findOne(period.getPeriod());

		if (periodInDb == null) {
			List<Session> sessions = period.getSessions();
			Set<NationalCouncilMember> nationalCouncilMembers = period.getNationalCouncilMembers();

			period.setSessions(new ArrayList<Session>());
			period.setNationalCouncilMembers(new HashSet<NationalCouncilMember>());

			periodRepository.save(period);
			sessions.forEach(s -> loadSession(s));
			// TODO check if nationalcouncilmembers are set on the other side
			// (should be)

			period.setSessions(sessions);
			period.setNationalCouncilMembers(nationalCouncilMembers);
		}
	}

	private void loadSession(Session session) {
		List<SessionChairMan> chairMen = session.getChairMen();
		List<Discussion> discussions = session.getDiscussions();
		Set<NationalCouncilMember> presentNationalCouncilMembers = session.getPresentNationalCouncilMembers();
		Set<NationalCouncilMember> absentNationalCouncilMembers = session.getAbsentNationalCouncilMembers();

		session.setChairMen(new ArrayList<SessionChairMan>());
		session.setDiscussions(new ArrayList<Discussion>());
		session.setPresentNationalCouncilMembers(new HashSet<NationalCouncilMember>());
		session.setAbsentNationalCouncilMembers(new HashSet<NationalCouncilMember>());

		sessionRepository.save(session);
		chairMen.forEach(cm -> loadSessionChairMan(cm));
		discussions.forEach(d -> loadDiscussion(d));
		presentNationalCouncilMembers.forEach(ncm -> loadMandate(ncm));
		absentNationalCouncilMembers.forEach(ncm -> loadMandate(ncm));

		session.setChairMen(chairMen);
		session.setDiscussions(discussions);
		session.setPresentNationalCouncilMembers(presentNationalCouncilMembers);
		session.setAbsentNationalCouncilMembers(absentNationalCouncilMembers);

		// to save the councilmember-associations
		sessionRepository.save(session);
	}

	private void loadSessionChairMan(SessionChairMan sessionChairMan) {
		Politician politician = sessionChairMan.getPolitician();
		loadPolitician(politician);

		sessionChairManRepository.save(sessionChairMan);
	}

	private void loadDiscussion(Discussion discussion) {
		List<DiscussionSpeech> speeches = discussion.getSpeeches();

		discussion.setSpeeches(new ArrayList<DiscussionSpeech>());

		discussionRepository.save(discussion);
		speeches.forEach(s -> loadDiscussionSpeech(s));

		discussion.setSpeeches(speeches);
	}

	private void loadDiscussionSpeech(DiscussionSpeech speech) {
		loadPolitician(speech.getPolitician());

		speechRepository.save(speech);
	}

}
