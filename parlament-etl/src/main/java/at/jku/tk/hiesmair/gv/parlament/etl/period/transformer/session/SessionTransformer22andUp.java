package at.jku.tk.hiesmair.gv.parlament.etl.period.transformer.session;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import at.jku.tk.hiesmair.gv.parlament.cache.DataCache;
import at.jku.tk.hiesmair.gv.parlament.entities.Politician;
import at.jku.tk.hiesmair.gv.parlament.entities.discussion.Discussion;

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
	protected List<Discussion> setSpeechTexts(Document protocol, List<Discussion> discussions) throws Exception {
		Elements speechBeginnings = getSpeechBeginTags(protocol);
		for (Element speechBegin : speechBeginnings) {
			Date time = getBeginTime(speechBegin);

			if (time != null) {
				Element speechPartElement = getFirstSpeechTextElement(speechBegin);

				if (speechPartElement != null) {
					Elements politicianLinks = getPoliticianLinks(speechPartElement);
					if (politicianLinks.size() > 0) {
						Politician politician = getPolitician(politicianLinks.get(0).attr("href"));
						String speechText = getSpeechText(speechPartElement);
						if (speechText != null) {
							setSpeechText(discussions, time, politician, speechText);
						}
						else {
							logger.info("no colon " + speechPartElement);
						}
					}
					else {
						logger.info("no link " + speechPartElement);
					}
				}
				else {
					logger.info("speechPart-Tag is null");
				}
			}
			else {
				logger.info("unable to parse start time");
			}
		}

		return discussions;
	}

	private String getSpeechText(Element speechPartElement) {
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

	protected Elements getSpeechBeginTags(Document protocol) {
		return protocol.select("p.RB:matches(\\d{1,2}\\.\\d{2}.*)");
	}
}
