package at.jku.tk.hiesmair.gv.parliament.web.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import at.jku.tk.hiesmair.gv.parliament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parliament.entities.mandate.NationalCouncilMember;
import at.jku.tk.hiesmair.gv.parliament.entities.session.Session;
import at.jku.tk.hiesmair.gv.parliament.web.dto.LegislativePeriodStatisticDataDTO;
import at.jku.tk.hiesmair.gv.parliament.web.dto.ParliamentClubDTO;
import at.jku.tk.hiesmair.gv.parliament.web.service.LegislativePeriodService;

@RestController
@RequestMapping("period")
public class LegislativePeriodController {

	@Inject
	private LegislativePeriodService periodService;

	@Inject
	private ModelMapper modelMapper;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public List<Integer> findAll() {
		return StreamSupport.stream(periodService.findAll().spliterator(), false).map(p -> p.getPeriod())
				.collect(Collectors.toList());
	}

	@RequestMapping(value = "stats/latest", method = RequestMethod.GET)
	public LegislativePeriodStatisticDataDTO getStatDataOfLatestPeriod() {
		LegislativePeriod latestPeriod = periodService.findOne(periodService.getLatestPeriod());

		Double absencePercentage = periodService.getAbsencePercentage(latestPeriod.getPeriod());
		Integer sessionCount = latestPeriod.getSessions().size();

		LegislativePeriodStatisticDataDTO statData = new LegislativePeriodStatisticDataDTO(latestPeriod.getPeriod(),
				absencePercentage, sessionCount);

		if (sessionCount > 0) {
			Session firstSession = latestPeriod.getSessions().get(0);
			Set<NationalCouncilMember> ncms = latestPeriod.getNationalCouncilMembersAt(firstSession.getStartDate());

			Map<ParliamentClubDTO, Long> mandateCount = ncms.stream().collect(
					Collectors.groupingBy(ncm -> ParliamentClubDTO.fromParliamentClub(ncm.getClub(), modelMapper),
							Collectors.counting()));
			
			statData.setNationalCouncilMemberCount(mandateCount);
		}

		return statData;
	}
}
