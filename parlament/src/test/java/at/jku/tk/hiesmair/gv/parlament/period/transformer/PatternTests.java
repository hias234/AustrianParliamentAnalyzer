package at.jku.tk.hiesmair.gv.parlament.period.transformer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class PatternTests {
	
	@Test
	public void testDiscussionPatterns(){
		Pattern discussionTypePattern = Pattern.compile("Einzelredezeitbeschränkung:\\s+((?:\\d+)|.)\\s+min\\s+(.+)");
		Matcher m = discussionTypePattern.matcher(" Einzelredezeitbeschränkung: 20 min Normaldebatte ");
		if (m.find()){
			assertEquals("Normaldebatte", m.group(2).trim());
		}
		else{
			fail();
		}
		
		m = discussionTypePattern.matcher(" Einzelredezeitbeschränkung:  - min Normaldebatte ");
		if (m.find()){
			assertEquals("Normaldebatte", m.group(2).trim());
		}
		else{
			fail();
		}
	}
	
	@Test
	public void politicianMandatePatternTest() {
		Pattern p = Pattern.compile("([^(,]*)(?:\\(([^\\.]+)\\.(?:.([^\\.]+)\\.)?\\sGP\\))?,? ?([^\\d]+)?\\s(\\d+\\.\\d+\\.\\d{4})(?: . (\\d+\\.\\d+\\.\\d{4}))?");
		Matcher m = p.matcher("Abgeordneter zum Nationalrat (XXV. GP), SPÖ 29.10.2013 – 16.12.2013");
		if (m.find()) {
			String function = m.group(1);
			assertEquals("Abgeordneter zum Nationalrat", function.trim());
			assertEquals("XXV", m.group(2));
			assertNull(m.group(3));
			assertEquals("SPÖ", m.group(4));
			assertEquals("29.10.2013", m.group(5));
			assertEquals("16.12.2013", m.group(6));
		}

		m = p.matcher("Abgeordnete zum Nationalrat (XXIII.–XXV. GP), FPÖ 30.10.2006 –");
		if (m.find()) {
			String function = m.group(1);
			assertEquals("Abgeordnete zum Nationalrat", function.trim());
			assertEquals("XXIII", m.group(2));
			assertEquals("XXV", m.group(3));
			assertEquals("FPÖ", m.group(4));
			assertEquals("30.10.2006", m.group(5));
			assertNull(m.group(6));
		}
		else{
			fail();
		}
		
		m = p.matcher("Abgeordnete zum Nationalrat (XXV. GP), STRONACH 29.10.2013 –");
		if (m.find()) {
			String function = m.group(1);
			assertEquals("Abgeordnete zum Nationalrat", function.trim());
			assertEquals("XXV", m.group(2));
			assertNull(m.group(3));
			assertEquals("STRONACH", m.group(4));
			assertEquals("29.10.2013", m.group(5));
			assertNull(m.group(6));
		}
		else{
			fail();
		}
		
		m = p.matcher("Bundesminister für Landesverteidigung und Sport, 11.03.2013 –");
		if (m.find()) {
			String function = m.group(1);
			assertEquals("Bundesminister für Landesverteidigung und Sport", function.trim());
			assertNull(m.group(2));
			assertNull(m.group(3));
			assertNull(m.group(4));
			assertEquals("11.03.2013", m.group(5));
			assertNull(m.group(6));
		}
		else{
			fail();
		}
		
		m = p.matcher("Mitglied des Bundesrates, ohne Fraktion 24.04.2013 –");
		if (m.find()) {
			String function = m.group(1);
			assertEquals("Mitglied des Bundesrates", function.trim());
			assertNull(m.group(2));
			assertNull(m.group(3));
			assertEquals("ohne Fraktion", m.group(4).trim());
			assertEquals("24.04.2013", m.group(5));
			assertNull(m.group(6));
		}
		else{
			fail();
		}
		
		
	}
}
