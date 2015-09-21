package at.jku.tk.hiesmair.gv.parliament.web.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import at.jku.tk.hiesmair.gv.parliament.db.PoliticianRepository;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

@Service
public class PoliticianService {

	@Inject
	private PoliticianRepository politicianRep;
	
	public List<Politician> findNationalCouncilMembersOfPeriod(Integer period) {
		return politicianRep.findNationalCouncilMembersOfPeriod(period);
	}
}
