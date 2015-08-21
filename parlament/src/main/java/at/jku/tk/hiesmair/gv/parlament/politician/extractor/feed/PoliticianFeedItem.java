package at.jku.tk.hiesmair.gv.parlament.politician.extractor.feed;

import at.jku.tk.hiesmair.gv.parlament.feed.parser.FeedItem;

public class PoliticianFeedItem extends FeedItem {

	protected static final String CACHE_PATH = "C:\\parlament\\politicians";
	protected static final String CACHE_PREFIX = "politician_";
	
	public PoliticianFeedItem() {
		super(CACHE_PATH, CACHE_PREFIX);
	}

}
