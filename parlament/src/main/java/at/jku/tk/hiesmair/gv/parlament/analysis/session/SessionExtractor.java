package at.jku.tk.hiesmair.gv.parlament.analysis.session;

import org.jsoup.nodes.Document;

import at.jku.tk.hiesmair.gv.parlament.entities.ParliamentData;
import at.jku.tk.hiesmair.gv.parlament.entities.Session;

/**
 * returns a session object from a given xml-document
 * 
 * @author Markus
 *
 */
public interface SessionExtractor {

	Session getSession(Document index, Document protocol, ParliamentData data);
	
}
