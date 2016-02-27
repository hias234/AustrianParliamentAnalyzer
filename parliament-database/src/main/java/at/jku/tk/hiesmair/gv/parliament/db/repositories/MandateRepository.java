package at.jku.tk.hiesmair.gv.parliament.db.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import at.jku.tk.hiesmair.gv.parliament.entities.mandate.Mandate;
import at.jku.tk.hiesmair.gv.parliament.entities.mandate.NationalCouncilMember;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

public interface MandateRepository extends CrudRepository<Mandate, Integer> {

	public Mandate findByPoliticianAndDescriptionAndValidFrom(Politician politician, String description, Date validFrom);

	@Query("SELECT ncm FROM NationalCouncilMember ncm "
			+ " INNER JOIN ncm.periods per"
			+ " WHERE per.period = :period"
			+ " AND ncm.politician.id = :politician_id ")
	public List<NationalCouncilMember> findNationalCouncilMembersOfPoliticianAndPeriod(@Param("politician_id") String politicianId, @Param("period") Integer period);
}
