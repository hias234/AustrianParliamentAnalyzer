package at.jku.tk.hiesmair.gv.parliament.web.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import at.jku.tk.hiesmair.gv.parliament.db.repositories.LegislativePeriodRepository;
import at.jku.tk.hiesmair.gv.parliament.entities.LegislativePeriod;

@Service
public class LegislativePeriodService {

	@Inject
	private LegislativePeriodRepository periodRep;
	
	public Iterable<LegislativePeriod> findAll(){
		return periodRep.findAll();
	}
	
	public Double getAbsencePercentage(Integer period){
		Long presences = periodRep.getSessionPresenceCountOfPeriod(period);
		Long absences = periodRep.getSessionAbsenceCountOfPeriod(period);
		
		return Double.valueOf(absences) / (presences + absences);
	}
	
	public int getLatestPeriod(){
		return periodRep.getLatestPeriod();
	}
	
	public LegislativePeriod findOne(Integer period){
		return periodRep.findOne(period);
	}
}
