package at.jku.tk.hiesmair.gv.parlament.etl.period.transformer.session.discussion;

import org.junit.Test;

import at.jku.tk.hiesmair.gv.parlament.cache.DataCache;
import at.jku.tk.hiesmair.gv.parlament.cache.InMemoryDataCache;
import at.jku.tk.hiesmair.gv.parlament.etl.politician.transformer.PoliticianTransformer;

public class DiscussionTransformer22andUpTest {

	@Test
	public void testGetDiscussions(){
		DataCache cache = new InMemoryDataCache();
		PoliticianTransformer politicianTransformer = new PoliticianTransformer(cache);
		DiscussionTransformer22andUp discussionTransformer = new DiscussionTransformer22andUp(politicianTransformer);
	}
	
}
