package at.jku.tk.hiesmair.gv.parliament.db;

import org.springframework.data.jpa.repository.JpaRepository;

import at.jku.tk.hiesmair.gv.parliament.entities.session.SessionChairMan;
import at.jku.tk.hiesmair.gv.parliament.entities.session.SessionChairMan.SessionChairManId;

public interface SessionChairManRepository extends JpaRepository<SessionChairMan, SessionChairManId> {

}
