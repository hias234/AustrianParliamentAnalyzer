package at.jku.tk.hiesmair.gv.parlament.transformer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;

import at.jku.tk.hiesmair.gv.parlament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parlament.entities.ParliamentData;
import at.jku.tk.hiesmair.gv.parlament.entities.Session;
import at.jku.tk.hiesmair.gv.parlament.extractor.feed.Protocol;

public class LegislativePeriodTransformer {

	protected SessionTransformer sessionTransformer;
	
	public LegislativePeriodTransformer(){
		sessionTransformer = new SessionTransformer();
	}
	
	public LegislativePeriod getLegislativePeriod(int period, List<Protocol> sessionProtocols) throws IOException,
			InterruptedException {
		ParliamentData data = new ParliamentData();
		
		List<Session> sessions = getSessions(sessionProtocols, data);

		LegislativePeriod legislativePeriod = new LegislativePeriod();
		legislativePeriod.setPeriod(period);
		legislativePeriod.setSessions(sessions);
		return legislativePeriod;
	}

	protected List<Session> getSessions(List<Protocol> sessionProtocols, ParliamentData data) {
		List<Session> sessions = new ArrayList<Session>(sessionProtocols.size());
		
		for (Protocol sessionProtocol : sessionProtocols){
			Session session = getSession(sessionProtocol, data);
			sessions.add(session);
		}
		return sessions;
	}

	private Session getSession(Protocol sessionProtocol, ParliamentData data) {
		Document indexDoc = sessionProtocol.getIndexDocument();
		Document protocolDoc = sessionProtocol.getProtocolDocument();
		
		return sessionTransformer.getSession(indexDoc, protocolDoc, data);
	}

}
