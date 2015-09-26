package at.jku.tk.hiesmair.gv.parliament;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import at.jku.tk.hiesmair.gv.parliament.db.loader.ParliamentDatabaseLoader;
import at.jku.tk.hiesmair.gv.parliament.db.repositories.ParliamentClubRepository;
import at.jku.tk.hiesmair.gv.parliament.db.repositories.PoliticianRepository;
import at.jku.tk.hiesmair.gv.parliament.db.repositories.relation.PoliticianAttitudeRelationRepository;
import at.jku.tk.hiesmair.gv.parliament.db.result.AbsenceResult;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;
import at.jku.tk.hiesmair.gv.parliament.entities.relation.ClubAttitudeRelationByPeriod;
import at.jku.tk.hiesmair.gv.parliament.entities.relation.PoliticianAttitudeRelationByPeriod;
import at.jku.tk.hiesmair.gv.parliament.etl.period.LegislativePeriodEtlJob;
import at.jku.tk.hiesmair.gv.parliament.etl.politician.PoliticiansEtlJob;

/**
 * Main Application kicking off the analysis process
 */
@SpringBootApplication
public class App implements CommandLineRunner {

	/**
	 * download everything and analyse everything
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		SpringApplication.run(App.class, args);
	}

	@Inject
	private LegislativePeriodEtlJob periodJob;
	
	@Inject
	private PoliticiansEtlJob politiciansJob;
	
	@Inject
	private PoliticianRepository politicianRep;
	
	@Inject
	private ParliamentClubRepository clubRep;

	@Inject
	private ParliamentDatabaseLoader databaseLoader;
	
	@Inject
	private PoliticianAttitudeRelationRepository paRep;
	
	@Override
	public void run(String... args) throws Exception {
		periodJob.start(Arrays.asList(20,21,22,23,24,25));

//		databaseLoader.updatePoliticianAttitudeRelations();
		
		List<PoliticianAttitudeRelationByPeriod> result = paRep.getPoliticianAttitudesByPeriod(24);
		System.out.println(result.size());
		
		List<ClubAttitudeRelationByPeriod> clubResult = paRep.getClubAttitudesByPeriod(24);
		System.out.println(clubResult.size());
		
		System.out.println(politicianRep.countSessionPresencesOfPolitician("http://www.parlament.gv.at/WWER/PAD_36450/index.shtml"));
		System.out.println(politicianRep.countSessionAbsencesOfPolitician("http://www.parlament.gv.at/WWER/PAD_36450/index.shtml"));
		System.out.println(politicianRep.countSessionPresencesOfPoliticianByPeriod("http://www.parlament.gv.at/WWER/PAD_36450/index.shtml", 25));
		System.out.println(politicianRep.countSessionAbsencesOfPoliticianByPeriod("http://www.parlament.gv.at/WWER/PAD_36450/index.shtml", 25));
		System.out.println();
		
		System.out.println(clubRep.countSessionAbsencesOfClub("ÖVP"));
		System.out.println(clubRep.countSessionAbsencesOfClubByPeriod("ÖVP", 25));
		System.out.println(clubRep.countSessionPresencesOfClub("ÖVP"));
		System.out.println(clubRep.countSessionPresencesOfClubByPeriod("ÖVP", 25));
		System.out.println();
		
		System.out.println(politicianRep.countSpeechesOfPoliticianByPeriod("http://www.parlament.gv.at/WWER/PAD_36450/index.shtml", 25));
		System.out.println(politicianRep.countSpeechesOfPolitician("http://www.parlament.gv.at/WWER/PAD_36450/index.shtml"));
		
		List<Politician> politiciansOfPeriod = politicianRep.findNationalCouncilMembersOfPeriod(25);
		System.out.println(politiciansOfPeriod.size());
		
		List<AbsenceResult> absences = politicianRep.countSessionAbsences();
		absences.forEach(a -> System.out.println(a.getId() + " " + a.getAbsenceCount() + " " + a.getPresenceCount()));
		
		absences = clubRep.countSessionAbsencesPerClub();
		absences.forEach(a -> System.out.println(a.getId() + " " + a.getAbsenceCount() + " " + a.getPresenceCount()));
		
		absences = politicianRep.countSessionAbsencesByPeriod(25,25);
		absences.forEach(a -> System.out.println(a.getId() + " " + a.getAbsenceCount() + " " + a.getPresenceCount()));

		absences = clubRep.countSessionAbsencesPerClubByPeriod(25,25);
		absences.forEach(a -> System.out.println(a.getId() + " " + a.getAbsenceCount() + " " + a.getPresenceCount()));
	}
}
