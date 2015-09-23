package at.jku.tk.hiesmair.gv.parliament.web.controller;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import at.jku.tk.hiesmair.gv.parliament.web.dto.graph.D3Graph;
import at.jku.tk.hiesmair.gv.parliament.web.service.ClubAttitudeService;

@RestController
@RequestMapping("club/graph/")
public class ClubAttitudeController {

	@Inject
	private ClubAttitudeService clubAttitudeService;
	
	@RequestMapping(value = "period/{period}", method = RequestMethod.GET)
	public D3Graph getClubAttitudeGraph(@PathVariable("period") Integer period){
		return clubAttitudeService.getClubAttitudeGraph(period);
	}
}
