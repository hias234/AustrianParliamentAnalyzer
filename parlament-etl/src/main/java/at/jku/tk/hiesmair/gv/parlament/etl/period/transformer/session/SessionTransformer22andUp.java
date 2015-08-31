package at.jku.tk.hiesmair.gv.parlament.etl.period.transformer.session;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;

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

}
