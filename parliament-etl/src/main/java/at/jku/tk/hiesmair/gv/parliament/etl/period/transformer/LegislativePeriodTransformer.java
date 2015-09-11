package at.jku.tk.hiesmair.gv.parliament.etl.period.transformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import at.jku.tk.hiesmair.gv.parliament.cache.DataCache;
import at.jku.tk.hiesmair.gv.parliament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parliament.entities.session.Session;
import at.jku.tk.hiesmair.gv.parliament.etl.period.extractor.feed.ProtocolFeedItem;
import at.jku.tk.hiesmair.gv.parliament.etl.period.transformer.session.SessionTransformer;

/**
 * Transforms the protocols of a period into a period object.
 * 
 * @author Markus
 *
 */
@Component
public class LegislativePeriodTransformer {

	protected static final Logger logger = Logger.getLogger(LegislativePeriodTransformer.class.getSimpleName());

	protected final Collection<SessionTransformer> sessionTransformers;

	protected final DataCache cache;

	@Inject
	public LegislativePeriodTransformer(DataCache cache, Collection<SessionTransformer> sessionTransformers) {
		this.cache = cache;
		this.sessionTransformers = sessionTransformers;
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

	protected SessionTransformer getSessionTransformer(LegislativePeriod period) {
		Optional<SessionTransformer> sessionTransformer = sessionTransformers.stream().filter(
				st -> st.getSessionTranformerFromPeriod() <= period.getPeriod()
						&& st.getSessionTranformerToPeriod() >= period.getPeriod()).findFirst();
		
		return sessionTransformer.get();
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

		SessionTransformer transformer = getSessionTransformer(period);
		return transformer.getSession(period, sessionProtocol.getTitle(), indexDoc, protocolDoc);
	}

}
