package at.jku.tk.hiesmair.gv.parlament.feed;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import at.jku.tk.hiesmair.gv.parlament.feed.parser.AustrianParlamentTitleParser;

/**
 * Test retrieval and parsing of feeds
 * 
 * @author matthias
 */
public class FeedTest {
	
	
	
	@Test
	public void testURL() throws MalformedURLException {
		System.out.println("testUrl");
		System.out.println("------------------------------------------------------------------------");
		
		FeedReader reader = new FeedReader(25);
		assertEquals(FeedReader.RSS_FEED_URL.replaceAll(FeedReader.PERIOD_PATTERN, "XXV"), reader.getUrl().toString());
		System.out.println(reader.getUrl().toString());
		
		System.out.println("------------------------------------------------------------------------");
	}

	@Test
	public void testFeedRetrieval() throws IOException, InterruptedException {
		System.out.println("testFeedRetrieval");
		System.out.println("------------------------------------------------------------------------");
		
		FeedReader reader = new FeedReader(25);
		String content = reader.getFeedContent();
		assertTrue(content != null);
		assertTrue(content.length() > 0);
		System.out.println(content);
		
		System.out.println("------------------------------------------------------------------------");
	}
	
	@Test
	public void testFeedParsing() throws IOException, InterruptedException, SAXException, ParserConfigurationException, DOMException, ParseException {
		System.out.println("testFeedParsing");
		System.out.println("------------------------------------------------------------------------");
		
		FeedReader reader = new FeedReader(25);
		FeedParser parser = new FeedParser(reader.getFeedContent(), new AustrianParlamentTitleParser());
		List<Protocol> protocols = parser.getProtocols();
		assertNotNull(protocols);
		assertTrue(protocols.size() > 0);
		
		for (Protocol p : protocols){
			System.out.println(p);
		}
		System.out.println("------------------------------------------------------------------------");
	}
	
	@Test
	public void testFileDownload() throws IOException, InterruptedException, DOMException, SAXException, ParserConfigurationException, ParseException {
		System.out.println("testFileDownload");
		System.out.println("------------------------------------------------------------------------");
		
		FeedReader reader = new FeedReader(25);
		FeedParser parser = new FeedParser(reader.getFeedContent(), new AustrianParlamentTitleParser());
		List<Protocol> protocols = parser.getProtocols();
		assertNotNull(protocols);
		assertTrue(protocols.size() > 0);
		Protocol p = protocols.get(0);
		System.out.println(p);
		assertTrue(p.getFileContent().trim().length() > 0);
		
		System.out.println("------------------------------------------------------------------------");
	}
	
}
