package at.jku.tk.hiesmair.gv.parlament.etl.politician.loader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
import at.jku.tk.hiesmair.gv.parlament.entities.club.ParliamentClub;
import at.jku.tk.hiesmair.gv.parlament.entities.mandate.Mandate;
import at.jku.tk.hiesmair.gv.parlament.entities.mandate.NationalCouncilMember;
import at.jku.tk.hiesmair.gv.parlament.entities.politician.Politician;
import at.jku.tk.hiesmair.gv.parlament.etl.politician.DummyPoliticianEtlApp;
import at.jku.tk.hiesmair.gv.parlament.etl.politician.loader.db.PoliticiansDatabaseLoader;

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

		Set<Mandate> mandates = new HashSet<Mandate>();
		NationalCouncilMember member = new NationalCouncilMember("desc", p, new Date(), new Date(), club,
				new HashSet<LegislativePeriod>());
		
//		LegislativePeriod period = new LegislativePeriod(25);
//		member.setPeriods(Arrays.asList(period));

		mandates.add(member);

		p.setMandates(mandates);

		politiciansLoader.loadPoliticians(Arrays.asList(p));

		int size = 0;
		for (Politician pol : politicianRepository.findAll()) {
			assertEquals(p.getId(), pol.getId());

			Set<Mandate> m = pol.getMandates();
			assertEquals(mandates.size(), m.size());
			assertEquals(mandates.iterator().next().getDescription(), m.iterator().next().getDescription());
			assertTrue(mandates.iterator().next() instanceof NationalCouncilMember);
//			assertEquals(period.getPeriod(), ((NationalCouncilMember)mandates.get(0)).getPeriods().get(0).getPeriod());

			size++;
		}
		assertEquals(1, size);
	}

}
