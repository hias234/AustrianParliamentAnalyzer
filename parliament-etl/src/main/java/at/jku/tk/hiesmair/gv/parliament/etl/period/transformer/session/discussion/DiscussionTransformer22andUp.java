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
public class DiscussionTransformer22andUp extends AbstractDiscussionTransformer {

	private static final Logger logger = Logger.getLogger(DiscussionTransformer22andUp.class.getSimpleName());

	protected static final String SPEECH_END_CLASSNAME = "RE";
	
	@Inject
	public DiscussionTransformer22andUp(PoliticianTransformer politicianTransformer, SentimentAnalyzer sentimentAnalyzer) {
		super(politicianTransformer, sentimentAnalyzer);
	}

	@Override
	protected String getSpeechText(Element speechPartElement) {
		String speechText = null;
		String firstText = speechPartElement.text();
		int indexOfColon = firstText.indexOf(":");
		if (indexOfColon != -1) {

			speechText = firstText.substring(indexOfColon + 1).trim();

			speechPartElement = speechPartElement.nextElementSibling();
			for (; speechPartElement != null && !speechPartElement.className().equals(SPEECH_END_CLASSNAME); speechPartElement = speechPartElement
					.nextElementSibling()) {
				speechText += "\n\n" + speechPartElement.text();
			}
		}
		return speechText;
	}

	@Override
	protected Element getFirstSpeechTextElement(Element speechBegin, Date date) throws IOException {
		Element speechPart = getNextPossibleStartTextElement(speechBegin);
		while (speechPart != null && !speechPart.className().equals(SPEECH_END_CLASSNAME)) {
			if (getPoliticianOfSpeech(speechPart, date) != null) {
				return speechPart;
			}

			speechPart = getNextPossibleStartTextElement(speechPart);
		}

		if (speechPart == null || speechPart.className().equals(SPEECH_END_CLASSNAME)) {
			logger.debug("did not find first speechText-Element -> reached SpeechEndElement");
			return null;
		}
		return speechPart;
	}

	protected Element getNextPossibleStartTextElement(Element speechPart) {
		Element nextSibling = speechPart.nextElementSibling();
		if (nextSibling != null) {
			speechPart = nextSibling;
		}
		else {
			speechPart = speechPart.parent().nextElementSibling();
			while (speechPart != null && speechPart.children().isEmpty()) {
				speechPart = speechPart.nextElementSibling();
			}
			if (speechPart != null) {
				speechPart = speechPart.child(0);
			}
		}
		return speechPart;
	}

	@Override
	protected Elements getSpeechBeginElements(Document protocol) {
		return protocol.select("p:matches(^[^\\d]{0,6}\\d{1,2}\\.\\d{2})");
	}

}
