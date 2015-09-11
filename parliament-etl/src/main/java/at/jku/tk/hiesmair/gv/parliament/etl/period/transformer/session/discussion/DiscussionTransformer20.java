package at.jku.tk.hiesmair.gv.parliament.etl.period.transformer.session.discussion;

import java.io.IOException;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import at.jku.tk.hiesmair.gv.parliament.etl.politician.transformer.PoliticianTransformer;
import at.jku.tk.hiesmair.gv.parliament.sentiment.SentimentAnalyzer;

@Component
public class DiscussionTransformer20 extends AbstractDiscussionTransformer {

	private static final Logger logger = Logger.getLogger(DiscussionTransformer20.class.getSimpleName());

	@Inject
	public DiscussionTransformer20(PoliticianTransformer politicianTransformer, SentimentAnalyzer sentimentAnalyzer) {
		super(politicianTransformer, sentimentAnalyzer);
	}

	@Override
	protected String getSpeechText(Element speechPartElement) {
		if (speechPartElement.parent().tagName().equals("body")) {
			// handle 116

			String speechText = null;
			String firstText = speechPartElement.text();
			int indexOfColon = firstText.indexOf(":");
			if (indexOfColon != -1) {

				speechText = firstText.substring(indexOfColon + 1).trim();

				speechPartElement = speechPartElement.nextElementSibling();
				for (; speechPartElement != null && !matchesSpeechBeginPattern(speechPartElement); speechPartElement = speechPartElement
						.nextElementSibling()) {
					speechText += "\n\n" + speechPartElement.text();
				}
			}
			return speechText;
		}

		String text = speechPartElement.parent().text().replaceAll(NBSP_STRING, " ");
		int indexOfColon = text.indexOf(":");
		if (indexOfColon == -1) {
			return text;
		}
		return text.substring(indexOfColon + 1).trim();
	}

	@Override
	protected Element getFirstSpeechTextElement(Element speechBeginElement) throws IOException {
		if (speechBeginElement.parent().tagName().equals("p")) {
			// for e.g. protocol 116

			Element speechTextElement = speechBeginElement.parent();
			for (int i = 0; speechTextElement != null; i++, speechTextElement = speechTextElement.nextElementSibling()) {
				if (getPoliticianOfSpeech(speechTextElement) != null) {
					return speechTextElement;
				}
				if (i > 0 && matchesSpeechBeginPattern(speechTextElement)) {
					break;
				}
			}

			return null;
		}

		Element speechTextElement = speechBeginElement.nextElementSibling();
		for (; speechTextElement != null; speechTextElement = speechTextElement.nextElementSibling()) {
			if (!speechTextElement.children().isEmpty()) {
				Element childElement = speechTextElement.child(0);

				if (getPoliticianOfSpeech(childElement) != null) {
					return childElement;
				}
			}
			if (matchesSpeechBeginPattern(speechTextElement)) {
				break;
			}
		}

		logger.debug("no firstspeechTextElement found");
		return null;
	}

	protected boolean matchesSpeechBeginPattern(Element speechTextElement) {
		return SPEECH_BEGIN_PATTERN.matcher(speechTextElement.text().replaceAll(NBSP_STRING, " ").trim()).find();
	}

	@Override
	protected Elements getSpeechBeginElements(Document protocol) {
		Elements speechBeginAndEndElements = protocol.select("i:matches(^\\s*\\d{1,2}\\.\\d{2}\\s*$)");

		return speechBeginAndEndElements;
	}

}
