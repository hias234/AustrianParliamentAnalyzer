package at.jku.tk.hiesmair.gv.parliament.db.repositories.relation;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import at.jku.tk.hiesmair.gv.parliament.entities.relation.PoliticianAttitudeRelation;
import at.jku.tk.hiesmair.gv.parliament.entities.relation.PoliticianAttitudeRelationByPeriod;

public interface PoliticianAttitudeRelationRepository extends CrudRepository<PoliticianAttitudeRelation, Integer> {

	@Query("SELECT new PoliticianAttitudeRelationByPeriod(par.politician1, par.politician2, par.discussion.session.period, SUM(par.weight)) FROM PoliticianAttitudeRelation par GROUP BY par.politician1, par.politician2, par.discussion.session.period")
	public List<PoliticianAttitudeRelationByPeriod> getPoliticianAttitudesByPeriods();
	
}
