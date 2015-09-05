package at.jku.tk.hiesmair.gv.parliament.entities.discussion;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class DiscussionSpeechSentiment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne(optional = false)
	private DiscussionSpeech speech;

	private String generator;

	private Double sentiment;
	private Double positiveSentiment;
	private Double negativeSentiment;

	public DiscussionSpeechSentiment() {
		super();
	}

	public DiscussionSpeechSentiment(DiscussionSpeech speech, String generator, Double sentiment,
			Double positiveSentiment, Double negativeSentiment) {
		super();
		this.speech = speech;
		this.generator = generator;
		this.sentiment = sentiment;
		this.positiveSentiment = positiveSentiment;
		this.negativeSentiment = negativeSentiment;
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

	public String getGenerator() {
		return generator;
	}

	public void setGenerator(String generator) {
		this.generator = generator;
	}

	public Double getSentiment() {
		return sentiment;
	}

	public void setSentiment(Double sentiment) {
		this.sentiment = sentiment;
	}

	public Double getPositiveSentiment() {
		return positiveSentiment;
	}

	public void setPositiveSentiment(Double positiveSentiment) {
		this.positiveSentiment = positiveSentiment;
	}

	public Double getNegativeSentiment() {
		return negativeSentiment;
	}

	public void setNegativeSentiment(Double negativeSentiment) {
		this.negativeSentiment = negativeSentiment;
	}

}
