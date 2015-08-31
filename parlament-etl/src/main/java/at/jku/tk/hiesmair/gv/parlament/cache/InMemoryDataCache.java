package at.jku.tk.hiesmair.gv.parlament.cache;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import org.springframework.stereotype.Component;

import at.jku.tk.hiesmair.gv.parlament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parlament.entities.Politician;
import at.jku.tk.hiesmair.gv.parlament.entities.club.ParliamentClub;

/**
 * caching of politicians and clubs so that it does not have to load it several times.
 * 
 * @author Markus
 *
 */
@Component
@Singleton
public class InMemoryDataCache implements DataCache {
	
	private Map<String, ParliamentClub> clubs = new HashMap<String, ParliamentClub>();
	private Map<String, Politician> politicians = new HashMap<String, Politician>();
	private Map<Integer, LegislativePeriod> legislativePeriods = new HashMap<Integer, LegislativePeriod>();

	/* (non-Javadoc)
	 * @see at.jku.tk.hiesmair.gv.parlament.cache.DCache#getClubs()
	 */
	@Override
	public Map<String, ParliamentClub> getClubs() {
		return clubs;
	}

	/* (non-Javadoc)
	 * @see at.jku.tk.hiesmair.gv.parlament.cache.DCache#setClubs(java.util.Map)
	 */
	@Override
	public void setClubs(Map<String, ParliamentClub> clubs) {
		this.clubs = clubs;
	}

	/* (non-Javadoc)
	 * @see at.jku.tk.hiesmair.gv.parlament.cache.DCache#getPoliticians()
	 */
	@Override
	public Map<String, Politician> getPoliticians() {
		return politicians;
	}

	/* (non-Javadoc)
	 * @see at.jku.tk.hiesmair.gv.parlament.cache.DCache#setPoliticians(java.util.Map)
	 */
	@Override
	public void setPoliticians(Map<String, Politician> politicians) {
		this.politicians = politicians;
	}
	
	

	/* (non-Javadoc)
	 * @see at.jku.tk.hiesmair.gv.parlament.cache.DCache#getLegislativePeriods()
	 */
	@Override
	public Map<Integer, LegislativePeriod> getLegislativePeriods() {
		return legislativePeriods;
	}

	/* (non-Javadoc)
	 * @see at.jku.tk.hiesmair.gv.parlament.cache.DCache#setLegislativePeriods(java.util.Map)
	 */
	@Override
	public void setLegislativePeriods(Map<Integer, LegislativePeriod> legislativePeriods) {
		this.legislativePeriods = legislativePeriods;
	}

	/* (non-Javadoc)
	 * @see at.jku.tk.hiesmair.gv.parlament.cache.DCache#getClub(java.lang.String)
	 */
	@Override
	public ParliamentClub getClub(String shortName){
		return getClubs().get(shortName);
	}
	
	/* (non-Javadoc)
	 * @see at.jku.tk.hiesmair.gv.parlament.cache.DCache#putClub(at.jku.tk.hiesmair.gv.parlament.entities.club.ParliamentClub)
	 */
	@Override
	public void putClub(ParliamentClub club){
		getClubs().put(club.getShortName(), club);
	}
	
	/* (non-Javadoc)
	 * @see at.jku.tk.hiesmair.gv.parlament.cache.DCache#getPolitician(java.lang.String)
	 */
	@Override
	public Politician getPolitician(String id){
		return getPoliticians().get(id);
	}
	
	/* (non-Javadoc)
	 * @see at.jku.tk.hiesmair.gv.parlament.cache.DCache#putPolitician(at.jku.tk.hiesmair.gv.parlament.entities.Politician)
	 */
	@Override
	public void putPolitician(Politician politician){
		getPoliticians().put(politician.getId(), politician);
	}
	
	/* (non-Javadoc)
	 * @see at.jku.tk.hiesmair.gv.parlament.cache.DCache#getLegislativePeriod(java.lang.Integer)
	 */
	@Override
	public LegislativePeriod getLegislativePeriod(Integer period){
		return legislativePeriods.get(period);
	}
	
	/* (non-Javadoc)
	 * @see at.jku.tk.hiesmair.gv.parlament.cache.DCache#putLegislativePeriod(at.jku.tk.hiesmair.gv.parlament.entities.LegislativePeriod)
	 */
	@Override
	public void putLegislativePeriod(LegislativePeriod period){
		legislativePeriods.put(period.getPeriod(), period);
	}
}
