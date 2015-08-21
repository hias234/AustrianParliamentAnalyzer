package at.jku.tk.hiesmair.gv.parlament.extractor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import at.jku.tk.hiesmair.gv.parlament.extractor.feed.FeedParser;
import at.jku.tk.hiesmair.gv.parlament.extractor.feed.FeedReader;
import at.jku.tk.hiesmair.gv.parlament.extractor.feed.Protocol;
import at.jku.tk.hiesmair.gv.parlament.extractor.feed.parser.AustrianParlamentTitleParser;

public class LegislativePeriodExtractor {
	
	public List<Protocol> extractProtocols(int period) throws Exception {
		String feedContent = getFeedContent(period);
		FeedParser parser = new FeedParser(feedContent, new AustrianParlamentTitleParser());
		
		return parser.getProtocols();
	}

	protected String getFeedContent(int period) throws MalformedURLException, IOException, InterruptedException {
		FeedReader feedReader = new FeedReader(period);
		return feedReader.getFeedContent();
	}
	
}
