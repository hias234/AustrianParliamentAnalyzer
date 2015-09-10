package at.jku.tk.hiesmair.gv.parlament.etl.period.loader;

import java.util.Arrays;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import at.jku.tk.hiesmair.gv.parlament.db.DummyDbApp;
import at.jku.tk.hiesmair.gv.parlament.entities.DummyEntitiesApp;
import at.jku.tk.hiesmair.gv.parlament.etl.period.DummyPeriodEtlApp;
import at.jku.tk.hiesmair.gv.parliament.db.LegislativePeriodRepository;
import at.jku.tk.hiesmair.gv.parliament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parliament.etl.period.loader.db.LegislativePeriodDatabaseLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DummyPeriodEtlApp.class, DummyEntitiesApp.class, DummyDbApp.class })
//@ComponentScan({ "at.jku.tk.hiesmair.gv.parlament.entities", "at.jku.tk.hiesmair.gv.parlament.db",
//		"at.jku.tk.hiesmair.gv.parlament.etl.period.db" })
@TestPropertySource(locations = "classpath:application-test.properties")
public class PeriodLoaderTest {

	@Inject
	private LegislativePeriodDatabaseLoader periodLoader;

	@Inject
	private LegislativePeriodRepository periodRepository;

	@Test
	public void testLoader() {
		LegislativePeriod period25 = new LegislativePeriod(25);

		periodLoader.loadLegislativePeriods(Arrays.asList(period25));
	}
}
