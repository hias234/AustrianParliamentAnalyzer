package at.jku.tk.hiesmair.gv.parliament.web.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import at.jku.tk.hiesmair.gv.parliament.db.PoliticianRepository;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

@RestController
@RequestMapping("politician/")
public class PoliticianController {

	@Inject
	private PoliticianRepository politicianRep;
	
	@RequestMapping(value = "period/{period}", method = RequestMethod.GET)
	public List<Politician> findPolticiansByPeriod(@PathVariable("period") Integer period){
		return politicianRep.findNationalCouncilMembersOfPeriod(period);
	}
	
}
