package at.jku.tk.hiesmair.gv.parliament.db;

import org.springframework.data.repository.CrudRepository;

import at.jku.tk.hiesmair.gv.parliament.entities.discussion.Discussion;
import at.jku.tk.hiesmair.gv.parliament.entities.discussion.Discussion.DiscussionId;

public interface DiscussionRepository extends CrudRepository<Discussion, DiscussionId>{

}
