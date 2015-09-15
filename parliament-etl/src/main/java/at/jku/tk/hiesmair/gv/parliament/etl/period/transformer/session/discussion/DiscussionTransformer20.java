package at.jku.tk.hiesmair.gv.parliament.etl.period.transformer.session.discussion;

import java.io.IOException;
import java.util.Date;

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
		if (isSpeechTextVersion2(speechPartElement)) {
			return getSpeechTextVersion2(speechPartElement);
		}

		return getSpeechTextVersion1(speechPartElement);
	}

	protected boolean isSpeechTextVersion2(Element speechPartElement) {
		return speechPartElement.parent().tagName().equals("body");
	}

	protected String getSpeechTextVersion1(Element speechPartElement) {
		String text = speechPartElement.parent().text().replaceAll(NBSP_STRING, " ");
		int indexOfColon = text.indexOf(":");
		if (indexOfColon == -1) {
			return text;
		}
		return text.substring(indexOfColon + 1).trim();
	}

	private String getSpeechTextVersion2(Element speechPartElement) {
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

	@Override
	protected Element getFirstSpeechTextElement(Element speechBeginElement, Date date) throws IOException {
		if (isVersion2(speechBeginElement)) {
			return getFirstSpeechTextElementVersion2(speechBeginElement, date);
		}

		return getFirstSpeechTextElementVersion1(speechBeginElement, date);
	}

	protected Element getFirstSpeechTextElementVersion1(Element speechBeginElement, Date date) throws IOException {
		Element speechTextElement = speechBeginElement.nextElementSibling();
		for (; speechTextElement != null; speechTextElement = speechTextElement.nextElementSibling()) {
			if (!speechTextElement.children().isEmpty()) {
				Element childElement = speechTextElement.child(0);

				if (getPoliticianOfSpeech(childElement, date) != null) {
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

	protected Element getFirstSpeechTextElementVersion2(Element speechBeginElement, Date date) throws IOException {
		Element speechTextElement = speechBeginElement.parent();
		for (int i = 0; speechTextElement != null; i++, speechTextElement = speechTextElement.nextElementSibling()) {
			if (getPoliticianOfSpeech(speechTextElement, date) != null) {
				return speechTextElement;
			}
			if (i > 0 && matchesSpeechBeginPattern(speechTextElement)) {
				break;
			}
		}

		return null;
	}

	protected boolean isVersion2(Element speechBeginElement) {
		return speechBeginElement.parent().tagName().equals("p");
	}

	protected boolean matchesSpeechBeginPattern(Element speechTextElement) {
		return SPEECH_BEGIN_PATTERN.matcher(speechTextElement.text().replaceAll(NBSP_STRING, " ").trim()).find();
	}

	@Override
	protected Elements getSpeechBeginElements(Document protocol) {
		Elements speechBeginAndEndElements = protocol.select("i:matches(^[^\\d]{0,6}\\d{1,2}[^\\d]\\d\\s{0,5}\\d)");

		return speechBeginAndEndElements;
	}

}
