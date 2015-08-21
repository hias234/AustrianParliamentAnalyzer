package at.jku.tk.hiesmair.gv.parlament.period.transformer;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;

import at.jku.tk.hiesmair.gv.parlament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parlament.entities.ParliamentData;
import at.jku.tk.hiesmair.gv.parlament.entities.Session;
import at.jku.tk.hiesmair.gv.parlament.period.extractor.feed.ProtocolFeedItem;

/**
 * Transforms the protocols of a period into a period object.
 * 
 * @author Markus
 *
 */
public class LegislativePeriodTransformer {

	protected SessionTransformer sessionTransformer;

	public LegislativePeriodTransformer() {
		sessionTransformer = new SessionTransformer();
	}

	public LegislativePeriod getLegislativePeriod(int period, List<ProtocolFeedItem> sessionProtocols) throws Exception {
		ParliamentData data = new ParliamentData();

		List<Session> sessions = getSessions(sessionProtocols, data);

		LegislativePeriod legislativePeriod = new LegislativePeriod();
		legislativePeriod.setPeriod(period);
		legislativePeriod.setSessions(sessions);
		return legislativePeriod;
	}

	protected List<Session> getSessions(List<ProtocolFeedItem> sessionProtocols, ParliamentData data) throws Exception {
		List<Session> sessions = new ArrayList<Session>(sessionProtocols.size());

		for (ProtocolFeedItem sessionProtocol : sessionProtocols) {
			Session session = getSession(sessionProtocol, data);
			if (session != null) {
				sessions.add(session);
			}
		}
		return sessions;
	}

	private Session getSession(ProtocolFeedItem sessionProtocol, ParliamentData data) throws Exception {
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

		return sessionTransformer.getSession(indexDoc, protocolDoc, data);
	}

}
