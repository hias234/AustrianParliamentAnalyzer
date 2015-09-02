package at.jku.tk.hiesmair.gv.parlament.etl.politician.transformer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import at.jku.tk.hiesmair.gv.parlament.entities.Politician;
import at.jku.tk.hiesmair.gv.parlament.etl.politician.extractor.feed.PoliticianFeedItem;

@Component
public class PoliticiansTransformer {

	private PoliticianTransformer politicianTransformer;

	@Inject
	public PoliticiansTransformer(PoliticianTransformer politicianTransformer) {
		this.politicianTransformer = politicianTransformer;
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
