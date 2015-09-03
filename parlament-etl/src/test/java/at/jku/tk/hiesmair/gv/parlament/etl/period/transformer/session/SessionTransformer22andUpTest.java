package at.jku.tk.hiesmair.gv.parlament.etl.period.transformer.session;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;

import at.jku.tk.hiesmair.gv.parlament.ParliamentTestUtil;
import at.jku.tk.hiesmair.gv.parlament.cache.DataCache;
import at.jku.tk.hiesmair.gv.parlament.cache.InMemoryDataCache;
import at.jku.tk.hiesmair.gv.parlament.entities.politician.Politician;
import at.jku.tk.hiesmair.gv.parlament.entities.session.Session;
import at.jku.tk.hiesmair.gv.parlament.entities.session.SessionChairMan;
import at.jku.tk.hiesmair.gv.parlament.etl.politician.PoliticiansEtlJob;
import at.jku.tk.hiesmair.gv.parlament.etl.politician.extractor.PoliticiansExtractor;
import at.jku.tk.hiesmair.gv.parlament.etl.politician.loader.PoliticiansLoader;
import at.jku.tk.hiesmair.gv.parlament.etl.politician.transformer.PoliticianTransformer;
import at.jku.tk.hiesmair.gv.parlament.etl.politician.transformer.PoliticiansTransformer;

public class SessionTransformer22andUpTest {

	private SimpleDateFormat dateFormat = new SimpleDateFormat(
			"dd.MM.yyyy HH:mm");

	@Test
	public void testGetSession() throws Exception {
		DataCache cache = new InMemoryDataCache();

		PoliticiansEtlJob politicianJob = new PoliticiansEtlJob(
				new PoliticiansExtractor(), new PoliticiansTransformer(
						new PoliticianTransformer(cache)),
				new PoliticiansLoader() {

					@Override
					public void loadPoliticians(List<Politician> politicians) {

					}
				});
		politicianJob.start();

		SessionTransformer22andUp extractor = new SessionTransformer22andUp(
				cache, new PoliticianTransformer(cache));
		Session session = extractor.getSession(cache.getLegislativePeriod(25),
				ParliamentTestUtil
						.getDocumentFromClasspath("period25/index_25.html"),
				ParliamentTestUtil
						.getDocumentFromClasspath("period25/protocol_25.html"));

		assertEquals("SessionNr of Session", 25, session.getSessionNr()
				.intValue());
		assertEquals("StartDate of Session", "20.05.2014 09:05",
				dateFormat.format(session.getStartDate()));
		assertEquals("EndDate of Session", "20.05.2014 22:04",
				dateFormat.format(session.getEndDate()));

		List<SessionChairMan> chairMen = session.getChairMen();
		chairMen.sort((cm1, cm2) -> cm1.getPosition().compareTo(
				cm2.getPosition()));

		assertEquals(3, chairMen.size());
		assertEquals(1, chairMen.get(0).getPosition().intValue());
		assertEquals("http://www.parlament.gv.at/WWER/PAD_04476/index.shtml",
				chairMen.get(0).getPolitician().getId());
		assertEquals(2, chairMen.get(1).getPosition().intValue());
		assertEquals("http://www.parlament.gv.at/WWER/PAD_02822/index.shtml",
				chairMen.get(1).getPolitician().getId());
		assertEquals(3, chairMen.get(2).getPosition().intValue());
		assertEquals("http://www.parlament.gv.at/WWER/PAD_35521/index.shtml",
				chairMen.get(2).getPolitician().getId());

		// assertTrue("politicians of session", session
		// .getAbsentNationalCouncilMembers().size() > 0);
		//
		// for (NationalCouncilMember ncm : session
		// .getPresentNationalCouncilMembers()) {
		// Politician p = ncm.getPolitician();
		// if (p.getSurName().equals("Kopf")) {
		// assertEquals("Karlheinz", p.getFirstName());
		// assertEquals("", p.getTitle());
		// }
		// System.out.println(p);
		// }
		//
		// System.out.println();
		// session.getDiscussions().forEach(d -> System.out.println(d));
	}
}
