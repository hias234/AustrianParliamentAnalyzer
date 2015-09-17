package at.jku.tk.hiesmair.gv.parliament.etl.politician.loader;

import java.util.List;

import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

/**
 * Doesn't do anything after extracting politicians
 * @author Markus
 *
 */
//@Component
public class DummyPoliticiansLoader implements PoliticiansLoader {

	@Override
	public void loadPoliticians(List<Politician> politicians) {
		
	}

}
