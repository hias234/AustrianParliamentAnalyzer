package at.jku.tk.hiesmair.gv.parlament.politician.extractor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import at.jku.tk.hiesmair.gv.parlament.etl.politician.extractor.PoliticiansExtractor;
import at.jku.tk.hiesmair.gv.parlament.etl.politician.extractor.feed.PoliticianFeedItem;
import at.jku.tk.hiesmair.gv.parlament.etl.politician.extractor.feed.parser.PoliticianTitleParser;

public class PoliticianExtractorTest {

	@Test
	public void testTitle(){
		PoliticianTitleParser titleParser = new PoliticianTitleParser();
		String parsed = titleParser.parse("Dipl.-Ing., Name Name");
		assertEquals("DiplIngNameName", parsed);
	}

	@Test
	public void test() throws Exception {
		PoliticiansExtractor extractor = new PoliticiansExtractor();
		List<PoliticianFeedItem> items = extractor.getPoliticianFeedItems();
		assertTrue(items.size() > 0);
	}
}
