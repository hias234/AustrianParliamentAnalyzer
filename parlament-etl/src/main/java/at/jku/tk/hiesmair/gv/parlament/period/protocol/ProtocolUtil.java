package at.jku.tk.hiesmair.gv.parlament.period.protocol;

import org.jsoup.nodes.Document;

public class ProtocolUtil {

	public static Document filterPageBreaks(Document doc){
		doc.select("hr").remove();
		doc.select("span.threecol").remove();
		
		return doc;
	}
	
}
