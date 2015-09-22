package at.jku.tk.hiesmair.gv.parliament.db.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import at.jku.tk.hiesmair.gv.parliament.entities.discussion.Discussion;
import at.jku.tk.hiesmair.gv.parliament.entities.session.Session;

public interface DiscussionRepository extends CrudRepository<Discussion, Integer>{

	public Discussion findBySessionAndOrder(Session session, Integer order);
	
	public List<Discussion> findBySession(Session session);
}
