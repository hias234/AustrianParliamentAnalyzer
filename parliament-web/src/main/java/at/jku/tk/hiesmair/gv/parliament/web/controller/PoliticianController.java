package at.jku.tk.hiesmair.gv.parliament.web.controller;

import java.util.List;

import javax.inject.Inject;

import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import at.jku.tk.hiesmair.gv.parliament.db.repositories.PoliticianRepository;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;
import at.jku.tk.hiesmair.gv.parliament.graph.gephi.GephiGraphConstructor;
import at.jku.tk.hiesmair.gv.parliament.web.dto.PoliticianDTO;
import at.jku.tk.hiesmair.gv.parliament.web.service.PoliticianService;

@RestController
@RequestMapping("politician/")
public class PoliticianController {

	@Inject
	private PoliticianService politicianService;
	
	@Inject
	private ModelMapper mapper;
	
	@RequestMapping(value = "period/{period}", method = RequestMethod.GET)
	public List<PoliticianDTO> findPolticiansByPeriod(@PathVariable("period") Integer period){
		return PoliticianDTO.fromPoliticians(mapper, politicianService.findNationalCouncilMembersOfPeriod(period));
	}
	
	@RequestMapping(value = "test", method = RequestMethod.GET, produces = "image/svg+xml")
	public String test(){
		return new GephiGraphConstructor().doWork();
	}
}
