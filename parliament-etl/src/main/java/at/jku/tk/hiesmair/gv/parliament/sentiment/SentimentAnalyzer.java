package at.jku.tk.hiesmair.gv.parliament.sentiment;

import at.jku.tk.hiesmair.gv.parliament.entities.discussion.speech.sentiment.Sentiment;

/**
 * Takes text and returns the sentiment
 * 
 * @author Markus
 *
 */
public interface SentimentAnalyzer {

	/**
	 * Analyzes the given text on sentiment
	 * 
	 * @param text The text to be analyzed
	 * @return positive, negative and overall sentiment of the text
	 */
	public Sentiment getSentiment(String text);
	
}
