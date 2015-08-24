package at.jku.tk.hiesmair.gv.parlament.period.extractor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import at.jku.tk.hiesmair.gv.parlament.period.extractor.feed.ProtocolFeedItem;

public class LegislativePeriodExtractorTest {

	@Test
	public void testUrl() throws Exception {
		LegislativePeriodExtractor periodExtractor = new LegislativePeriodExtractor();
		String url = periodExtractor.getUrl(25);
		assertEquals(
				LegislativePeriodExtractor.RSS_FEED_URL.replaceAll(LegislativePeriodExtractor.PERIOD_PATTERN, "XXV"),
				url);
	}

	@Test
	public void test() throws Exception {
		LegislativePeriodExtractor periodExtractor = new LegislativePeriodExtractor();
		List<ProtocolFeedItem> items = periodExtractor.extractProtocols(25);
		assertTrue(items.size() > 0);
	}
}
