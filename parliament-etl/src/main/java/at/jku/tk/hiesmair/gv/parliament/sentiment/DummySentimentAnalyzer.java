package at.jku.tk.hiesmair.gv.parliament.sentiment;

import java.util.ArrayList;
import java.util.List;

import at.jku.tk.hiesmair.gv.parliament.entities.discussion.speech.sentiment.Sentiment;

public class DummySentimentAnalyzer implements SentimentAnalyzer {

	@Override
	public List<Sentiment> getSentiments(String text) {
		return new ArrayList<Sentiment>();
	}

}
