package at.jku.tk.hiesmair.gv.parliament.db;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import at.jku.tk.hiesmair.gv.parliament.entities.mandate.Mandate;
import at.jku.tk.hiesmair.gv.parliament.entities.mandate.Mandate.MandateId;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

public interface MandateRepository extends CrudRepository<Mandate, MandateId> {
	
}
