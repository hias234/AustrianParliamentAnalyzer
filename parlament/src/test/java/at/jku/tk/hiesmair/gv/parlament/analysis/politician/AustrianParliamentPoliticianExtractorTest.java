package at.jku.tk.hiesmair.gv.parlament.analysis.politician;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import at.jku.tk.hiesmair.gv.parlament.entities.ClubMembership;
import at.jku.tk.hiesmair.gv.parlament.entities.ParliamentData;
import at.jku.tk.hiesmair.gv.parlament.entities.Politician;

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
	}

	@Test
	public void patterntest() {
		Pattern p = Pattern.compile("([^(,]*)\\([^)]*\\),? ?([^\\s]+)\\s(\\d+\\.\\d+\\.\\d{4})(?: . (\\d+\\.\\d+\\.\\d{4}))?");
		Matcher m = p.matcher("Abgeordneter zum Nationalrat (XXV. GP), SPÖ 29.10.2013 – 16.12.2013");
		if (m.find()) {
			String function = m.group(1);
			assertEquals("Abgeordneter zum Nationalrat", function.trim());
			assertEquals("SPÖ", m.group(2));
			assertEquals("29.10.2013", m.group(3));
			assertEquals("16.12.2013", m.group(4));
		}

		m = p.matcher("Abgeordnete zum Nationalrat (XXIII.–XXV. GP), FPÖ 30.10.2006 –");
		if (m.find()) {
			String function = m.group(1);
			assertEquals("Abgeordnete zum Nationalrat", function.trim());
			assertEquals("FPÖ", m.group(2));
			assertEquals("30.10.2006", m.group(3));
			assertNull(m.group(4));
		}
		
		m = p.matcher("Abgeordnete zum Nationalrat (XXV. GP), STRONACH 29.10.2013 –");
		if (m.find()) {
			String function = m.group(1);
			assertEquals("Abgeordnete zum Nationalrat", function.trim());
			assertEquals("STRONACH", m.group(2));
			assertEquals("29.10.2013", m.group(3));
			assertNull(m.group(4));
		}
		
		
	}
}
