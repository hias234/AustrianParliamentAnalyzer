package at.jku.tk.hiesmair.gv.parlament.analysis.politician;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import at.jku.tk.hiesmair.gv.parlament.entities.ParliamentData;
import at.jku.tk.hiesmair.gv.parlament.entities.Politician;
import at.jku.tk.hiesmair.gv.parlament.entities.club.ClubMembership;

public class AustrianParliamentPoliticianExtractorTest {

	@Test
	public void testGetPolitician() {
		ParliamentData data = new ParliamentData();
		
		AustrianParliamentPoliticianExtractor extractor = new AustrianParliamentPoliticianExtractor();
		Politician p = extractor.getPolitician("http://www.parlament.gv.at/WWER/PAD_35468/index.shtml", data);

		assertEquals("Dr.", p.getTitle());
		assertEquals("Dagmar", p.getFirstName());
		assertEquals("Belakowitsch-Jenewein", p.getSurName());
		assertEquals(1, p.getClubMemberships().size());
		
		ClubMembership membership = p.getClubMemberships().stream().findFirst().get();
		assertEquals("Abgeordnete zum Nationalrat", membership.getFunction());
		assertEquals("http://www.parlament.gv.at/WWER/PAD_35468/index.shtml", membership.getPolitician().getId());
		assertEquals("FPÖ", membership.getClub().getShortName());
		
		assertEquals(1, data.getPoliticians().size());
		assertTrue(data.getClubs().size() >= 1);
		
		p = extractor.getPolitician("http://www.parlament.gv.at//WWER/PAD_61659/index.shtml", data);

		assertEquals("Josef A.", p.getFirstName());
		assertEquals("Riemer", p.getSurName());
		assertEquals(1, p.getClubMemberships().size());
		
	}

	
}
