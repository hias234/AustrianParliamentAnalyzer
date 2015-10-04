package at.jku.tk.hiesmair.gv.parliament.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import at.jku.tk.hiesmair.gv.parliament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parliament.entities.club.ParliamentClub;
import at.jku.tk.hiesmair.gv.parliament.entities.session.Session;
import at.jku.tk.hiesmair.gv.parliament.web.dto.ClubMandateCountDTO;
import at.jku.tk.hiesmair.gv.parliament.web.dto.ParliamentClubDTO;
import at.jku.tk.hiesmair.gv.parliament.web.dto.period.LegislativePeriodStatisticDataDTO;
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

	@RequestMapping(value = "stats/list", method = RequestMethod.GET)
	public List<LegislativePeriodStatisticDataDTO> getStatList() {
		List<LegislativePeriodStatisticDataDTO> listWithData = new ArrayList<LegislativePeriodStatisticDataDTO>();

		for (int i = periodService.getLatestPeriod(); i >= 20; i--) {
			listWithData.add(getStatData(i));
		}

		return listWithData;
	}

	@RequestMapping(value = "stats/{period}", method = RequestMethod.GET)
	protected LegislativePeriodStatisticDataDTO getStatData(@PathVariable("period") Integer period) {
		LegislativePeriod legislativePeriod = periodService.findOne(period);

		Double absencePercentage = periodService.getAbsencePercentage(legislativePeriod.getPeriod());
		Integer sessionCount = legislativePeriod.getSessions().size();

		LegislativePeriodStatisticDataDTO statData = new LegislativePeriodStatisticDataDTO(
				legislativePeriod.getPeriod(), absencePercentage, sessionCount);

		Optional<Session> firstSession = legislativePeriod.getSessions().stream()
				.filter(lp -> lp.getSessionNr().equals(1)).findFirst();

		if (firstSession.isPresent()) {
			Map<ParliamentClub, Long> mandateCount = legislativePeriod.getMandateCountByClubAtDate(firstSession.get()
					.getStartDate());

			List<ClubMandateCountDTO> clubMandateCounts = mandateCount
					.entrySet()
					.stream()
					.map(entry -> new ClubMandateCountDTO(ParliamentClubDTO.fromParliamentClub(entry.getKey(),
							modelMapper), entry.getValue().intValue()))
					.sorted((cmc1, cmc2) -> -cmc1.getMandateCount().compareTo(cmc2.getMandateCount()))
					.collect(Collectors.toList());

			statData.setNationalCouncilMemberCount(clubMandateCounts);
		}

		return statData;
	}

	@RequestMapping(value = "stats/latest", method = RequestMethod.GET)
	public LegislativePeriodStatisticDataDTO getStatDataOfLatestPeriod() {
		Integer latestPeriod = periodService.getLatestPeriod();

		return getStatData(latestPeriod);
	}
}
