package at.jku.tk.hiesmair.gv.parliament.db.repositories.relation;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import at.jku.tk.hiesmair.gv.parliament.entities.relation.ClubAttitudeRelationByPeriod;
import at.jku.tk.hiesmair.gv.parliament.entities.relation.PoliticianAttitudeRelation;
import at.jku.tk.hiesmair.gv.parliament.entities.relation.PoliticianAttitudeRelationByPeriod;

public interface PoliticianAttitudeRelationRepository extends CrudRepository<PoliticianAttitudeRelation, Integer> {

	@Query("SELECT new PoliticianAttitudeRelationByPeriod(par.politician1, par.politician2, par.discussion.session.period, SUM(par.weight)) FROM PoliticianAttitudeRelation par GROUP BY par.politician1, par.politician2, par.discussion.session.period")
	public List<PoliticianAttitudeRelationByPeriod> getPoliticianAttitudesByPeriods();

	@Query("SELECT new PoliticianAttitudeRelationByPeriod(par.politician1, par.politician2, par.discussion.session.period, SUM(par.weight)) "
			+ "FROM PoliticianAttitudeRelation par "
			+ "WHERE par.discussion.session.period.period = :period "
			+ "GROUP BY par.politician1, par.politician2, par.discussion.session.period")
	public List<PoliticianAttitudeRelationByPeriod> getPoliticianAttitudesByPeriod(@Param("period") Integer period);

	@Query("SELECT new ClubAttitudeRelationByPeriod(ncm1.club, ncm2.club, par.discussion.session.period, SUM(par.weight)) FROM PoliticianAttitudeRelation par "
			+ " INNER JOIN TREAT(par.politician1.mandates as NationalCouncilMember) ncm1 "
			+ " INNER JOIN TREAT(par.politician2.mandates as NationalCouncilMember) ncm2 "
			+ " INNER JOIN ncm1.periods periods1 INNER JOIN ncm2.periods periods2 "
			+ " WHERE periods1.period = par.discussion.session.period.period AND periods2.period = par.discussion.session.period.period "
			+ "GROUP BY ncm1.club, ncm2.club, par.discussion.session.period")
	public List<ClubAttitudeRelationByPeriod> getClubAttitudesByPeriods();
	
	@Query("SELECT new ClubAttitudeRelationByPeriod(ncm1.club, ncm2.club, par.discussion.session.period, SUM(par.weight)) FROM PoliticianAttitudeRelation par "
			+ " INNER JOIN TREAT(par.politician1.mandates as NationalCouncilMember) ncm1 "
			+ " INNER JOIN TREAT(par.politician2.mandates as NationalCouncilMember) ncm2 "
			+ " INNER JOIN ncm1.periods periods1 INNER JOIN ncm2.periods periods2 "
			+ " WHERE par.discussion.session.period.period = :period "
			+ " AND periods1.period = par.discussion.session.period.period AND periods2.period = par.discussion.session.period.period "
			+ "GROUP BY ncm1.club, ncm2.club, par.discussion.session.period")
	public List<ClubAttitudeRelationByPeriod> getClubAttitudesByPeriod(@Param("period") Integer period);
}
