package at.jku.tk.hiesmair.gv.parliament.etl.period.transformer.session;

import org.jsoup.nodes.Document;

import at.jku.tk.hiesmair.gv.parliament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parliament.entities.session.Session;

public interface SessionTransformer {

	public Session getSession(LegislativePeriod period, String sessionTitle, Document index, Document protocol) throws Exception;
	
	public Integer getSessionTranformerFromPeriod();

	public Integer getSessionTranformerToPeriod();
}
