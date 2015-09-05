package at.jku.tk.hiesmair.gv.parliament.sentiment;

public class SentimentAnalyzerException extends RuntimeException {

	private static final long serialVersionUID = -1845321224241028570L;

	public SentimentAnalyzerException(String generator, String text, Throwable exception) {
		super("Error during sentiment-analysis with generator " + generator + " and text (" + text + ")", exception);
	}

}
