package at.jku.tk.hiesmair.gv.parlament.politician.transformer;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import at.jku.tk.hiesmair.gv.parlament.entities.Politician;
import at.jku.tk.hiesmair.gv.parlament.politician.extractor.PoliticiansExtractor;
import at.jku.tk.hiesmair.gv.parlament.politician.extractor.feed.PoliticianFeedItem;

public class PoliticiansTranformerTest {

	@Test
	public void testTransformer() throws Exception {
		List<PoliticianFeedItem> items = new PoliticiansExtractor().getPoliticianFeedItems();
		
		List<Politician> politicians = new PoliticiansTransformer().getPoliticians(items);
		assertTrue(politicians.size() > 0);
		
		politicians.forEach(p -> System.out.println(p));
		System.out.println(politicians.stream().filter(p -> p.getNationalCouncilPeriods().contains(23)).count());
	}
	
}
