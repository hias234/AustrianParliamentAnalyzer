package at.jku.tk.hiesmair.gv.parliament.etl.politician.loader.db;

import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import at.jku.tk.hiesmair.gv.parliament.db.MandateRepository;
import at.jku.tk.hiesmair.gv.parliament.db.ParliamentClubRepository;
import at.jku.tk.hiesmair.gv.parliament.db.PoliticianRepository;
import at.jku.tk.hiesmair.gv.parliament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parliament.entities.mandate.CouncilMember;
import at.jku.tk.hiesmair.gv.parliament.entities.mandate.Mandate;
import at.jku.tk.hiesmair.gv.parliament.entities.mandate.NationalCouncilMember;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;
import at.jku.tk.hiesmair.gv.parliament.etl.politician.loader.PoliticiansLoader;

public class PoliticiansDatabaseLoader implements PoliticiansLoader {

	@Inject
	private PoliticianRepository politicianRepository;

	@Inject
	private MandateRepository mandateRepository;

	@Inject
	private ParliamentClubRepository clubRepository;

	@Override
	public void loadPoliticians(List<Politician> politicians) {
		for (Politician politician : politicians) {
			loadPolitician(politician);
		}
	}

	protected void loadPolitician(Politician politician) {
		// List<Mandate> curMandates =
		// mandateRepository.findByPolitician(politician);
		// mandateRepository.delete(curMandates);
		//
		// List<Mandate> mandates = politician.getMandates();
		// politician.setMandates(new ArrayList<Mandate>());
		politicianRepository.save(politician);

		// loadMandates(mandates);
		//
		// politician.setMandates(mandates);
		// politicianRepository.save(politician);
	}

	protected void loadMandates(List<Mandate> mandates) {
		for (Mandate mandate : mandates) {
			if (mandate instanceof CouncilMember) {
				clubRepository.save(((CouncilMember) mandate).getClub());
			}

			if (mandate instanceof NationalCouncilMember) {
				((NationalCouncilMember) mandate).setPeriods(new HashSet<LegislativePeriod>());

			}
			mandateRepository.save(mandate);
		}
	}

}
