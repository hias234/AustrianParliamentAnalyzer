package at.jku.tk.hiesmair.gv.parliament.db.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import at.jku.tk.hiesmair.gv.parliament.entities.discussion.Discussion;
import at.jku.tk.hiesmair.gv.parliament.entities.discussion.speech.DiscussionSpeech;

public interface DiscussionSpeechRepository extends CrudRepository<DiscussionSpeech, Integer> {

	public DiscussionSpeech findByDiscussionAndOrder(Discussion discussion, Integer order);
	
	public List<DiscussionSpeech> findByDiscussion(Discussion discussion);
}
