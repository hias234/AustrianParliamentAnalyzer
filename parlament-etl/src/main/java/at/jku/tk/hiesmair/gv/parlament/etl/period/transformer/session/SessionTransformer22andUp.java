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
import at.jku.tk.hiesmair.gv.parlament.entities.discussion.DiscussionSpeech;

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
				Element speechPart = speechBegin.nextElementSibling();
				if (speechPart == null) {
					speechPart = speechBegin.parent().nextElementSibling().child(0);
				}
				if (speechPart != null) {
					for (; speechPart.select("a[href]").isEmpty();) {
						Element nextSibling = speechPart.nextElementSibling();
						if (nextSibling == null) {
							speechPart = speechPart.parent().nextElementSibling().child(0);
						}
						else {
							speechPart = nextSibling;
						}
					}
					String firstText = speechPart.text();

					Elements links = getPoliticianLinks(speechPart);
					if (links.size() > 0) {
						Politician politician = getPolitician(links.get(0).attr("href"));
						int indexOfColon = firstText.indexOf(":");
						if (indexOfColon != -1) {

							String speechText = firstText.substring(indexOfColon + 1).trim();

							speechPart = speechPart.nextElementSibling();
							for (; speechPart != null && !speechPart.className().equals("RE"); speechPart = speechPart
									.nextElementSibling()) {
								speechText += "\n\n" + speechPart.text();
							}

							for (Discussion discussion : discussions) {
								for (DiscussionSpeech speech : discussion.getSpeeches()) {
									if (speech.getPolitician().equals(politician)
											&& isTimeForSpeechCorrect(time, speech) && speech.getText() == null) {
										speech.setText(speechText);
										break;
									}
								}
							}
						}
						else {
							logger.info("no colon " + firstText);
						}
					}
					else {
						logger.info("no link " + firstText);
					}
				}
			}
			else {
				logger.info("unable to parse start time");
			}
		}

		return discussions;
	}

	protected Elements getSpeechBeginTags(Document protocol) {
		return protocol.select("p.RB:matches(\\d{1,2}\\.\\d{2}.*)");
	}
}
