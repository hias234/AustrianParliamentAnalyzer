package at.jku.tk.hiesmair.gv.parliament.db;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import at.jku.tk.hiesmair.gv.parliament.db.loader.ParliamentDatabaseLoader;

@SpringBootApplication
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ParliamentDatabaseLoader.class })
@ComponentScan(basePackages = { "at.jku.tk.hiesmair.gv.parliament.db" })
@TestPropertySource(locations = "classpath:application-test.properties")
public class ParliamentDatabaseLoaderTest {

	@Inject
	private ParliamentDatabaseLoader databaseLoader;
	
	@Test
	public void testDbLoader() {
		
	}
}
