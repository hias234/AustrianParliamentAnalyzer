package at.jku.tk.hiesmair.gv.parliament.db;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

public interface PoliticianRepository extends CrudRepository<Politician, String> {

	@Query("SELECT COUNT(ncm) FROM Session s INNER JOIN s.presentNationalCouncilMembers ncm WHERE ncm.id.politician.id = :politicianId")
	public long countSessionPresences(@Param("politicianId") String id);
	
	@Query("SELECT COUNT(ncm) FROM Session s INNER JOIN s.presentNationalCouncilMembers ncm WHERE ncm.id.politician.id = :politicianId AND s.id.period.period = :period")
	public long countSessionPresencesByPeriod(@Param("politicianId") String id, @Param("period") Integer period);

	@Query("SELECT COUNT(ncm) FROM Session s INNER JOIN s.absentNationalCouncilMembers ncm WHERE ncm.id.politician.id = :politicianId")
	public long countSessionAbsences(@Param("politicianId") String id);

	@Query("SELECT COUNT(ncm) FROM Session s INNER JOIN s.absentNationalCouncilMembers ncm WHERE ncm.id.politician.id = :politicianId AND s.id.period.period = :period")
	public long countSessionAbsencesByPeriod(@Param("politicianId") String id, @Param("period") Integer period);
	
}
