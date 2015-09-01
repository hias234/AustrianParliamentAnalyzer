package at.jku.tk.hiesmair.gv.parlament.etl.period.transformer.session;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import at.jku.tk.hiesmair.gv.parlament.cache.DataCache;
import at.jku.tk.hiesmair.gv.parlament.entities.discussion.Discussion;

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
	protected List<Discussion> setSpeechTexts(Document protocol, List<Discussion> discussions) throws Exception {
		Elements speechBeginAndEndElements = getSpeechBeginAndEndTags(protocol);
		
		for (int i = 0; i < speechBeginAndEndElements.size(); i += 2){
			Element speechBeginElement = speechBeginAndEndElements.get(i);
			
			Date beginTime = getBeginTime(speechBeginElement);
			if (beginTime != null){
				Element speechPartElement = getSpeechPartElement(speechBeginElement);
				
			}
		}
		
		return discussions;
	}

	protected Element getSpeechPartElement(Element speechBeginElement) {
		return speechBeginElement.nextElementSibling()
				.nextElementSibling().nextElementSibling().child(0);
	}

	/**
	 * Get speech begin and end-elements that contain the time aka. 12.08
	 * @param protocol
	 * @return
	 */
	private Elements getSpeechBeginAndEndTags(Document protocol) {
		return protocol.select("i:matches(^\\d{1,2}\\.\\d{1,2}$)");
	}

}
