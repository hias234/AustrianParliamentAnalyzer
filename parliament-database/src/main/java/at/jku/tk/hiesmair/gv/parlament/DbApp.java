package at.jku.tk.hiesmair.gv.parlament;


//@SpringBootApplication
public class DbApp {

//	public static void main(String[] args) {
//		SpringApplication.run(DbApp.class, args);
//	}
//
//	@Inject
//	private ParliamentClubRepository clubRepository;
//	
//	@Inject
//	private PoliticianRepository politicianRepository;
//	
//	@Inject
//	private LegislativePeriodRepository periodRepository;
//	
//	@Inject
//	private SessionRepository sessionRepository;
//	
//	@Inject
//	private DiscussionRepository discussionRepository;
//	
//	@Inject
//	private DiscussionSpeechRepository speechRepository;
//	
//	@Inject
//	private MandateRepository mandateRepository;
//	
//	@Override
//	public void run(String... arg0) throws Exception {
//		ParliamentClub club = new ParliamentClub();
//		club.setShortName("asdf");
//		club.setLongName("asdfasdf");
//		
//		clubRepository.save(club);
//		
//		Politician p = new Politician();
//		p.setId("asdf");
//		p.setBirthDate(new Date());
//		p.setFirstName("kkkk");
//		p.setSurName("eiie");
//		p.setTitle(null);
//		
//		politicianRepository.save(p);
//		
//		NationalCouncilPresident pres = new NationalCouncilPresident();
//		pres.setDescription("asdf");
//		pres.setPolitician(p);
//		pres.setPosition(2);
//		pres.setValidFrom(new Date());
//		pres.setValidUntil(new Date());
//		
//		mandateRepository.save(pres);
//		
//		p.setMandates(Arrays.asList(pres));
//		politicianRepository.save(p);
//		
//		LegislativePeriod period = new LegislativePeriod();
//		period.setPeriod(1);
//		
//		periodRepository.save(period);
//		
//		Session session = new Session();
//		session.setPeriod(period);
//		session.setSessionNr(1);
//		session.setStartDate(new Date());
//		session.setEndDate(new Date());
//		session.setPoliticians(Arrays.asList(p));
//		period.setSessions(Arrays.asList(session));
//		sessionRepository.save(session);
//		
//		periodRepository.save(period);
//		
//		Discussion d = new Discussion();
//		d.setOrder(1);
//		d.setTopic("asdf");
//		d.setType(null);
//		d.setSession(session);
//		discussionRepository.save(d);
//		session.setDiscussions(Arrays.asList(d));
//		
//		DiscussionSpeech speech = new DiscussionSpeech();
//		speech.setDiscussion(d);
//		speech.setPolitician(p);
//		speech.setEndTime(new Date());
//		speech.setType(SpeechType.ACTUAL_CORRECTION);
//		
//		speechRepository.save(speech);
//		d.setSpeeches(Arrays.asList(speech));
//		
//		System.out.println(clubRepository.findAll());
//		System.out.println(politicianRepository.findOne("asdf"));
//		System.out.println(periodRepository.findAll());
//		
//		System.out.println(sessionRepository.findAll());
//	}
	
}
