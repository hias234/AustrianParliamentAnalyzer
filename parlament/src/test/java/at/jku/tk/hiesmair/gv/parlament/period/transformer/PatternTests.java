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
	public void politicianPatternTest() {
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
		else{
			fail();
		}
		
		m = p.matcher("Abgeordnete zum Nationalrat (XXV. GP), STRONACH 29.10.2013 –");
		if (m.find()) {
			String function = m.group(1);
			assertEquals("Abgeordnete zum Nationalrat", function.trim());
			assertEquals("STRONACH", m.group(2));
			assertEquals("29.10.2013", m.group(3));
			assertNull(m.group(4));
		}
		else{
			fail();
		}
		
		
	}
}
