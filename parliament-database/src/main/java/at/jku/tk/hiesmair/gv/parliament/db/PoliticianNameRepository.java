package at.jku.tk.hiesmair.gv.parliament.db;

import java.util.Date;

import org.springframework.data.repository.CrudRepository;

import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.PoliticianName;

public interface PoliticianNameRepository extends CrudRepository<PoliticianName, Integer> {

	public PoliticianName findByPoliticianAndValidUntil(Politician politician, Date validUntil);
	
}
