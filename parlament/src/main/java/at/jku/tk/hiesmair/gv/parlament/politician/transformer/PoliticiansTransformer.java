package at.jku.tk.hiesmair.gv.parlament.politician.transformer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import at.jku.tk.hiesmair.gv.parlament.entities.ParliamentData;
import at.jku.tk.hiesmair.gv.parlament.entities.Politician;
import at.jku.tk.hiesmair.gv.parlament.politician.extractor.feed.PoliticianFeedItem;

public class PoliticiansTransformer {

	private PoliticianTransformer politicianTransformer;

	public PoliticiansTransformer() {
		this.politicianTransformer = new PoliticianTransformer();
	}

	public List<Politician> getPoliticians(List<PoliticianFeedItem> feedItems) throws IOException {
		ParliamentData data = new ParliamentData();

		List<Politician> politicians = new ArrayList<Politician>();

		for (PoliticianFeedItem feedItem : feedItems) {
			Politician politician = politicianTransformer.getPolitician(data, feedItem);

			politicians.add(politician);
		}

		return politicians;
	}

}
