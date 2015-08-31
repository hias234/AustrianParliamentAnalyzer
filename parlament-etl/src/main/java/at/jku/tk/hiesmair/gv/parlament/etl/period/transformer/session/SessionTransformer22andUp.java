package at.jku.tk.hiesmair.gv.parlament.etl.period.transformer.session;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

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
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH.mm");
		int found = 0;

		Elements speechBeginnings = getSpeechBeginTags(protocol);
		for (Element speechBegin : speechBeginnings) {
			String timeStr = speechBegin.text().replace(NBSP_STRING, " ").trim();
			Matcher m = speechBeginPattern.matcher(timeStr);
			Date time = null;
			if (m.find()) {
				try {
					time = timeFormat.parse(m.group(0));
				} catch (ParseException pe) {
				}
			}

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

										found++;
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

		int speechCnt = 0;
		for (Discussion discussion : discussions) {
			speechCnt += discussion.getSpeeches().size();
		}

		if (speechCnt != found) {
			logger.warn("not all speechtexts found in protocol: found " + found + " of " + speechCnt);
		}
		
		return discussions;
	}

	protected Elements getSpeechBeginTags(Document protocol) {
		return protocol.select("p.RB:matches(\\d{2}\\.\\d{2}.*)");
	}
}
