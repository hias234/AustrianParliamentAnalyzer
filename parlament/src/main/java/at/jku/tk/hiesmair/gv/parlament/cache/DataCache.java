package at.jku.tk.hiesmair.gv.parlament.cache;

import java.util.HashMap;
import java.util.Map;

import at.jku.tk.hiesmair.gv.parlament.entities.Politician;
import at.jku.tk.hiesmair.gv.parlament.entities.club.ParliamentClub;

/**
 * caching of politicians and clubs so that it does not have to load it several times.
 * 
 * @author Markus
 *
 */
public class DataCache {

	private static DataCache INSTANCE = null;
	
	public static DataCache getInstance(){
		if (INSTANCE == null){
			INSTANCE = new DataCache();
		}
		return INSTANCE;
	}
	
	private DataCache(){}
	
	private Map<String, ParliamentClub> clubs = new HashMap<String, ParliamentClub>();
	private Map<String, Politician> politicians = new HashMap<String, Politician>();

	public Map<String, ParliamentClub> getClubs() {
		return clubs;
	}

	public void setClubs(Map<String, ParliamentClub> clubs) {
		this.clubs = clubs;
	}

	public Map<String, Politician> getPoliticians() {
		return politicians;
	}

	public void setPoliticians(Map<String, Politician> politicians) {
		this.politicians = politicians;
	}

	public ParliamentClub getClub(String shortName){
		return getClubs().get(shortName);
	}
	
	public void putClub(ParliamentClub club){
		getClubs().put(club.getShortName(), club);
	}
	
	public Politician getPolitician(String id){
		return getPoliticians().get(id);
	}
	
	public void putPolitician(Politician politician){
		getPoliticians().put(politician.getId(), politician);
	}
}
