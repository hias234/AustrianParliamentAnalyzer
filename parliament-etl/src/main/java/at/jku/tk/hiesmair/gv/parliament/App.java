package at.jku.tk.hiesmair.gv.parliament;

import javax.inject.Inject;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import at.jku.tk.hiesmair.gv.parliament.db.PoliticianRepository;
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
	
	@Override
	public void run(String... args) throws Exception {
//		periodJob.start(Arrays.asList(25));
		
		System.out.println(politicianRep.countSessionPresences("http://www.parlament.gv.at/WWER/PAD_36450/index.shtml"));
		System.out.println(politicianRep.countSessionAbsences("http://www.parlament.gv.at/WWER/PAD_36450/index.shtml"));
		System.out.println(politicianRep.countSessionPresencesByPeriod("http://www.parlament.gv.at/WWER/PAD_36450/index.shtml", 25));
		System.out.println(politicianRep.countSessionAbsencesByPeriod("http://www.parlament.gv.at/WWER/PAD_36450/index.shtml", 25));
	}
}
