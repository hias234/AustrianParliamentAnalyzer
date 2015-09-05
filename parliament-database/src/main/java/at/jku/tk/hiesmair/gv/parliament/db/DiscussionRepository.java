package at.jku.tk.hiesmair.gv.parliament.db;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import at.jku.tk.hiesmair.gv.parliament.entities.discussion.Discussion;

@Transactional
public interface DiscussionRepository extends CrudRepository<Discussion, Integer>{

}
