package at.jku.tk.hiesmair.gv.parliament.db.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import at.jku.tk.hiesmair.gv.parliament.db.result.AbsenceResult;
import at.jku.tk.hiesmair.gv.parliament.entities.club.ParliamentClub;

public interface ParliamentClubRepository extends CrudRepository<ParliamentClub, String> {

	@Query("SELECT COUNT(ncm) FROM Session s INNER JOIN s.presentNationalCouncilMembers ncm WHERE ncm.club.shortName = :clubShortName")
	public long countSessionPresencesOfClub(@Param("clubShortName") String clubShortName);
	
	@Query("SELECT COUNT(ncm) FROM Session s INNER JOIN s.presentNationalCouncilMembers ncm WHERE ncm.club.shortName = :clubShortName AND s.period.period = :period")
	public long countSessionPresencesOfClubByPeriod(@Param("clubShortName") String clubShortName, @Param("period") Integer period);

	@Query("SELECT COUNT(ncm) FROM Session s INNER JOIN s.absentNationalCouncilMembers ncm WHERE ncm.club.shortName = :clubShortName")
	public long countSessionAbsencesOfClub(@Param("clubShortName") String clubShortName);

	@Query("SELECT COUNT(ncm) FROM Session s INNER JOIN s.absentNationalCouncilMembers ncm WHERE ncm.club.shortName = :clubShortName AND s.period.period = :period")
	public long countSessionAbsencesOfClubByPeriod(@Param("clubShortName") String clubShortName, @Param("period") Integer period);
	
	@Query(nativeQuery = true)
	public List<AbsenceResult> countSessionAbsencesPerClub();
	
	@Query(nativeQuery = true)
	public List<AbsenceResult> countSessionAbsencesPerClubByPeriod(@Param("periodFrom") Integer periodFrom, @Param("periodTo") Integer periodTo);
	
}
