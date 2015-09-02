package at.jku.tk.hiesmair.gv.parlament.etl.period.transformer.session;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import at.jku.tk.hiesmair.gv.parlament.cache.DataCache;
import at.jku.tk.hiesmair.gv.parlament.entities.Politician;

public class SessionTransformer21 extends AbstractSessionTransformer {

	private static final Logger logger = Logger.getLogger(SessionTransformer21.class.getSimpleName());

	public SessionTransformer21(DataCache cache) {
		super(cache);
	}

	@Override
	protected Document filterPageBreaks(Document protocol) {
		protocol.select("hr + table:matches(^Nationalrat, )").remove();
		protocol.select("hr").remove();

		return protocol;
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
		Elements politicianLinks = getPoliticianLinks(firstSpeechTextElement);
		if (!politicianLinks.isEmpty()){
			return getPolitician(politicianLinks.get(0).attr("href"));
		}
		return null;
	}

}
