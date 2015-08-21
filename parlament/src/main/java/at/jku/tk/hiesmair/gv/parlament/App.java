package at.jku.tk.hiesmair.gv.parlament;

import java.io.IOException;
import java.text.ParseException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import at.jku.tk.hiesmair.gv.parlament.extractor.LegislativePeriodExtractor;
import at.jku.tk.hiesmair.gv.parlament.extractor.feed.FeedReader;
import at.jku.tk.hiesmair.gv.parlament.loader.ConsoleLegislativePeriodLoader;
import at.jku.tk.hiesmair.gv.parlament.transformer.LegislativePeriodTransformer;

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
		
		job.start(25);
	}
}
