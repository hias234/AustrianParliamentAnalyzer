package at.jku.tk.hiesmair.gv.parliament.db;

import org.springframework.data.repository.CrudRepository;

import at.jku.tk.hiesmair.gv.parliament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parliament.entities.session.Session;

public interface SessionRepository extends CrudRepository<Session, Integer> {

	public Session findByPeriodAndSessionNr(LegislativePeriod period, Integer sessionNr);
	
}
