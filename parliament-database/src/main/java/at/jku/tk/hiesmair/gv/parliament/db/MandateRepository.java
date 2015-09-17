package at.jku.tk.hiesmair.gv.parliament.db;

import java.util.Date;

import org.springframework.data.repository.CrudRepository;

import at.jku.tk.hiesmair.gv.parliament.entities.mandate.Mandate;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

public interface MandateRepository extends CrudRepository<Mandate, Integer> {

	public Mandate findByPoliticianAndDescriptionAndValidFrom(Politician politician, String description, Date validFrom);

}
