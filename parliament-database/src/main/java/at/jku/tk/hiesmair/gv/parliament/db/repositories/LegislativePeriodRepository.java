package at.jku.tk.hiesmair.gv.parliament.db.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import at.jku.tk.hiesmair.gv.parliament.entities.LegislativePeriod;

public interface LegislativePeriodRepository extends CrudRepository<LegislativePeriod, Integer> {

	@Query("SELECT COUNT(ncm) FROM Session s INNER JOIN s.presentNationalCouncilMembers ncm WHERE s.period.period = :period")
	public long getSessionPresenceCountOfPeriod(@Param("period") Integer period);
	
	@Query("SELECT COUNT(ncm) FROM Session s INNER JOIN s.absentNationalCouncilMembers ncm WHERE s.period.period = :period")
	public long getSessionAbsenceCountOfPeriod(@Param("period") Integer period);
	
	@Query("SELECT MAX(p.period) FROM LegislativePeriod p")
	public int getLatestPeriod();
}
