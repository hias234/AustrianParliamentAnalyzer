package at.jku.tk.hiesmair.gv.parlament.etl.period.extractor;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import at.jku.tk.hiesmair.gv.parliament.etl.period.extractor.LegislativePeriodExtractor;
import at.jku.tk.hiesmair.gv.parliament.etl.period.extractor.feed.ProtocolFeedItem;

public class LegislativePeriodExtractorTest {

	@Test
	public void test() throws Exception {
		LegislativePeriodExtractor periodExtractor = new LegislativePeriodExtractor();
		List<ProtocolFeedItem> items = periodExtractor.extractProtocols(25);
		assertTrue(items.size() > 0);
	}
}
