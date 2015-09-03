package at.jku.tk.hiesmair.gv.parlament.etl.period.transformer.session.discussion;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import at.jku.tk.hiesmair.gv.parlament.entities.politician.Politician;
import at.jku.tk.hiesmair.gv.parlament.etl.politician.transformer.PoliticianTransformer;

@Component
public class DiscussionTransformer22andUp extends AbstractDiscussionTransformer {

	private static final Logger logger = Logger.getLogger(DiscussionTransformer22andUp.class.getSimpleName());
	
	@Inject
	public DiscussionTransformer22andUp(PoliticianTransformer politicianTransformer) {
		super(politicianTransformer);
	}
	
	@Override
	protected String getSpeechText(Element speechPartElement) {
		String speechText = null;
		String firstText = speechPartElement.text();
		int indexOfColon = firstText.indexOf(":");
		if (indexOfColon != -1) {

			speechText = firstText.substring(indexOfColon + 1).trim();

			speechPartElement = speechPartElement.nextElementSibling();
			for (; speechPartElement != null && !speechPartElement.className().equals("RE"); speechPartElement = speechPartElement
					.nextElementSibling()) {
				speechText += "\n\n" + speechPartElement.text();
			}
		}
		return speechText;
	}

	@Override
	protected Element getFirstSpeechTextElement(Element speechBegin) {
		Element speechPart = getNextPossibleStartTextElement(speechBegin);
		while (speechPart != null && !speechPart.className().equals("RE")){
			if (!politicianTransformer.getPoliticianLinks(speechPart).isEmpty()){
				return speechPart;
			}
			
			speechPart = getNextPossibleStartTextElement(speechPart);
		}
	
		if (speechPart == null || speechPart.className().equals("RE")){
			logger.debug("did not find first speechText-Element -> reached SpeechEndElement");
			return null;
		}
		return speechPart;
	}

	protected Element getNextPossibleStartTextElement(Element speechPart) {
		Element nextSibling = speechPart.nextElementSibling();
		if (nextSibling != null){
			speechPart = nextSibling;
		}
		else {
			speechPart = speechPart.parent().nextElementSibling();
			while (speechPart != null && speechPart.children().isEmpty()){
				speechPart = speechPart.nextElementSibling();
			}
			if (speechPart != null){
				speechPart = speechPart.child(0);
			}
		}
		return speechPart;
	}

	@Override
	protected Elements getSpeechBeginElements(Document protocol) {
		return protocol.select("p:matches(^\\s*\\d{1,2}\\.\\d{2})");
	}

	@Override
	protected Politician getPoliticianOfSpeech(Element firstSpeechTextElement) throws Exception {
		Elements politicianLinks = politicianTransformer.getPoliticianLinks(firstSpeechTextElement);
		if (!politicianLinks.isEmpty()){
			return politicianTransformer.getPolitician(politicianLinks.get(0).attr("href"));
		}
		return null;
	}

}
