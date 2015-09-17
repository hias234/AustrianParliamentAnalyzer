package at.jku.tk.hiesmair.gv.parliament.db;

import org.springframework.data.repository.CrudRepository;

import at.jku.tk.hiesmair.gv.parliament.entities.discussion.speech.sentiment.DiscussionSpeechSentiment;

public interface DiscussionSpeechSentimentRepository extends CrudRepository<DiscussionSpeechSentiment, Integer> {

	
	
}
