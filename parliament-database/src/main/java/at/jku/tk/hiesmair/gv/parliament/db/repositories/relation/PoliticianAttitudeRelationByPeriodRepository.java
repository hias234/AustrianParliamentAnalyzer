package at.jku.tk.hiesmair.gv.parliament.db.repositories.relation;

import org.springframework.data.repository.CrudRepository;

import at.jku.tk.hiesmair.gv.parliament.entities.relation.PoliticianAttitudeRelationByPeriod;

public interface PoliticianAttitudeRelationByPeriodRepository extends
		CrudRepository<PoliticianAttitudeRelationByPeriod, Integer> {

}
