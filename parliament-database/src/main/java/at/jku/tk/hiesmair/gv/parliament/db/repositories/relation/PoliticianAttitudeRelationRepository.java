package at.jku.tk.hiesmair.gv.parliament.db.repositories.relation;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import at.jku.tk.hiesmair.gv.parliament.entities.relation.ClubAttitudeRelationByPeriod;
import at.jku.tk.hiesmair.gv.parliament.entities.relation.PoliticianAttitudeRelation;
import at.jku.tk.hiesmair.gv.parliament.entities.relation.PoliticianAttitudeRelationByPeriod;

public interface PoliticianAttitudeRelationRepository extends CrudRepository<PoliticianAttitudeRelation, Integer> {

	@Query("SELECT new PoliticianAttitudeRelationByPeriod(par.politician1, par.politician2, par.discussion.session.period, SUM(par.weight), COUNT(par.weight)) FROM PoliticianAttitudeRelation par GROUP BY par.politician1, par.politician2, par.discussion.session.period")
	public List<PoliticianAttitudeRelationByPeriod> getPoliticianAttitudesByPeriods();

	@Query("SELECT new PoliticianAttitudeRelation(par.politician1, par.politician2, SUM(par.weight), COUNT(par.weight)) "
			+ "FROM PoliticianAttitudeRelation par "
			+ "WHERE par.politician1.id = :politician_id "
			+ "OR par.politician2.id = :politician_id "
			+ "GROUP BY par.politician1, par.politician2 "
			+ "ORDER BY SUM(par.weight) / COUNT(par.weight) DESC, SUM(par.weight) DESC")
	public Page<PoliticianAttitudeRelation> getMostRelatedPoliticians(@Param("politician_id") String politicianId, Pageable page);
	
	@Query("SELECT new PoliticianAttitudeRelation(par.politician1, par.politician2, SUM(par.weight), COUNT(par.weight)) "
			+ "FROM PoliticianAttitudeRelation par "
			+ "WHERE par.politician1.id = :politician_id "
			+ "OR par.politician2.id = :politician_id "
			+ "GROUP BY par.politician1, par.politician2 "
			+ "ORDER BY SUM(par.weight) / COUNT(par.weight) ASC, SUM(par.weight) ASC")
	public Page<PoliticianAttitudeRelation> getLeastRelatedPoliticians(@Param("politician_id") String politicianId, Pageable page);
	
	@Query("SELECT new PoliticianAttitudeRelationByPeriod(par.politician1, par.politician2, par.discussion.session.period, SUM(par.weight), COUNT(par.weight)) "
			+ "FROM PoliticianAttitudeRelation par "
			+ "WHERE par.discussion.session.period.period = :period "
			+ "GROUP BY par.politician1, par.politician2, par.discussion.session.period")
	public List<PoliticianAttitudeRelationByPeriod> getPoliticianAttitudesByPeriod(@Param("period") Integer period);

	@Query("SELECT new PoliticianAttitudeRelationByPeriod(par.politician1, par.politician2, par.discussion.session.period, SUM(par.weight), COUNT(par.weight)) "
			+ "FROM PoliticianAttitudeRelation par "
			+ "WHERE par.discussion.session.period.period = :period "
			+ "AND LOWER(par.discussion.topic) LIKE CONCAT('%', LOWER(:topic), '%') "
			+ "GROUP BY par.politician1, par.politician2, par.discussion.session.period")
	public List<PoliticianAttitudeRelationByPeriod> getPoliticianAttitudesByPeriodAndDiscussionTopic(
			@Param("period") Integer period, @Param("topic") String topic);

	@Query("SELECT new ClubAttitudeRelationByPeriod(ncm1.club, ncm2.club, par.discussion.session.period, SUM(par.weight), COUNT(par.weight)) FROM PoliticianAttitudeRelation par "
			+ " INNER JOIN TREAT(par.politician1.mandates as NationalCouncilMember) ncm1 "
			+ " INNER JOIN TREAT(par.politician2.mandates as NationalCouncilMember) ncm2 "
			+ " INNER JOIN ncm1.periods periods1 INNER JOIN ncm2.periods periods2 "
			+ " WHERE periods1.period = par.discussion.session.period.period AND periods2.period = par.discussion.session.period.period "
			+ " AND par.discussion.session.startDate >= ncm1.validFrom AND (ncm1.validUntil is null OR par.discussion.session.startDate <= ncm1.validUntil) "
			+ " AND par.discussion.session.startDate >= ncm2.validFrom AND (ncm2.validUntil is null OR par.discussion.session.startDate <= ncm2.validUntil) "
			+ "GROUP BY ncm1.club, ncm2.club, par.discussion.session.period")
	public List<ClubAttitudeRelationByPeriod> getClubAttitudesByPeriods();

	@Query("SELECT new ClubAttitudeRelationByPeriod(ncm1.club, ncm2.club, par.discussion.session.period, SUM(par.weight), COUNT(par.weight)) FROM PoliticianAttitudeRelation par "
			+ " INNER JOIN TREAT(par.politician1.mandates as NationalCouncilMember) ncm1 "
			+ " INNER JOIN TREAT(par.politician2.mandates as NationalCouncilMember) ncm2 "
			+ " INNER JOIN ncm1.periods periods1 INNER JOIN ncm2.periods periods2 "
			+ " WHERE par.discussion.session.period.period = :period "
			+ " AND periods1.period = par.discussion.session.period.period AND periods2.period = par.discussion.session.period.period "
			+ " AND par.discussion.session.startDate >= ncm1.validFrom AND (ncm1.validUntil is null OR par.discussion.session.startDate <= ncm1.validUntil) "
			+ " AND par.discussion.session.startDate >= ncm2.validFrom AND (ncm2.validUntil is null OR par.discussion.session.startDate <= ncm2.validUntil) "
			+ "GROUP BY ncm1.club, ncm2.club, par.discussion.session.period")
	public List<ClubAttitudeRelationByPeriod> getClubAttitudesByPeriod(@Param("period") Integer period);
}
