package at.jku.tk.hiesmair.gv.parliament.db;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import at.jku.tk.hiesmair.gv.parliament.entities.session.Session;
import at.jku.tk.hiesmair.gv.parliament.entities.session.Session.SessionId;

@Transactional
public interface SessionRepository extends CrudRepository<Session, SessionId> {

}
