package at.jku.tk.hiesmair.gv.parlament.util;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import at.jku.tk.hiesmair.gv.parliament.util.ParliamentUtil;

public class ParliamentUtilTest {

	@Test
	public void testGetCachePath() {
		String baseCachePath = ParliamentUtil.getBaseCachePath();
		assertEquals(System.getProperty("user.home"), baseCachePath);

		String target = baseCachePath + (baseCachePath.endsWith(File.separator) ? "" : File.separator);
		target += ".test\\test2" + File.separator + "testfile_file.pdf";
		String cachePath = ParliamentUtil.getCachePath(".test\\test2", "testfile_file.pdf");
		assertEquals(target, cachePath);
	}
}
