package at.jku.tk.hiesmair.gv.parliament.db;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import at.jku.tk.hiesmair.gv.parliament.entities.LegislativePeriod;

@Transactional
public interface LegislativePeriodRepository extends CrudRepository<LegislativePeriod, Integer> {

}
