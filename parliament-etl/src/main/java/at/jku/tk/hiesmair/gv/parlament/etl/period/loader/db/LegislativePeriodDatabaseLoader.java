package at.jku.tk.hiesmair.gv.parlament.etl.period.loader.db;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import at.jku.tk.hiesmair.gv.parlament.db.LegislativePeriodRepository;
import at.jku.tk.hiesmair.gv.parlament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parlament.etl.period.loader.LegislativePeriodLoader;

@Component
public class LegislativePeriodDatabaseLoader implements LegislativePeriodLoader {

	@Inject
	private LegislativePeriodRepository periodRepository;
	
	@Override
	public void loadLegislativePeriods(List<LegislativePeriod> periods) {
		periodRepository.delete(periodRepository.findAll());
		for (LegislativePeriod period : periods){
			// TODO
//			period.setSessions(new ArrayList<Session>());
			periodRepository.save(period);
		}
	}

}
