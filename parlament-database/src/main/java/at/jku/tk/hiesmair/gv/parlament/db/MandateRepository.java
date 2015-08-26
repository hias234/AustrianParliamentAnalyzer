package at.jku.tk.hiesmair.gv.parlament.db;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import at.jku.tk.hiesmair.gv.parlament.entities.Politician;
import at.jku.tk.hiesmair.gv.parlament.entities.mandate.Mandate;

public interface MandateRepository extends CrudRepository<Mandate, Integer> {

	public List<Mandate> findByPolitician(Politician politician);
	
}
