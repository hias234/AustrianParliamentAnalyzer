package at.jku.tk.hiesmair.gv.parlament.db;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import at.jku.tk.hiesmair.gv.parlament.entities.discussion.DiscussionSpeech;

@Transactional
public interface DiscussionSpeechRepository extends CrudRepository<DiscussionSpeech, Integer> {

}
