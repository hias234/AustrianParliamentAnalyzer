package at.jku.tk.hiesmair.gv.parlament.etl.period.transformer.session;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import at.jku.tk.hiesmair.gv.parlament.cache.DataCache;
import at.jku.tk.hiesmair.gv.parlament.entities.politician.Politician;

/**
 * Transforms the Protocols of a Session into a session object
 * 
 * @author Markus
 *
 */
public class SessionTransformer22andUp extends AbstractSessionTransformer {

	private static final Logger logger = Logger.getLogger(SessionTransformer22andUp.class.getSimpleName());

	public SessionTransformer22andUp(DataCache cache) {
		super(cache);
	}

	@Override
	protected Document filterPageBreaks(Document protocol) {
		protocol.select("hr").remove();
		protocol.select("span.threecol").remove();

		return protocol;
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
			if (!getPoliticianLinks(speechPart).isEmpty()){
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
		Elements politicianLinks = getPoliticianLinks(firstSpeechTextElement);
		if (!politicianLinks.isEmpty()){
			return getPolitician(politicianLinks.get(0).attr("href"));
		}
		return null;
	}
}
