package at.jku.tk.hiesmair.gv.parlament.analysis.session;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import at.jku.tk.hiesmair.gv.parlament.entities.Session;

public class AustrianParliamentSessionExtractorTest {

	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	
	@Test
	public void testGetSession() throws Exception {
		InputStream testProtocolStream = this.getClass().getClassLoader().getResourceAsStream("protocol_2.html");

		StringWriter writer = new StringWriter();
		IOUtils.copy(testProtocolStream, writer, "UTF-8");
		String testProtocolString = writer.toString();

		Document document = Jsoup.parse(testProtocolString);

		AustrianParliamentSessionExtractor extractor = new AustrianParliamentSessionExtractor();
		Session session = extractor.getSession(document);
		assertEquals("SessionNr of Session", 2, session.getSessionNr().intValue());
		assertEquals("StartDate of Session", "29.10.2013 14:31", dateFormat.format(session.getStartDate()));
		assertEquals("EndDate of Session", "29.10.2013 14:33", dateFormat.format(session.getEndDate()));
	}

}
