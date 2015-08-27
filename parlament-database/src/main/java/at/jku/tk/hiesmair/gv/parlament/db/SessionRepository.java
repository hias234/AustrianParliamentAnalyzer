package at.jku.tk.hiesmair.gv.parlament.db;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import at.jku.tk.hiesmair.gv.parlament.entities.session.Session;

@Transactional
public interface SessionRepository extends CrudRepository<Session, Integer> {

}
