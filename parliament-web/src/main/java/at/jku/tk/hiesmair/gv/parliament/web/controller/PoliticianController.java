package at.jku.tk.hiesmair.gv.parliament.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import at.jku.tk.hiesmair.gv.parliament.db.result.PoliticianActivityResult;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;
import at.jku.tk.hiesmair.gv.parliament.web.dto.absence.AbsenceDTO;
import at.jku.tk.hiesmair.gv.parliament.web.dto.activity.PoliticianActivityDTO;
import at.jku.tk.hiesmair.gv.parliament.web.dto.politician.PoliticianDTO;
import at.jku.tk.hiesmair.gv.parliament.web.dto.politician.PoliticianSummaryStatsItemDTO;
import at.jku.tk.hiesmair.gv.parliament.web.service.PoliticianService;

@RestController
@RequestMapping("politician/")
public class PoliticianController {

	@Inject
	private PoliticianService politicianService;

	@Inject
	private ModelMapper mapper;

	@RequestMapping(value = "period/{period}", method = RequestMethod.GET)
	public List<PoliticianDTO> findPolticiansByPeriod(@PathVariable("period") Integer period) {
		return PoliticianDTO.fromPoliticians(mapper, politicianService.findNationalCouncilMembersOfPeriod(period));
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

}
