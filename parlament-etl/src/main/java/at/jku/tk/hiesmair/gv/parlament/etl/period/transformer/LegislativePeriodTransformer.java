package at.jku.tk.hiesmair.gv.parlament.etl.period.transformer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import at.jku.tk.hiesmair.gv.parlament.cache.DataCache;
import at.jku.tk.hiesmair.gv.parlament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parlament.entities.session.Session;
import at.jku.tk.hiesmair.gv.parlament.etl.period.extractor.feed.ProtocolFeedItem;
import at.jku.tk.hiesmair.gv.parlament.etl.period.transformer.session.AbstractSessionTransformer;
import at.jku.tk.hiesmair.gv.parlament.etl.period.transformer.session.SessionTransformer21;
import at.jku.tk.hiesmair.gv.parlament.etl.period.transformer.session.SessionTransformer22andUp;

/**
 * Transforms the protocols of a period into a period object.
 * 
 * @author Markus
 *
 */
@Component
public class LegislativePeriodTransformer {

	protected SessionTransformer22andUp sessionTransformer22andUp;
	protected SessionTransformer21 sessionTransformer21;

	protected DataCache cache;

	@Inject
	public LegislativePeriodTransformer(DataCache cache) {
		this.cache = cache;
		this.sessionTransformer22andUp = new SessionTransformer22andUp(cache);
		this.sessionTransformer21 = new SessionTransformer21(cache);
	}

	public LegislativePeriod getLegislativePeriod(int period, List<ProtocolFeedItem> sessionProtocols) throws Exception {
		LegislativePeriod legislativePeriod = cache.getLegislativePeriod(period);
		if (legislativePeriod == null) {
			legislativePeriod = new LegislativePeriod(period);
			cache.putLegislativePeriod(legislativePeriod);
		}

		legislativePeriod.setSessions(getSessions(legislativePeriod, sessionProtocols));

		return legislativePeriod;
	}

	protected AbstractSessionTransformer getSessionTransformer(LegislativePeriod period) {
		if (period.getPeriod() <= 21) {
			return sessionTransformer21;
		}

		return sessionTransformer22andUp;
	}

	protected List<Session> getSessions(LegislativePeriod period, List<ProtocolFeedItem> sessionProtocols)
			throws Exception {
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

		AbstractSessionTransformer transformer = getSessionTransformer(period);
		return transformer.getSession(period, indexDoc, protocolDoc);
	}

}
