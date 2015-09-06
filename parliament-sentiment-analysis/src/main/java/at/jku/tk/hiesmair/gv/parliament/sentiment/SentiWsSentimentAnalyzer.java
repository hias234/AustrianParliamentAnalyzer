package at.jku.tk.hiesmair.gv.parliament.sentiment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import at.jku.tk.hiesmair.gv.parliament.entities.discussion.speech.sentiment.Sentiment;

@Component
public class SentiWsSentimentAnalyzer implements SentimentAnalyzer {

	public static final String SENTIWS_GENERATOR = "SENTIWS";
	
	private Map<String, Double> dict = new HashMap<String, Double>();

	public SentiWsSentimentAnalyzer() throws IOException {
		parseSentiWS(SentiWsSentimentAnalyzer.class.getClassLoader().getResourceAsStream("SentiWS_v1.8c_Negative.txt"));
		parseSentiWS(SentiWsSentimentAnalyzer.class.getClassLoader().getResourceAsStream("SentiWS_v1.8c_Positive.txt"));
	}

	protected void parseSentiWS(InputStream is) throws IOException {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(is))) {
			for (String line = in.readLine(); line != null; line = in.readLine()) {
				// input file will have a space- or tab-separated list per line:
				// - first component is the main word with a specification what
				// kind of word it is (can be result from POS tagging)
				// - second component is the positive or negative sentiment
				// associated with the word
				// - third argument is a comma-separated list of deflections of
				// the word as they might also occur in text
				String[] components = line.split("\\s");

				// parse the weight
				Double weight = Double.valueOf(components[1]);

				// get the main word
				String[] wordPart = components[0].split("\\|");
				String mainWord = wordPart[0];
				dict.put(mainWord, weight);

				// get the remaining words (deflections)
				if (components.length > 2) {
					for (String word : components[2].split(",")) {
						dict.put(word, weight);
					}
				}
			}
		}
	}

	protected double classifyWord(String word) {
		Double sentiment = dict.get(word);
		return sentiment != null ? sentiment.doubleValue() : 0.0;
	}

	@Override
	public List<Sentiment> getSentiments(String text) {
		String[] tokens = text.split("[\\s,.();-?!%$€+#:<>|=\\\\]");
		
		Integer nrOfPositiveTokens = 0;
		Integer nrOfNegativeTokens = 0;
		
		Double positiveSentiment = 0.0;
		Double negativeSentiment = 0.0;
		Double sentiment = 0.0;
		for (String token : tokens){
			Double tokentSentiment = classifyWord(token);
			sentiment += tokentSentiment;
			if (tokentSentiment > 0.0){
				positiveSentiment += tokentSentiment;
				nrOfPositiveTokens++;
			}
			else if (tokentSentiment < 0.0) {
				negativeSentiment += tokentSentiment;
				nrOfNegativeTokens++;
			}
		}
		Double absoluteSentiment = positiveSentiment - negativeSentiment;
		if (absoluteSentiment != 0){
			sentiment /= absoluteSentiment;
			positiveSentiment /= absoluteSentiment;
			negativeSentiment /= absoluteSentiment;
		}
		
		return Arrays.asList(new Sentiment(SENTIWS_GENERATOR, sentiment, positiveSentiment, negativeSentiment));
	}

}
