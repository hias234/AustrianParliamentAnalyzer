package at.jku.tk.hiesmair.gv.parliament.db.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import at.jku.tk.hiesmair.gv.parliament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parliament.entities.session.Session;

public interface SessionRepository extends CrudRepository<Session, Integer> {

	public Session findByPeriodAndSessionNr(LegislativePeriod period, Integer sessionNr);
	
	public List<Session> findByPeriod(LegislativePeriod period);
}
