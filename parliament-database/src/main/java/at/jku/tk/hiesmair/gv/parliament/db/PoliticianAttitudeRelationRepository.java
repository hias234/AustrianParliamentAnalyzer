package at.jku.tk.hiesmair.gv.parliament.db;

import org.springframework.data.repository.CrudRepository;

import at.jku.tk.hiesmair.gv.parliament.entities.politician.PoliticianAttitudeRelation;

public interface PoliticianAttitudeRelationRepository extends CrudRepository<PoliticianAttitudeRelation, Integer> {

}
