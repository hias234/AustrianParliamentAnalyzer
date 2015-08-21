package at.jku.tk.hiesmair.gv.parlament.politician.extractor;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import at.jku.tk.hiesmair.gv.parlament.politician.extractor.feed.PoliticianFeedItem;

public class PoliticianExtractorTest {


	@Test
	public void test() throws Exception {
		PoliticianExtractor extractor = new PoliticianExtractor();
		List<PoliticianFeedItem> items = extractor.getPoliticianFeedItems();
		assertTrue(items.size() > 0);
	}
}
