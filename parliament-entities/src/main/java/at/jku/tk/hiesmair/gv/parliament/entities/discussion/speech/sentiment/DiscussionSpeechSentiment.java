package at.jku.tk.hiesmair.gv.parliament.entities.discussion.speech.sentiment;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import at.jku.tk.hiesmair.gv.parliament.entities.discussion.speech.DiscussionSpeech;

@Entity
public class DiscussionSpeechSentiment implements Serializable {

	private static final long serialVersionUID = 6011897124215436250L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne(optional = false)
	private DiscussionSpeech speech;

	@Embedded
	private Sentiment sentiment = new Sentiment();

	public DiscussionSpeechSentiment() {
		super();
	}

	public DiscussionSpeechSentiment(DiscussionSpeech speech, Sentiment sentiment) {
		super();
		this.speech = speech;
		this.sentiment = sentiment;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public DiscussionSpeech getSpeech() {
		return speech;
	}

	public void setSpeech(DiscussionSpeech speech) {
		this.speech = speech;
	}

	public void setSentiment(Sentiment sentiment) {
		this.sentiment = sentiment;
	}

	public String getGenerator() {
		return sentiment.getGenerator();
	}

	public void setGenerator(String generator) {
		this.sentiment.setGenerator(generator);
	}

	public Double getSentiment() {
		return sentiment.getSentiment();
	}

	public void setSentiment(Double sentiment) {
		this.sentiment.setSentiment(sentiment);
	}

	public Double getPositiveSentiment() {
		return sentiment.getPositiveSentiment();
	}

	public void setPositiveSentiment(Double positiveSentiment) {
		this.sentiment.setPositiveSentiment(positiveSentiment);
	}

	public Double getNegativeSentiment() {
		return sentiment.getNegativeSentiment();
	}

	public void setNegativeSentiment(Double negativeSentiment) {
		this.sentiment.setNegativeSentiment(negativeSentiment);
	}

}
