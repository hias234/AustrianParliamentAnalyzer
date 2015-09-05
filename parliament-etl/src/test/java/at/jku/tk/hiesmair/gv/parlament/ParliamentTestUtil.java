package at.jku.tk.hiesmair.gv.parlament;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ParliamentTestUtil {

	/**
	 * reads xml/html-document from classpath, parses it and returns it
	 * 
	 * @param path path relative to classpath
	 * @return
	 * @throws IOException
	 */
	public static Document getDocumentFromClasspath(String path) throws IOException {
		InputStream fileStream = ParliamentTestUtil.class.getClassLoader()
				.getResourceAsStream(path);

		StringWriter writer = new StringWriter();
		IOUtils.copy(fileStream, writer, "UTF-8");
		String fileContent = writer.toString();

		Document document = Jsoup.parse(fileContent);
		return document;
	}
	
}
