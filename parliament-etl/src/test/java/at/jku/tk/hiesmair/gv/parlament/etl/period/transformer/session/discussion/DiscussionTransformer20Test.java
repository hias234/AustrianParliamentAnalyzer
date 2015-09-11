package at.jku.tk.hiesmair.gv.parlament.etl.period.transformer.session.discussion;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.List;

import org.jsoup.nodes.Document;
import org.junit.Test;

import at.jku.tk.hiesmair.gv.parlament.ParliamentTestUtil;
import at.jku.tk.hiesmair.gv.parliament.cache.DataCache;
import at.jku.tk.hiesmair.gv.parliament.cache.InMemoryDataCache;
import at.jku.tk.hiesmair.gv.parliament.entities.discussion.Discussion;
import at.jku.tk.hiesmair.gv.parliament.entities.session.Session;
import at.jku.tk.hiesmair.gv.parliament.etl.period.transformer.session.discussion.DiscussionTransformer20;
import at.jku.tk.hiesmair.gv.parliament.etl.politician.transformer.PoliticianTransformer;
import at.jku.tk.hiesmair.gv.parliament.sentiment.DummySentimentAnalyzer;

public class DiscussionTransformer20Test {

	@Test
	public void testGetDiscussions() throws Exception {
		DataCache cache = new InMemoryDataCache();
		PoliticianTransformer politicianTransformer = new PoliticianTransformer(cache, "http://www.parlament.gv.at");
		DiscussionTransformer20 discussionTransformer = new DiscussionTransformer20(politicianTransformer,
				new DummySentimentAnalyzer());

		Document index = ParliamentTestUtil.getDocumentFromClasspath("period20\\index_116.html");
		Document protocol = ParliamentTestUtil.getDocumentFromClasspath("period20\\protocol_116.html");

		Session session = new Session();
		session.setStartDate(new SimpleDateFormat("dd.MM.yyyy").parse("17.04.1998"));

		List<Discussion> discussions = discussionTransformer.getDiscussions(index, protocol, session);
		assertEquals(7, discussions.size());
	}

	
}
