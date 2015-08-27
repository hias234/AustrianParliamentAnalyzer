package at.jku.tk.hiesmair.gv.parlament.politician.transformer;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import at.jku.tk.hiesmair.gv.parlament.entities.Politician;
import at.jku.tk.hiesmair.gv.parlament.politician.extractor.PoliticiansExtractor;
import at.jku.tk.hiesmair.gv.parlament.politician.extractor.feed.PoliticianFeedItem;

public class PoliticiansTranformerTest {

	@Test
	public void testTransformer() throws Exception {
		List<PoliticianFeedItem> items = new PoliticiansExtractor().getPoliticianFeedItems();
		
		List<Politician> politicians = new PoliticiansTransformer().getPoliticians(items);
		assertTrue(politicians.size() > 0);

		StringWriter writer = new StringWriter();
		politicians.stream().filter(p -> p.isInNationalCouncilAt(new Date())).forEach(p -> writer.write(p.toString() + "\n"));
		FileUtils.writeStringToFile(new File("C:\\Temp\\pol.txt"), writer.toString());
	}
	
}