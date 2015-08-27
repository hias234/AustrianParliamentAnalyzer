package at.jku.tk.hiesmair.gv.parlament.period.transformer;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import at.jku.tk.hiesmair.gv.parlament.cache.DataCache;
import at.jku.tk.hiesmair.gv.parlament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parlament.entities.session.Session;
import at.jku.tk.hiesmair.gv.parlament.period.extractor.feed.ProtocolFeedItem;

/**
 * Transforms the protocols of a period into a period object.
 * 
 * @author Markus
 *
 */
@Component
public class LegislativePeriodTransformer {

	protected SessionTransformer sessionTransformer;

	protected DataCache cache;

	public LegislativePeriodTransformer() {
		sessionTransformer = new SessionTransformer();
		cache = DataCache.getInstance();
	}

	public LegislativePeriod getLegislativePeriod(int period, List<ProtocolFeedItem> sessionProtocols) throws Exception {
		LegislativePeriod legislativePeriod = cache.getLegislativePeriod(period);
		if (legislativePeriod == null){
			legislativePeriod = new LegislativePeriod(period);
			cache.putLegislativePeriod(legislativePeriod);
		}

		legislativePeriod.setSessions(getSessions(legislativePeriod, sessionProtocols));

		return legislativePeriod;
	}

	protected List<Session> getSessions(LegislativePeriod period, List<ProtocolFeedItem> sessionProtocols) throws Exception {
		List<Session> sessions = new ArrayList<Session>(sessionProtocols.size());

		for (ProtocolFeedItem sessionProtocol : sessionProtocols) {
			Session session = getSession(period, sessionProtocol);
			if (session != null) {
				sessions.add(session);
			}
		}
		return sessions;
	}

	private Session getSession(LegislativePeriod period, ProtocolFeedItem sessionProtocol) throws Exception {
		Document indexDoc = null;
		Document protocolDoc = null;

		try {
			indexDoc = sessionProtocol.getIndexDocument();
			protocolDoc = sessionProtocol.getProtocolDocument();
		} catch (Exception e) {
		}

		if (indexDoc == null || protocolDoc == null) {
			return null;
		}

		return sessionTransformer.getSession(period, indexDoc, protocolDoc);
	}

}
