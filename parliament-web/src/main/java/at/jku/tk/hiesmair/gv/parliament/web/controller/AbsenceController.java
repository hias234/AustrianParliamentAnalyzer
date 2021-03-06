package at.jku.tk.hiesmair.gv.parliament.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import at.jku.tk.hiesmair.gv.parliament.db.result.ClubAbsenceResult;
import at.jku.tk.hiesmair.gv.parliament.db.result.PoliticianAbsenceResult;
import at.jku.tk.hiesmair.gv.parliament.web.dto.absence.ClubAbsenceDTO;
import at.jku.tk.hiesmair.gv.parliament.web.dto.absence.PoliticianAbsenceDTO;
import at.jku.tk.hiesmair.gv.parliament.web.service.AbsenceService;

@RestController
@RequestMapping("absence")
public class AbsenceController {

	@Inject
	private AbsenceService absenceService;
	
	@Inject
	private ModelMapper modelMapper;
	
	@RequestMapping(value = "club/byPeriod/{period}", method = RequestMethod.GET)
	public List<ClubAbsenceDTO> getClubAbsenceByPeriod(@PathVariable("period") Integer period){
		List<ClubAbsenceResult> clubAbsences = absenceService.getClubAbsence(period);
		
		return getClubAbsenceDTOList(clubAbsences);
	}

	private List<ClubAbsenceDTO> getClubAbsenceDTOList(List<ClubAbsenceResult> clubAbsences) {
		List<ClubAbsenceDTO> result = new ArrayList<ClubAbsenceDTO>(clubAbsences.size());
		
		for (ClubAbsenceResult absenceResult : clubAbsences){
			result.add(modelMapper.map(absenceResult, ClubAbsenceDTO.class));
		}
		
		return result;
	}
	
	@RequestMapping(value = "politician/byPeriod/{period}", method = RequestMethod.GET)
	public List<PoliticianAbsenceDTO> getPoliticianAbsenceByPeriod(@PathVariable("period") Integer period) {
		List<PoliticianAbsenceResult> politicianAbsences = absenceService.getPoliticianAbsence(period);
		
		return getPoliticianAbsenceDTOList(politicianAbsences);
	}

	private List<PoliticianAbsenceDTO> getPoliticianAbsenceDTOList(List<PoliticianAbsenceResult> politicianAbsences) {
		List<PoliticianAbsenceDTO> result = new ArrayList<PoliticianAbsenceDTO>(politicianAbsences.size());
		
		for (PoliticianAbsenceResult absenceResult : politicianAbsences){
			result.add(modelMapper.map(absenceResult, PoliticianAbsenceDTO.class));
		}
		
		return result;
	}
}
