package at.jku.tk.hiesmair.gv.parlament.period.transformer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import at.jku.tk.hiesmair.gv.parlament.cache.InMemoryDataCache;
import at.jku.tk.hiesmair.gv.parlament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parlament.entities.Politician;
import at.jku.tk.hiesmair.gv.parlament.entities.mandate.NationalCouncilMember;
import at.jku.tk.hiesmair.gv.parlament.entities.session.Session;
import at.jku.tk.hiesmair.gv.parlament.etl.period.transformer.session.SessionTransformer22andUp;
import at.jku.tk.hiesmair.gv.parlament.etl.politician.PoliticiansEtlJob;
import at.jku.tk.hiesmair.gv.parlament.etl.politician.extractor.PoliticiansExtractor;
import at.jku.tk.hiesmair.gv.parlament.etl.politician.loader.PoliticiansLoader;
import at.jku.tk.hiesmair.gv.parlament.etl.politician.transformer.PoliticiansTransformer;

public class SessionTransformerTest {

	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

	@Test
	public void testGetSession() throws Exception {
		PoliticiansEtlJob politicianJob = new PoliticiansEtlJob(new PoliticiansExtractor(), new PoliticiansTransformer(),
				new PoliticiansLoader() {

			@Override
			public void loadPoliticians(List<Politician> politicians) {
				
			}
		});
		politicianJob.start();
		
		SessionTransformer22andUp extractor = new SessionTransformer22andUp(new InMemoryDataCache());
		Session session = extractor.getSession(new LegislativePeriod(25), getIndex(), getProtocol());

		assertEquals("SessionNr of Session", 25, session.getSessionNr().intValue());
		assertEquals("StartDate of Session", "20.05.2014 09:05", dateFormat.format(session.getStartDate()));
		assertEquals("EndDate of Session", "20.05.2014 22:04", dateFormat.format(session.getEndDate()));

		assertTrue("politicians of session", session.getAbsentNationalCouncilMembers().size() > 0);

		for (NationalCouncilMember ncm : session.getPresentNationalCouncilMembers()) {
			Politician p = ncm.getPolitician();
			if (p.getSurName().equals("Kopf")) {
				assertEquals("Karlheinz", p.getFirstName());
				assertEquals("", p.getTitle());
			}
			System.out.println(p);
		}
		
		System.out.println();
		session.getDiscussions().forEach(d -> System.out.println(d));
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
