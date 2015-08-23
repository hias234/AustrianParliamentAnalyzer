package at.jku.tk.hiesmair.gv.parlament.politician.transformer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import at.jku.tk.hiesmair.gv.parlament.entities.Politician;
import at.jku.tk.hiesmair.gv.parlament.politician.extractor.feed.PoliticianFeedItem;

public class PoliticiansTransformer {

	private PoliticianTransformer politicianTransformer;

	public PoliticiansTransformer() {
		this.politicianTransformer = new PoliticianTransformer();
	}

	public List<Politician> getPoliticians(List<PoliticianFeedItem> feedItems) throws IOException {
		Set<Politician> politicians = new HashSet<Politician>();

		for (PoliticianFeedItem feedItem : feedItems) {
			Politician politician = politicianTransformer.getPolitician(feedItem);
			politicians.add(politician);
		}

		return new ArrayList<Politician>(politicians);
	}

}
