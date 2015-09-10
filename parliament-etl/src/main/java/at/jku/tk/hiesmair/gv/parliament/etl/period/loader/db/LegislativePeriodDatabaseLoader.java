package at.jku.tk.hiesmair.gv.parliament.etl.period.loader.db;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import at.jku.tk.hiesmair.gv.parliament.db.loader.ParliamentDatabaseLoader;
import at.jku.tk.hiesmair.gv.parliament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parliament.etl.period.loader.LegislativePeriodLoader;

@Component
public class LegislativePeriodDatabaseLoader implements LegislativePeriodLoader {

	@Inject
	private ParliamentDatabaseLoader databaseLoader;

	@Override
	public void loadLegislativePeriods(List<LegislativePeriod> periods) {
		periods.forEach(p -> databaseLoader.loadPeriod(p));
	}

}
