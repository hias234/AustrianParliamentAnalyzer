package at.jku.tk.hiesmair.gv.parliament.entities.discussion.speech.sentiment;

import javax.persistence.Embeddable;

@Embeddable
public class Sentiment {

	private String generator;
	private Double sentiment;
	private Double positiveSentiment;
	private Double negativeSentiment;

	public Sentiment() {
		super();
	}

	public Sentiment(String generator, Double sentiment, Double positiveSentiment, Double negativeSentiment) {
		super();
		this.generator = generator;
		this.sentiment = sentiment;
		this.positiveSentiment = positiveSentiment;
		this.negativeSentiment = negativeSentiment;
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

	@Override
	public String toString() {
		return "Sentiment [generator=" + generator + ", sentiment=" + sentiment + ", positiveSentiment="
				+ positiveSentiment + ", negativeSentiment=" + negativeSentiment + "]";
	}

}
