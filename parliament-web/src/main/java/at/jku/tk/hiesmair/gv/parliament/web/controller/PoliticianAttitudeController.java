package at.jku.tk.hiesmair.gv.parliament.web.controller;

import javax.inject.Inject;
import javax.ws.rs.QueryParam;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import at.jku.tk.hiesmair.gv.parliament.web.dto.graph.D3Graph;
import at.jku.tk.hiesmair.gv.parliament.web.service.PoliticianAttitudeService;

@RestController
@RequestMapping("politician/graph")
public class PoliticianAttitudeController {

	@Inject
	private PoliticianAttitudeService politicianAttitudeService;
	
	@RequestMapping(value = "period/{period}", method = RequestMethod.GET)
	public D3Graph getClubAttitudeGraph(@PathVariable("period") Integer period, @QueryParam("topic") String discussionTopic){
		if (discussionTopic != null && !discussionTopic.isEmpty()){
			return politicianAttitudeService.getPoliticianAttitudeGraph(period, discussionTopic);
		}
		return politicianAttitudeService.getPoliticianAttitudeGraph(period);
	}
	
}
