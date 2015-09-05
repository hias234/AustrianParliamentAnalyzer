package at.jku.tk.hiesmair.gv.parliament.etl.period.extractor.feed.parser;

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

import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import at.jku.tk.hiesmair.gv.parliament.etl.period.extractor.feed.ProtocolFeedItem;
import at.jku.tk.hiesmair.gv.parliament.etl.politician.extractor.feed.PoliticianFeedParser;
import at.jku.tk.hiesmair.gv.parliament.feed.parser.FeedParser;
import at.jku.tk.hiesmair.gv.parliament.feed.parser.title.TitleParser;

/**
 * Uses a DOM parser to analyse the feed
 * 
 * @author matthias
 */
public class LegislativePeriodFeedParser extends FeedParser<ProtocolFeedItem> {

	protected static final Logger logger = Logger
			.getLogger(PoliticianFeedParser.class.getSimpleName());

	public LegislativePeriodFeedParser(String raw, TitleParser titleParser) {
		super(raw, titleParser);
	}

	/**
	 * Parse a single item
	 * 
	 * @param item
	 * @throws DOMException
	 * @throws MalformedURLException
	 * @throws ParseException
	 */
	protected void parseItem(Node item) throws Exception {
		NodeList children = item.getChildNodes();
		ProtocolFeedItem p = new ProtocolFeedItem();
		for (int i = 0; i < children.getLength(); i++) {
			Node n = children.item(i);
			if ("title".equalsIgnoreCase(n.getNodeName())) {
				p.setTitle(this.titleParser.parse(n.getTextContent().trim()));
			} else if ("pubDate".equalsIgnoreCase(n.getNodeName())) {
				try {
					p.setPubDate(this.sdf.parse(n.getTextContent().trim()));
				} catch (ParseException pe) {
					logger.warn("pubDate not parsable: "
							+ n.getTextContent().trim() + " for format: "
							+ sdf.toPattern());
				}
			} else if ("link".equalsIgnoreCase(n.getNodeName())) {
				p.setUrl(new URL(n.getTextContent().trim()));
			} else if ("description".equalsIgnoreCase(n.getNodeName())) {
				p.setDescription(n.getTextContent());
			}
		}
		this.items.add(p);
	}

}
