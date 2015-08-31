package at.jku.tk.hiesmair.gv.parlament.etl.politician.extractor;

import java.util.List;

import org.springframework.stereotype.Component;

import at.jku.tk.hiesmair.gv.parlament.etl.politician.extractor.feed.PoliticianFeedItem;
import at.jku.tk.hiesmair.gv.parlament.etl.politician.extractor.feed.PoliticianFeedParser;
import at.jku.tk.hiesmair.gv.parlament.etl.politician.extractor.feed.parser.PoliticianTitleParser;
import at.jku.tk.hiesmair.gv.parlament.feed.FeedReader;

@Component
public class PoliticiansExtractor {

	/** URL the feed is sitting on */
	public static final String RSS_FEED_URL = "http://www.parlament.gv.at/WWER/PARL/filter.psp?view=RSS&jsMode=RSS&xdocumentUri=%2FWWER%2FPARL%2Findex.shtml&NRBR=ALLE&anwenden=Anwenden&GP=ALLE&R_WF=FR&FR=ALLE&W=W&M=M&listeId=8&FBEZ=FW_008";

	public List<PoliticianFeedItem> getPoliticianFeedItems() throws Exception {
		FeedReader reader = new FeedReader(RSS_FEED_URL);
		PoliticianFeedParser parser = new PoliticianFeedParser(reader.getFeedContent(), new PoliticianTitleParser());
		return parser.getItems();
	}

}
