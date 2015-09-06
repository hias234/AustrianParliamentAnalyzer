package at.jku.tk.hiesmair.gv.parliament.sentiment;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

import at.jku.tk.hiesmair.gv.parliament.entities.discussion.speech.sentiment.Sentiment;

public class SentiWsSentimentAnalyzerTest {
	private static String STANBOL_ENDPOINT;
	private static String STANBOL_CHAINNAME;
	private static String TEXT1;
	private static String TEXT2;

	@BeforeClass
	public static void setUp() {
		try {
			final Properties properties = loadProperties();

			STANBOL_ENDPOINT = properties.getProperty("stanbol.endpointurl");
			STANBOL_CHAINNAME = properties.getProperty("stanbol.chainname");
			TEXT1 = properties.getProperty("text1");
			TEXT2 = properties.getProperty("text2");

		} catch (final IOException e) {
			// Should never happen
			throw new AssertionError(e);
		}
	}

	private static Properties loadProperties() throws IOException {
		return loadProperties(StanbolSentimentAnalyzerTest.class);
	}

	private static Properties loadProperties(final Class<?> loadingClass) throws IOException {
		final Properties result = new Properties();

		try (final InputStream propertyStream = loadingClass.getClassLoader().getResourceAsStream(
				"stanbol-test.properties")) {
			result.load(propertyStream);
		}

		return result;
	}
	
	@Test
	public void testSentimentAnalyzer() throws Exception{
		SentiWsSentimentAnalyzer analyzer = new SentiWsSentimentAnalyzer();
		
		Sentiment s = analyzer.getSentiments(TEXT1).get(0);
		System.out.println(s.getSentiment());
		
		s = analyzer.getSentiments(TEXT2).get(0);
		System.out.println(s.getSentiment());
	}
	
}
