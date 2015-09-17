package at.jku.tk.hiesmair.gv.parliament.db;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import at.jku.tk.hiesmair.gv.parliament.entities.club.ParliamentClub;

@Transactional
public interface ParliamentClubRepository extends CrudRepository<ParliamentClub, Long> {

	@Query("SELECT COUNT(ncm) FROM Session s INNER JOIN s.presentNationalCouncilMembers ncm WHERE ncm.club.shortName = :clubShortName")
	public long countSessionPresencesOfClub(@Param("clubShortName") String clubShortName);
	
	@Query("SELECT COUNT(ncm) FROM Session s INNER JOIN s.presentNationalCouncilMembers ncm WHERE ncm.club.shortName = :clubShortName AND s.period.period = :period")
	public long countSessionPresencesOfClubByPeriod(@Param("clubShortName") String clubShortName, @Param("period") Integer period);

	@Query("SELECT COUNT(ncm) FROM Session s INNER JOIN s.absentNationalCouncilMembers ncm WHERE ncm.club.shortName = :clubShortName")
	public long countSessionAbsencesOfClub(@Param("clubShortName") String clubShortName);

	@Query("SELECT COUNT(ncm) FROM Session s INNER JOIN s.absentNationalCouncilMembers ncm WHERE ncm.club.shortName = :clubShortName AND s.period.period = :period")
	public long countSessionAbsencesOfClubByPeriod(@Param("clubShortName") String clubShortName, @Param("period") Integer period);
	
	
}
