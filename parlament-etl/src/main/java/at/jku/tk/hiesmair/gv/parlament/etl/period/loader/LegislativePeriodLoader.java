package at.jku.tk.hiesmair.gv.parlament.etl.period.loader;

import java.util.List;

import at.jku.tk.hiesmair.gv.parlament.entities.LegislativePeriod;

public interface LegislativePeriodLoader {

	public void loadLegislativePeriods(List<LegislativePeriod> periods);
	
}