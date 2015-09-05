package at.jku.tk.hiesmair.gv.parliament.etl.politician.loader;

import java.util.List;

import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

public interface PoliticiansLoader {

	public void loadPoliticians(List<Politician> politicians);
	
}
