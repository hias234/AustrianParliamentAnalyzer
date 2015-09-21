package at.jku.tk.hiesmair.gv.parliament.db;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import at.jku.tk.hiesmair.gv.parliament.db.result.AbsenceResult;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

public interface PoliticianRepository extends CrudRepository<Politician, String> {

	@Query("SELECT COUNT(ncm) FROM Session s INNER JOIN s.presentNationalCouncilMembers ncm WHERE ncm.id.politician.id = :politicianId")
	public long countSessionPresencesOfPolitician(@Param("politicianId") String id);
	
	@Query("SELECT COUNT(ncm) FROM Session s INNER JOIN s.presentNationalCouncilMembers ncm WHERE ncm.id.politician.id = :politicianId AND s.period.period = :period")
	public long countSessionPresencesOfPoliticianByPeriod(@Param("politicianId") String id, @Param("period") Integer period);

	@Query("SELECT COUNT(ncm) FROM Session s INNER JOIN s.absentNationalCouncilMembers ncm WHERE ncm.id.politician.id = :politicianId")
	public long countSessionAbsencesOfPolitician(@Param("politicianId") String id);

	@Query("SELECT COUNT(ncm) FROM Session s INNER JOIN s.absentNationalCouncilMembers ncm WHERE ncm.id.politician.id = :politicianId AND s.period.period = :period")
	public long countSessionAbsencesOfPoliticianByPeriod(@Param("politicianId") String id, @Param("period") Integer period);
	
	@Query("SELECT p FROM Politician p INNER JOIN TREAT(p.mandates as NationalCouncilMember) ncm INNER JOIN ncm.periods per WHERE per.period = :period")
	public List<Politician> findNationalCouncilMembersOfPeriod(@Param("period") Integer period);
	
	@Query(nativeQuery = true)
	public List<AbsenceResult> countSessionAbsences();
	
	@Query(nativeQuery = true)
	public List<AbsenceResult> countSessionAbsencesByPeriod(@Param("periodFrom") Integer periodFrom, @Param("periodTo") Integer periodTo);
	
	@Query("SELECT COUNT(ds) FROM DiscussionSpeech ds WHERE ds.politician.id = :politicianId")
	public long countSpeechesOfPolitician(@Param("politicianId") String id);
	
	@Query("SELECT COUNT(ds) FROM DiscussionSpeech ds WHERE ds.politician.id = :politicianId AND ds.id.discussion.session.period.period = :period")
	public long countSpeechesOfPoliticianByPeriod(@Param("politicianId") String id, @Param("period") Integer period);
}
