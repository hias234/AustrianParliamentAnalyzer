package at.jku.tk.hiesmair.gv.parliament.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.QueryParam;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import at.jku.tk.hiesmair.gv.parliament.db.result.PoliticianActivityResult;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;
import at.jku.tk.hiesmair.gv.parliament.entities.relation.PoliticianAttitudeRelation;
import at.jku.tk.hiesmair.gv.parliament.web.dto.PoliticianRelationDTO;
import at.jku.tk.hiesmair.gv.parliament.web.dto.absence.AbsenceDTO;
import at.jku.tk.hiesmair.gv.parliament.web.dto.activity.PoliticianActivityDTO;
import at.jku.tk.hiesmair.gv.parliament.web.dto.politician.PoliticianDTO;
import at.jku.tk.hiesmair.gv.parliament.web.dto.politician.PoliticianSummaryStatsItemDTO;
import at.jku.tk.hiesmair.gv.parliament.web.service.PoliticianAttitudeService;
import at.jku.tk.hiesmair.gv.parliament.web.service.PoliticianService;

@RestController
@RequestMapping("politician/")
public class PoliticianController {

	@Inject
	private PoliticianService politicianService;

	@Inject
	private PoliticianAttitudeService politicianAttitudeService;
	
	@Inject
	private ModelMapper mapper;

	@RequestMapping(value = "period/{period}", method = RequestMethod.GET)
	public List<PoliticianDTO> findPolticiansByPeriod(@PathVariable("period") Integer period) {
		return PoliticianDTO.fromPoliticians(mapper, politicianService.findNationalCouncilMembersOfPeriod(period));
	}
	
	@RequestMapping(value = "most_related", method = RequestMethod.GET)
	public List<PoliticianRelationDTO> findMostRelatedPoliticians(@QueryParam("politicianId") String politicianId) {
		Page<PoliticianAttitudeRelation> mostRelatedPoliticians = politicianAttitudeService.getMostRelatedPoliticians(politicianId, 1, 10);
		return getPoliticianRelationDTOs(politicianId, mostRelatedPoliticians);
	}
	
	@RequestMapping(value = "least_related", method = RequestMethod.GET)
	public List<PoliticianRelationDTO> findLeastRelatedPoliticians(@QueryParam("politicianId") String politicianId) {
		Page<PoliticianAttitudeRelation> leastRelatedPoliticians = politicianAttitudeService.getLeastRelatedPoliticians(politicianId, 1, 10);
		return getPoliticianRelationDTOs(politicianId, leastRelatedPoliticians);
	}

	protected List<PoliticianRelationDTO> getPoliticianRelationDTOs(String politicianId,
			Page<PoliticianAttitudeRelation> mostRelatedPoliticians) {
		List<PoliticianRelationDTO> result = new ArrayList<PoliticianRelationDTO>();
		
		for (PoliticianAttitudeRelation relation : mostRelatedPoliticians){
			Politician p1, p2;
			if (relation.getPolitician1().getId().equals(politicianId)){
				p1 = relation.getPolitician1();
				p2 = relation.getPolitician2();
			}
			else{
				p2 = relation.getPolitician1();
				p1 = relation.getPolitician2();
			}
			
			result.add(new PoliticianRelationDTO(PoliticianDTO.fromPolitician(mapper, p1), PoliticianDTO.fromPolitician(mapper, p2), relation.getWeight()));
		}
		
		return result;
	}

	@RequestMapping(value = "test", method = RequestMethod.GET)
	public PoliticianDTO test() {
		return PoliticianDTO.fromPoliticians(mapper, politicianService.findNationalCouncilMembersOfPeriod(25)).get(0);
	}

	@RequestMapping(value = "activity/{period}", method = RequestMethod.GET)
	public List<PoliticianActivityDTO> getPoliticianActivity(@PathVariable("period") Integer period) {
		List<PoliticianActivityResult> activity = politicianService.getPoliticianActivityResult(period);

		return getPoliticianActivityDTO(activity);
	}

	private List<PoliticianActivityDTO> getPoliticianActivityDTO(List<PoliticianActivityResult> activity) {
		List<PoliticianActivityDTO> result = new ArrayList<PoliticianActivityDTO>(activity.size());

		for (PoliticianActivityResult activityResult : activity) {
			result.add(mapper.map(activityResult, PoliticianActivityDTO.class));
		}

		return result;
	}

	@RequestMapping(value = "stats/period/{period}", method = RequestMethod.GET)
	public List<PoliticianSummaryStatsItemDTO> getPoliticianStatsOfPeriod(@PathVariable("period") Integer period) {
		List<Politician> politicians = politicianService.findNationalCouncilMembersOfPeriod(period);
		List<PoliticianSummaryStatsItemDTO> result = new ArrayList<PoliticianSummaryStatsItemDTO>(politicians.size());

		for (Politician politician : politicians) {
			Integer speechCount = politicianService.getSpeechCount(politician.getId(), period);
			Long absenceCount = politicianService.getAbsenceCount(politician.getId(), period);
			Long presenceCount = politicianService.getPresenceCount(politician.getId(), period);

			result.add(new PoliticianSummaryStatsItemDTO(PoliticianDTO.fromPolitician(mapper, politician),
					new AbsenceDTO(absenceCount, presenceCount), speechCount));
		}

		return result;
	}

	@RequestMapping(value = "stats", method = RequestMethod.GET)
	public PoliticianSummaryStatsItemDTO getPoliticianStats(@QueryParam("politicianId") String politicianId) {
		Integer speechCount = politicianService.getSpeechCount(politicianId);
		Long absenceCount = politicianService.getAbsenceCount(politicianId);
		Long presenceCount = politicianService.getPresenceCount(politicianId);
		
		return new PoliticianSummaryStatsItemDTO(PoliticianDTO.fromPolitician(mapper, politicianService.findById(politicianId)),
				new AbsenceDTO(absenceCount, presenceCount), speechCount);
	}
	
}
