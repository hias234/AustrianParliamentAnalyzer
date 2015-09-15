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
public class DiscussionTransformer21 extends AbstractDiscussionTransformer {

	private static final Logger logger = Logger.getLogger(DiscussionTransformer21.class.getSimpleName());

	@Inject
	public DiscussionTransformer21(PoliticianTransformer politicianTransformer, SentimentAnalyzer sentimentAnalyzer) {
		super(politicianTransformer, sentimentAnalyzer);
	}

	@Override
	protected String getSpeechText(Element speechPartElement) {
		String text = speechPartElement.parent().text().replaceAll(NBSP_STRING, " ");
		int indexOfColon = text.indexOf(":");
		if (indexOfColon == -1){
			return text;
		}
		return text.substring(indexOfColon + 1).trim();
	}

	@Override
	protected Element getFirstSpeechTextElement(Element speechBeginElement, Date date) throws IOException {
		Element speechTextElement = speechBeginElement.nextElementSibling();
		for (;speechTextElement != null; speechTextElement = speechTextElement.nextElementSibling()){
			if (!speechTextElement.children().isEmpty()){
				Element childElement = speechTextElement.child(0);
				
				if (getPoliticianOfSpeech(childElement, date) != null){
					return childElement;
				}
			}
			if (SPEECH_BEGIN_PATTERN.matcher(speechTextElement.text().replaceAll(NBSP_STRING, " ").trim()).find()){
				break;
			}
		}

		logger.debug("no firstspeechTextElement found");
		return null;
	}

	/**
	 * returns begin and end-tags, because if one begin or end-tag is not found,
	 * it would completely mess up the system.
	 */
	@Override
	protected Elements getSpeechBeginElements(Document protocol) {
		Elements speechBeginAndEndElements = protocol.select("i:matches(^[^\\d]{0,6}\\d{1,2}\\.\\d{2})");

		return speechBeginAndEndElements;
	}
}
