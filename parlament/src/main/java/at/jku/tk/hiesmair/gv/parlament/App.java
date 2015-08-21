package at.jku.tk.hiesmair.gv.parlament;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import at.jku.tk.hiesmair.gv.parlament.entities.Session;
import at.jku.tk.hiesmair.gv.parlament.extractor.feed.FeedParser;
import at.jku.tk.hiesmair.gv.parlament.extractor.feed.FeedReader;
import at.jku.tk.hiesmair.gv.parlament.extractor.feed.Protocol;
import at.jku.tk.hiesmair.gv.parlament.extractor.feed.parser.AustrianParlamentTitleParser;

/**
 * Main Application kicking off the analysis process
 */
public class App {
	
	/**
	 * Create a {@link FeedReader} download everything and analyse everything
	 * 
	 * @param args
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws ParseException 
	 * @throws ParserConfigurationException 
	 * @throws SAXException 
	 * @throws DOMException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException, DOMException, SAXException, ParserConfigurationException, ParseException {
		FeedReader reader = new FeedReader(25);
		FeedParser parser = new FeedParser(reader.getFeedContent(), new AustrianParlamentTitleParser());
		List<Protocol> protocols = parser.getProtocols();
		List<Session> sessions = new ArrayList<Session>(protocols.size());
		for(Protocol p:protocols) {
			System.out.println("Downloading and parsing protocol " + p.getTitle());
			Document document = p.getIndexDocument();
			if(document != null) {
				Elements select = document.select("h3");
				for(Element e : select) {
					System.out.println("Thema: " + e.text());
				}
			}
		}
	}
}
