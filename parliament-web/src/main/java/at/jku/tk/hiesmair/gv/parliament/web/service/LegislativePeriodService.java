package at.jku.tk.hiesmair.gv.parliament.web.service;

import java.util.List;

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
}
