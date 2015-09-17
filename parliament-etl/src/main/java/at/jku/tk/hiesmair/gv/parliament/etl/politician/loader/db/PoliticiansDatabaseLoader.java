package at.jku.tk.hiesmair.gv.parliament.etl.politician.loader.db;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import at.jku.tk.hiesmair.gv.parliament.db.loader.ParliamentDatabaseLoader;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;
import at.jku.tk.hiesmair.gv.parliament.etl.politician.loader.PoliticiansLoader;

@Component
public class PoliticiansDatabaseLoader implements PoliticiansLoader {

	@Inject
	private ParliamentDatabaseLoader databaseLoader;

	@Override
	public void loadPoliticians(List<Politician> politicians) {
		for (Politician p : politicians) {
			databaseLoader.loadPolitician(p);
		}
	}

}
