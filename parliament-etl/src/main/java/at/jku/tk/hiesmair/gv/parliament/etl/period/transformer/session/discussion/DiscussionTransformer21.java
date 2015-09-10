package at.jku.tk.hiesmair.gv.parliament.etl.period.transformer.session.discussion;

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
		return speechPartElement.text();
	}

	@Override
	protected Element getFirstSpeechTextElement(Element speechBeginElement) {
		Element speechTextElement = speechBeginElement.nextElementSibling();
		if (speechTextElement != null) {
			speechTextElement = speechTextElement.nextElementSibling();
			if (speechTextElement != null) {
				speechTextElement = speechTextElement.nextElementSibling();
				if (speechTextElement != null && speechTextElement.children().size() > 0) {
					return speechTextElement.child(0);
				}
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
		Elements speechBeginAndEndElements = protocol.select("i:matches(^\\s*\\d{1,2}\\.\\d{2}\\s*$)");

		return speechBeginAndEndElements;
	}
}
