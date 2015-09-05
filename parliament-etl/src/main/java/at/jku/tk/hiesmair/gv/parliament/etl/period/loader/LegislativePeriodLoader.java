package at.jku.tk.hiesmair.gv.parliament.etl.period.loader;

import java.util.List;

import at.jku.tk.hiesmair.gv.parliament.entities.LegislativePeriod;

public interface LegislativePeriodLoader {

	public void loadLegislativePeriods(List<LegislativePeriod> periods);
	
}
