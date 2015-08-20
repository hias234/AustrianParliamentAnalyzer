package at.jku.tk.hiesmair.gv.parlament.analysis;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import at.jku.tk.hiesmair.gv.parlament.feed.Protocol;

/**
 * JSoup parsed document capable of analysing a protocol 
 * 
 * @author matthias
 *
 */
public class IndexDocument {

	/** The protocol POJO */
	private final Protocol protocol;
	
	/** parsed document */
	private Document document;
	
	/** Construct ProtocolDocument from Protocol */
	public IndexDocument(Protocol protocol) {
		this.protocol = protocol;
	}
	
	/**
	 * Parse the {@link Protocol} content and return a JSoup {@link Document}
	 * 
	 * @return
	 * @throws IOException 
	 */
	public Document parse() throws IOException {
		if(this.protocol.getFileContent() != null) {
			return Jsoup.parse(this.protocol.getFileContent());
		}else{
			return null;
		}
	}
	
	/** return document parse on demand 
	 * @throws IOException */
	public Document getDocument() throws IOException {
		if(this.document == null) {
			this.document = parse();
		}
		return this.document;
	}
	
}
