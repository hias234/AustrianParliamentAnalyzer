package at.jku.tk.hiesmair.gv.parlament.analysis.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import at.jku.tk.hiesmair.gv.parlament.entities.ParliamentData;
import at.jku.tk.hiesmair.gv.parlament.entities.Politician;
import at.jku.tk.hiesmair.gv.parlament.entities.Session;

public class AustrianParliamentSessionExtractorTest {

	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	
	
	
	@Test
	public void testGetSession() throws Exception {
		ParliamentData data = new ParliamentData();

		AustrianParliamentSessionExtractor extractor = new AustrianParliamentSessionExtractor();
		Session session = extractor.getSession(getIndex(), getProtocol(), data);
		
		assertEquals("SessionNr of Session", 25, session.getSessionNr().intValue());
		assertEquals("StartDate of Session", "20.05.2014 09:05", dateFormat.format(session.getStartDate()));
		assertEquals("EndDate of Session", "20.05.2014 22:04", dateFormat.format(session.getEndDate()));
		
		assertTrue("politicians of session", session.getPoliticians().size() > 0);
		
		Collections.sort(session.getPoliticians(), new Comparator<Politician>() {

			@Override
			public int compare(Politician o1, Politician o2) {
				return o1.getSurName().compareTo(o2.getSurName());
			}
		});
		
		for (Politician p : session.getPoliticians()){
			if (p.getSurName().equals("Kopf")){
				assertEquals("Karlheinz", p.getFirstName());
				assertEquals("", p.getTitle());
			}
			System.out.println(p);
		}
	}



	protected Document getProtocol() throws IOException {
		InputStream testProtocolStream = this.getClass().getClassLoader().getResourceAsStream("protocol_25.html");

		StringWriter writer = new StringWriter();
		IOUtils.copy(testProtocolStream, writer, "UTF-8");
		String testProtocolString = writer.toString();

		Document document = Jsoup.parse(testProtocolString);
		return document;
	}

	protected Document getIndex() throws IOException {
		InputStream testIndexStream = this.getClass().getClassLoader().getResourceAsStream("index_25.html");

		StringWriter writer = new StringWriter();
		IOUtils.copy(testIndexStream, writer, "UTF-8");
		String testIndexString = writer.toString();

		Document document = Jsoup.parse(testIndexString);
		return document;
	}
}
