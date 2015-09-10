package at.jku.tk.hiesmair.gv.parliament.etl.period.transformer.session.discussion;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;
import at.jku.tk.hiesmair.gv.parliament.etl.politician.transformer.PoliticianTransformer;
import at.jku.tk.hiesmair.gv.parliament.sentiment.SentimentAnalyzer;

@Component
public class DiscussionTransformer21 extends AbstractDiscussionTransformer {

	private static final Logger logger = Logger.getLogger(DiscussionTransformer21.class.getSimpleName());

	private static final Pattern POLITICIAN_NAME_PATTERN = Pattern
			.compile("^(Abgeordneter )((?:[\\wöäüÖÄÜß]+\\..?(?:\\(FH\\))?)*\\s)?((?:[\\wöäüÖÄÜß,-\\.]+(?:\\s.\\.)?\\s?)+)\\s([^\\s,(\\.:]+)");

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

	@Override
	protected Politician getPoliticianOfSpeech(Element firstSpeechTextElement) throws Exception {
		Elements politicianLinks = politicianTransformer.getPoliticianLinks(firstSpeechTextElement);
		if (!politicianLinks.isEmpty()) {
			return politicianTransformer.getPolitician(politicianLinks.get(0).attr("href"));
		}
		else {
			String text = firstSpeechTextElement.text().replaceAll(NBSP_STRING, " ").replaceAll(EN_DASH_STRING, "-");
			String[] parts = text.split(":");
			if (parts.length > 1){
				String namePart = parts[0];
				Matcher m = POLITICIAN_NAME_PATTERN.matcher(namePart);
				if (m.find()){
					String title = m.group(2);
					String firstName = m.group(3);
					String surName = m.group(4);
					
					return politicianTransformer.getPoliticianByName(title, firstName, surName);
				}
			}
		}
		return null;
	}
}
