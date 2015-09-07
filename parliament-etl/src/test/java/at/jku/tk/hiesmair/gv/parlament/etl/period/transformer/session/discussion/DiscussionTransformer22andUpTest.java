package at.jku.tk.hiesmair.gv.parlament.etl.period.transformer.session.discussion;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.jsoup.nodes.Document;
import org.junit.Test;

import at.jku.tk.hiesmair.gv.parlament.ParliamentTestUtil;
import at.jku.tk.hiesmair.gv.parliament.cache.DataCache;
import at.jku.tk.hiesmair.gv.parliament.cache.InMemoryDataCache;
import at.jku.tk.hiesmair.gv.parliament.entities.discussion.Discussion;
import at.jku.tk.hiesmair.gv.parliament.entities.session.Session;
import at.jku.tk.hiesmair.gv.parliament.etl.period.transformer.session.discussion.DiscussionTransformer22andUp;
import at.jku.tk.hiesmair.gv.parliament.etl.politician.transformer.PoliticianTransformer;
import at.jku.tk.hiesmair.gv.parliament.sentiment.DummySentimentAnalyzer;

public class DiscussionTransformer22andUpTest {

	@Test
	public void testGetDiscussions() throws Exception {
		DataCache cache = new InMemoryDataCache();
		PoliticianTransformer politicianTransformer = new PoliticianTransformer(cache, "http://www.parlament.gv.at");
		DiscussionTransformer22andUp discussionTransformer = new DiscussionTransformer22andUp(politicianTransformer,
				new DummySentimentAnalyzer());

		Document index = ParliamentTestUtil.getDocumentFromClasspath("period25\\index_25.html");
		Document protocol = ParliamentTestUtil.getDocumentFromClasspath("period25\\Protocol_25.html");

		Session session = new Session();

		List<Discussion> discussions = discussionTransformer.getDiscussions(index, protocol, session);
		assertEquals(15, discussions.size());
	}

}
