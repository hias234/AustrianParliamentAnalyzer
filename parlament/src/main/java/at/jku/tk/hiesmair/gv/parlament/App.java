package at.jku.tk.hiesmair.gv.parlament;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import at.jku.tk.hiesmair.gv.parlament.feed.FeedReader;
import at.jku.tk.hiesmair.gv.parlament.period.LegislativePeriodEtlJob;
import at.jku.tk.hiesmair.gv.parlament.period.extractor.LegislativePeriodExtractor;
import at.jku.tk.hiesmair.gv.parlament.period.loader.ConsoleLegislativePeriodLoader;
import at.jku.tk.hiesmair.gv.parlament.period.transformer.LegislativePeriodTransformer;

/**
 * Main Application kicking off the analysis process
 */
public class App {

	/**
	 * Create a {@link FeedReader} download everything and analyse everything
	 * 
	 * @param args
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws ParseException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws DOMException
	 */
	public static void main(String[] args) throws Exception {
		LegislativePeriodEtlJob job = new LegislativePeriodEtlJob(new LegislativePeriodExtractor(),
				new LegislativePeriodTransformer(), new ConsoleLegislativePeriodLoader());
		
		job.start(Arrays.asList(25, 24, 23, 22, 21));
	}
}
