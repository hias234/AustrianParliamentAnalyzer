package at.jku.tk.hiesmair.gv.parlament.politician.transformer;

import java.util.ArrayList;
import java.util.List;

import at.jku.tk.hiesmair.gv.parlament.entities.Politician;
import at.jku.tk.hiesmair.gv.parlament.politician.extractor.feed.PoliticianFeedItem;

public class PoliticiansTransformer {

	public List<Politician> getPoliticians(List<PoliticianFeedItem> feedItems){
		List<Politician> politicians = new ArrayList<Politician>();
		
		for (PoliticianFeedItem feedItem : feedItems){
			
		}
		
		return politicians;
	}
	
}
