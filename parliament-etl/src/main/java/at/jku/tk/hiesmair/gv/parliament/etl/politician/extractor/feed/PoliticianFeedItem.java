package at.jku.tk.hiesmair.gv.parliament.etl.politician.extractor.feed;

import at.jku.tk.hiesmair.gv.parliament.feed.parser.FeedItem;

public class PoliticianFeedItem extends FeedItem {

	protected static final String CACHE_SPECIFIC_DIRECTORY = ".parliamentdata\\politicians";
	protected static final String CACHE_PREFIX = "politician_";
	
	public PoliticianFeedItem() {
		super(CACHE_SPECIFIC_DIRECTORY, CACHE_PREFIX);
	}

}
