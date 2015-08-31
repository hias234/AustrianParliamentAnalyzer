package at.jku.tk.hiesmair.gv.parlament.cache;

import java.util.Map;

import at.jku.tk.hiesmair.gv.parlament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parlament.entities.Politician;
import at.jku.tk.hiesmair.gv.parlament.entities.club.ParliamentClub;

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