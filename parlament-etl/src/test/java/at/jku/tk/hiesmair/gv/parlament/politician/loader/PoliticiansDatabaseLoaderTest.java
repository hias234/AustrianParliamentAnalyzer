package at.jku.tk.hiesmair.gv.parlament.politician.loader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import at.jku.tk.hiesmair.gv.parlament.db.DummyDbApp;
import at.jku.tk.hiesmair.gv.parlament.db.PoliticianRepository;
import at.jku.tk.hiesmair.gv.parlament.entities.DummyEntitiesApp;
import at.jku.tk.hiesmair.gv.parlament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parlament.entities.Politician;
import at.jku.tk.hiesmair.gv.parlament.entities.club.ParliamentClub;
import at.jku.tk.hiesmair.gv.parlament.entities.mandate.Mandate;
import at.jku.tk.hiesmair.gv.parlament.entities.mandate.NationalCouncilMember;
import at.jku.tk.hiesmair.gv.parlament.politician.DummyPoliticianEtlApp;
import at.jku.tk.hiesmair.gv.parlament.politician.loader.db.PoliticiansDatabaseLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { DummyDbApp.class, DummyEntitiesApp.class, DummyPoliticianEtlApp.class })
@TestPropertySource(locations = "classpath:application-test.properties")
public class PoliticiansDatabaseLoaderTest {

	@Inject
	private PoliticiansDatabaseLoader politiciansLoader;

	@Inject
	private PoliticianRepository politicianRepository;

	@Test
	public void testLoader() {
		Politician p = new Politician();
		p.setBirthDate(new Date());
		p.setFirstName("first");
		p.setSurName("sur");
		p.setId("id");

		ParliamentClub club = new ParliamentClub("FPÖ", "");

		List<Mandate> mandates = new ArrayList<Mandate>();
		NationalCouncilMember member = new NationalCouncilMember("desc", p, new Date(), new Date(), club,
				new ArrayList<LegislativePeriod>());
		
//		LegislativePeriod period = new LegislativePeriod(25);
//		member.setPeriods(Arrays.asList(period));

		mandates.add(member);

		p.setMandates(mandates);

		politiciansLoader.loadPoliticians(Arrays.asList(p));

		int size = 0;
		for (Politician pol : politicianRepository.findAll()) {
			assertEquals(p.getId(), pol.getId());

			List<Mandate> m = pol.getMandates();
			assertEquals(mandates.size(), m.size());
			assertEquals(mandates.get(0).getDescription(), m.get(0).getDescription());
			assertTrue(mandates.get(0) instanceof NationalCouncilMember);
//			assertEquals(period.getPeriod(), ((NationalCouncilMember)mandates.get(0)).getPeriods().get(0).getPeriod());

			size++;
		}
		assertEquals(1, size);
	}

}
