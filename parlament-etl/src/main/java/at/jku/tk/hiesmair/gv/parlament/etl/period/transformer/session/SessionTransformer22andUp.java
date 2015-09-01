package at.jku.tk.hiesmair.gv.parlament.etl.period.transformer.session;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import at.jku.tk.hiesmair.gv.parlament.cache.DataCache;

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
		Element speechPart = speechBegin.nextElementSibling();
		if (speechPart == null) {
			speechPart = speechBegin.parent().nextElementSibling().child(0);
		}
		if (speechPart != null) {
			for (; getPoliticianLinks(speechPart).isEmpty();) {
				Element nextSibling = speechPart.nextElementSibling();
				if (nextSibling == null) {
					speechPart = speechPart.parent().nextElementSibling().child(0);
				}
				else {
					speechPart = nextSibling;
				}
			}
		}
		return speechPart;
	}

	@Override
	protected Elements getSpeechBeginElements(Document protocol) {
		return protocol.select("p.RB:matches(^\\s*\\d{1,2}\\.\\d{2}.*)");
	}
}
