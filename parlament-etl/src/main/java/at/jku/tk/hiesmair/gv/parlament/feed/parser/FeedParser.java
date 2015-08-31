package at.jku.tk.hiesmair.gv.parlament.feed.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
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

import at.jku.tk.hiesmair.gv.parlament.feed.parser.title.TitleParser;

/**
 * parses a single feedItem and returns a list of feedItems
 * @author Markus
 *
 * @param <T>
 */
public abstract class FeedParser<T extends FeedItem> {

	/** Date-format to parse XML files */
	public static final String DATE_FORMAT = "d MMM yyyy HH:mm:ss Z";

	/** A list of all HTML protocols */
	protected List<T> items;

	/** The raw XML document */
	protected String raw;

	/** Simple Date format */
	protected SimpleDateFormat sdf;

	/** Parser that converts titles */
	protected TitleParser titleParser;

	/**
	 * Parses a raw feed to document URLs
	 * 
	 * @param raw
	 */
	public FeedParser(String raw, TitleParser titleParser) {
		this.raw = raw;
		this.titleParser = titleParser;
		this.items = null;
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
		try{
		return DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(new ByteArrayInputStream(raw.getBytes()));
		}
		catch(SAXException | IOException | ParserConfigurationException ex){
			System.out.println(raw);
			ex.printStackTrace();
			throw ex;
		}
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
	private void loadItems() throws Exception {
		this.sdf = new SimpleDateFormat(DATE_FORMAT);
		Document doc = getDocument(this.raw);
		NodeList items = doc.getElementsByTagName("item");
		this.items = new ArrayList<T>();
		for (int i = 0; i < items.getLength(); i++) {
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
	protected abstract void parseItem(Node item) throws Exception;

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
	public List<T> getItems() throws Exception {
		if (this.items == null) {
			loadItems();
		}
		return this.items;
	}
}
