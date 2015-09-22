package at.jku.tk.hiesmair.gv.parliament.db.repositories;

import org.springframework.data.repository.CrudRepository;

import at.jku.tk.hiesmair.gv.parliament.entities.session.Session;
import at.jku.tk.hiesmair.gv.parliament.entities.session.SessionChairMan;

public interface SessionChairManRepository extends CrudRepository<SessionChairMan, Integer> {

	public SessionChairMan findBySessionAndPosition(Session session, Integer position);
	
}
