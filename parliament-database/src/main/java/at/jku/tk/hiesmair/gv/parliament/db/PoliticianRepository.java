package at.jku.tk.hiesmair.gv.parliament.db;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

@Transactional
public interface PoliticianRepository extends CrudRepository<Politician, String> {

}
