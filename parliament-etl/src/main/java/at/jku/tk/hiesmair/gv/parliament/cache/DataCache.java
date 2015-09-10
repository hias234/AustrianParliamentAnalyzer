package at.jku.tk.hiesmair.gv.parliament.cache;

import java.util.Map;

import at.jku.tk.hiesmair.gv.parliament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parliament.entities.club.ParliamentClub;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

/**
 * Caching data in memory so that it does not have to be loaded several times.
 * 
 * @author Markus
 *
 */
public interface DataCache {

	public abstract Map<String, ParliamentClub> getClubs();

	public abstract void setClubs(Map<String, ParliamentClub> clubs);

	public abstract Map<String, Politician> getPoliticians();

	public abstract void setPoliticians(Map<String, Politician> politicians);

	public abstract Map<Integer, LegislativePeriod> getLegislativePeriods();

	public abstract void setLegislativePeriods(Map<Integer, LegislativePeriod> legislativePeriods);

	public abstract ParliamentClub getClub(String shortName);

	public abstract void putClub(ParliamentClub club);

	public abstract Politician getPolitician(String id);

	public abstract void putPolitician(Politician politician);
	
	public abstract LegislativePeriod getLegislativePeriod(Integer period);

	public abstract void putLegislativePeriod(LegislativePeriod period);

}