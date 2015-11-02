package at.jku.tk.hiesmair.gv.parliament.web.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import at.jku.tk.hiesmair.gv.parliament.db.repositories.PoliticianRepository;
import at.jku.tk.hiesmair.gv.parliament.db.result.PoliticianActivityResult;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

@Service
public class PoliticianService {

	@Inject
	private PoliticianRepository politicianRep;

	public List<Politician> findNationalCouncilMembersOfPeriod(Integer period) {
		return politicianRep.findNationalCouncilMembersOfPeriod(period);
	}

	public List<PoliticianActivityResult> getPoliticianActivityResult(Integer period) {
		return politicianRep.getActivityResult(period, period);
	}

	public Integer getSpeechCount(String politicianId, Integer period) {
		return Long.valueOf(politicianRep.countSpeechesOfPoliticianByPeriod(politicianId, period)).intValue();
	}
	
	public Integer getSpeechCount(String politicianId) {
		return Long.valueOf(politicianRep.countSpeechesOfPolitician(politicianId)).intValue();
	}
	
	public Long getAbsenceCount(String politicianId, Integer period) {
		return politicianRep.countSessionAbsencesOfPoliticianByPeriod(politicianId, period);
	}
	
	public Long getAbsenceCount(String politicianId) {
		return politicianRep.countSessionAbsencesOfPolitician(politicianId);
	}
	
	public Long getPresenceCount(String politicianId, Integer period) {
		return politicianRep.countSessionPresencesOfPoliticianByPeriod(politicianId, period);
	}
	
	public Long getPresenceCount(String politicianId) {
		return politicianRep.countSessionPresencesOfPolitician(politicianId);
	}
	
	public Politician findById(String politicianId) {
		return politicianRep.findOne(politicianId);
	}
}
