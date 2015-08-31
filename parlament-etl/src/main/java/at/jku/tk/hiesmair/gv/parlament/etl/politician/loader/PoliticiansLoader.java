package at.jku.tk.hiesmair.gv.parlament.etl.politician.loader;

import java.util.List;

import at.jku.tk.hiesmair.gv.parlament.entities.Politician;

public interface PoliticiansLoader {

	public void loadPoliticians(List<Politician> politicians);
	
}
