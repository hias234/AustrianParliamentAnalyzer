package at.jku.tk.hiesmair.gv.parliament.sentiment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import at.jku.tk.hiesmair.gv.parliament.entities.discussion.speech.sentiment.Sentiment;

@Primary
@Component
public class CombinedSentimentAnalyzer implements SentimentAnalyzer {

	protected Collection<SentimentAnalyzer> sentimentAnalyzers;

	@Inject
	public CombinedSentimentAnalyzer(Collection<SentimentAnalyzer> sentimentAnalyzers) {
		super();
		this.sentimentAnalyzers = sentimentAnalyzers.stream()
				.filter(a -> !a.getClass().equals(CombinedSentimentAnalyzer.class)).collect(Collectors.toList());
	}

	@Override
	public List<Sentiment> getSentiments(String text) {
		List<Sentiment> sentiments = new ArrayList<Sentiment>();
		for (SentimentAnalyzer analyzer : sentimentAnalyzers) {
			try {
				sentiments.addAll(analyzer.getSentiments(text));
			} catch (SentimentAnalyzerException se) {
			}
		}
		return sentiments;
	}

}
