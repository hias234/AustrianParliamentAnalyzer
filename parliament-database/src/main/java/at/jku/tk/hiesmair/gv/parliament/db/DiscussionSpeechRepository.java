package at.jku.tk.hiesmair.gv.parliament.db;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import at.jku.tk.hiesmair.gv.parliament.entities.discussion.speech.DiscussionSpeech;

@Transactional
public interface DiscussionSpeechRepository extends CrudRepository<DiscussionSpeech, Integer> {

}
