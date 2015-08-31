package at.jku.tk.hiesmair.gv.parlament.etl.period.extractor.feed.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import at.jku.tk.hiesmair.gv.parlament.etl.period.extractor.feed.ProtocolFeedItem;
import at.jku.tk.hiesmair.gv.parlament.feed.parser.title.TitleParser;

/**
 * Uses a DOM parser to analyse the feed
 * 
 * @author matthias
 */
public class LegislativePeriodFeedParser {

	/** Date-format to parse XML files */
	public static final String DATE_FORMAT = "d MMM yyyy HH:mm:ss Z";
	
	/** A list of all HTML protocols */
	private List<ProtocolFeedItem> protocols;
	
	/** The raw XML document */
	private String raw;
	
	/** Simple Date format */
	private SimpleDateFormat sdf;
	
	/** Parser that converts titles */
	private TitleParser titleParser;
	
	/**
	 * Parses a raw feed to document URLs
	 * 
	 * @param raw
	 */
	public LegislativePeriodFeedParser(String raw, TitleParser titleParser) {
		this.raw = raw;
		this.titleParser = titleParser;
		this.protocols = null;
	}

	/**
	 * Ask the DOM parser to read the document
	 * 
	 * @param raw
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	private Document getDocument(String raw) throws SAXException, IOException, ParserConfigurationException {
		return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(raw.getBytes()));
	}
	
	/**
	 * Load items from the document
	 * 
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParseException 
	 * @throws DOMException 
	 */
	private void loadItems() throws SAXException, IOException, ParserConfigurationException, DOMException, ParseException {
		this.sdf = new SimpleDateFormat(DATE_FORMAT);
		Document doc = getDocument(this.raw);
		NodeList items = doc.getElementsByTagName("item");
		this.protocols = new ArrayList<ProtocolFeedItem>();
		for(int i=0;i<items.getLength();i++) {
			parseItem(items.item(i));
		}
	}
	
	/**
	 * Parse a single item
	 * 
	 * @param item
	 * @throws DOMException 
	 * @throws MalformedURLException 
	 * @throws ParseException 
	 */
	private void parseItem(Node item) throws MalformedURLException, DOMException, ParseException {
		NodeList children = item.getChildNodes();
		ProtocolFeedItem p = new ProtocolFeedItem();
		for(int i=0;i<children.getLength();i++) {
			Node n = children.item(i);
			if("title".equalsIgnoreCase(n.getNodeName())) {
				p.setTitle(this.titleParser.parse(n.getTextContent().trim()));
			}else if("pubDate".equalsIgnoreCase(n.getNodeName())) {
				p.setPubDate(this.sdf.parse(n.getTextContent().trim()));
			}else if("link".equalsIgnoreCase(n.getNodeName())) {
				p.setUrl(new URL(n.getTextContent().trim()));
			}else if("description".equalsIgnoreCase(n.getNodeName())) {
				p.setDescription(n.getTextContent());
			} 
		}
		this.protocols.add(p);
	}

	/**
	 * Get the protocols from the XML feed
	 * 
	 * @return
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParseException 
	 * @throws DOMException 
	 */
	public List<ProtocolFeedItem> getProtocols() throws SAXException, IOException, ParserConfigurationException, DOMException, ParseException {
		if(this.protocols == null) {
			loadItems();
		}
		return this.protocols;
	}
	
}
