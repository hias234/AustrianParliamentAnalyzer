package at.jku.tk.hiesmair.gv.parlament.db;

import org.springframework.data.repository.CrudRepository;

import at.jku.tk.hiesmair.gv.parlament.entities.mandate.Mandate;

public interface MandateRepository extends CrudRepository<Mandate, Integer> {

}
