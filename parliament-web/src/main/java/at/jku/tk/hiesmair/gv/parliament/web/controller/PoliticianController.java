package at.jku.tk.hiesmair.gv.parliament.web.controller;

import java.util.List;

import javax.inject.Inject;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import at.jku.tk.hiesmair.gv.parliament.db.PoliticianRepository;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;
import at.jku.tk.hiesmair.gv.parliament.web.dto.PoliticianDTO;

@RestController
@RequestMapping("politician/")
public class PoliticianController {

	@Inject
	private PoliticianRepository politicianRep;
	
	@Inject
	private ModelMapper mapper;
	
	@RequestMapping(value = "period/{period}", method = RequestMethod.GET)
	public List<PoliticianDTO> findPolticiansByPeriod(@PathVariable("period") Integer period){
		return PoliticianDTO.fromPoliticians(mapper, politicianRep.findNationalCouncilMembersOfPeriod(period));
	}
	
	@RequestMapping(value = "test", method = RequestMethod.GET)
	public PoliticianDTO test(){
		return PoliticianDTO.fromPoliticians(mapper, politicianRep.findNationalCouncilMembersOfPeriod(25)).get(0);
	}
}
