package at.jku.tk.hiesmair.gv.parlament.etl.politician.extractor.feed;

import java.net.URL;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import at.jku.tk.hiesmair.gv.parlament.feed.parser.FeedParser;
import at.jku.tk.hiesmair.gv.parlament.feed.parser.title.TitleParser;

public class PoliticianFeedParser extends FeedParser<PoliticianFeedItem> {

	public PoliticianFeedParser(String raw, TitleParser titleParser) {
		super(raw, titleParser);
	}

	@Override
	protected void parseItem(Node item) throws Exception {
		NodeList children = item.getChildNodes();
		PoliticianFeedItem feedItem = new PoliticianFeedItem();
		for (int i = 0; i < children.getLength(); i++) {
			Node n = children.item(i);
			if ("title".equalsIgnoreCase(n.getNodeName())) {
				feedItem.setTitle(this.titleParser.parse(n.getTextContent().trim()));
			} else if ("pubDate".equalsIgnoreCase(n.getNodeName())) {
				feedItem.setPubDate(this.sdf.parse(n.getTextContent().trim()));
			} else if ("link".equalsIgnoreCase(n.getNodeName())) {
				feedItem.setUrl(new URL(n.getTextContent().trim()));
			} else if ("description".equalsIgnoreCase(n.getNodeName())) {
				feedItem.setDescription(n.getTextContent());
			}
		}
		this.items.add(feedItem);
	}

}
