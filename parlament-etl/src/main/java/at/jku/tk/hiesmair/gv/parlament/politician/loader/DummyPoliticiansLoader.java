package at.jku.tk.hiesmair.gv.parlament.politician.loader;

import java.util.List;

import org.springframework.stereotype.Component;

import at.jku.tk.hiesmair.gv.parlament.entities.Politician;

/**
 * Doesn't do anything after extracting politicians
 * @author Markus
 *
 */
@Component
public class DummyPoliticiansLoader implements PoliticiansLoader {

	@Override
	public void loadPoliticians(List<Politician> politicians) {
		
	}

}
