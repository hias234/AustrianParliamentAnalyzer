package at.jku.tk.hiesmair.gv.parliament.db;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

public interface PoliticianRepository extends CrudRepository<Politician, String> {

	@Query("SELECT COUNT(ncm) FROM Session s INNER JOIN s.presentNationalCouncilMembers ncm WHERE ncm.id.politician.id = :politicianId")
	public long countSessionsPresences(@Param("politicianId") String id);

}
