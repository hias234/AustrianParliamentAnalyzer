package at.jku.tk.hiesmair.gv.parlament.etl.period.transformer.session;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;

import at.jku.tk.hiesmair.gv.parlament.cache.DataCache;

public class SessionTransformer21 extends AbstractSessionTransformer {

	private static final Logger logger = Logger.getLogger(SessionTransformer21.class.getSimpleName());
	
	public SessionTransformer21(DataCache cache) {
		super(cache);
	}

	@Override
	protected Document filterPageBreaks(Document protocol) {
		int pagebreaktables = protocol.select("hr + table:matches(^Nationalrat, )").remove().size();
		if (pagebreaktables == 0){
			logger.error("no pagebreaks found");
		}
		protocol.select("hr").remove();
		
		return protocol;
	}

}
