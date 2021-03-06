package at.jku.tk.hiesmair.gv.parliament.web.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import at.jku.tk.hiesmair.gv.parliament.db.repositories.ParliamentClubRepository;
import at.jku.tk.hiesmair.gv.parliament.db.repositories.PoliticianRepository;
import at.jku.tk.hiesmair.gv.parliament.db.result.AbsenceResult;
import at.jku.tk.hiesmair.gv.parliament.db.result.ClubAbsenceResult;
import at.jku.tk.hiesmair.gv.parliament.db.result.PoliticianAbsenceResult;
import at.jku.tk.hiesmair.gv.parliament.entities.club.ParliamentClub;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

@Service
public class AbsenceService {

	@Inject
	private PoliticianRepository politicianRep;
	
	@Inject
	private ParliamentClubRepository clubRep;
	
	public List<ClubAbsenceResult> getClubAbsence(Integer period){
		List<AbsenceResult> absences = clubRep.countSessionAbsencesPerClubByPeriod(period, period);
		
		return getClubAbsences(absences);
	}

	private List<ClubAbsenceResult> getClubAbsences(List<AbsenceResult> absences) {
		List<ClubAbsenceResult> clubAbsences = new ArrayList<ClubAbsenceResult>(absences.size());
		
		for (AbsenceResult result : absences){
			ParliamentClub club = clubRep.findOne(result.getId());
			clubAbsences.add(new ClubAbsenceResult(result.getId(), result.getAbsenceCount(), result.getPresenceCount(), club));
		}
		
		return clubAbsences;
	}
	
	public List<PoliticianAbsenceResult> getPoliticianAbsence(Integer period) {
		List<AbsenceResult> absences = politicianRep.countSessionAbsencesByPeriod(period, period);
		
		return getPoliticianAbsences(absences);
	}
	
	private List<PoliticianAbsenceResult> getPoliticianAbsences(List<AbsenceResult> absences) {
		List<PoliticianAbsenceResult> politicianAbsences = new ArrayList<PoliticianAbsenceResult>(absences.size());
		
		for (AbsenceResult result : absences){
			Politician politician = politicianRep.findOne(result.getId());
			politicianAbsences.add(new PoliticianAbsenceResult(result.getId(), result.getAbsenceCount(), result.getPresenceCount(), politician));
		}
		
		return politicianAbsences;
	}
}
