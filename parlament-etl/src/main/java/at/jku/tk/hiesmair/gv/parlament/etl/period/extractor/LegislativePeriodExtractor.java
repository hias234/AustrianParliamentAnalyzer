package at.jku.tk.hiesmair.gv.parlament.etl.period.extractor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.springframework.stereotype.Component;

import at.jku.tk.hiesmair.gv.parlament.etl.period.extractor.feed.ProtocolFeedItem;
import at.jku.tk.hiesmair.gv.parlament.etl.period.extractor.feed.parser.LegislativePeriodFeedParser;
import at.jku.tk.hiesmair.gv.parlament.etl.period.extractor.feed.parser.SessionTitleParser;
import at.jku.tk.hiesmair.gv.parlament.feed.FeedReader;

import com.frequal.romannumerals.Converter;

/**
 * Extracts the protocols from the Website of the Austrian parliament
 * 
 * @author Markus
 *
 */
@Component
public class LegislativePeriodExtractor {

	/** URL the feed is sitting on */
	protected static final String RSS_FEED_URL = "http://www.parlament.gv.at/PAKT/STPROT/filter.psp?view=RSS&jsMode=RSS&xdocumentUri=%2FPAKT%2FSTPROT%2Findex.shtml&NRBRBV=NR&anwenden=Anwenden&NUR_VORL=N&R_PLSO=PL&GP=##PERIOD##&SUCH=&listeId=211&FBEZ=FP_011";

	/** Pattern that needs to be replaced with the period */
	protected static final String PERIOD_PATTERN = "##PERIOD##";

	/** roman number converter */
	protected Converter converter;

	public LegislativePeriodExtractor() {
		converter = new Converter();
	}

	public List<ProtocolFeedItem> extractProtocols(int period) throws Exception {
		String feedContent = getFeedContent(period);
		LegislativePeriodFeedParser parser = new LegislativePeriodFeedParser(feedContent, new SessionTitleParser(
				converter.toRomanNumerals(period)));

		return parser.getProtocols();
	}

	protected String getFeedContent(int period) throws MalformedURLException, IOException, InterruptedException {
		FeedReader feedReader = new FeedReader(getUrl(period));
		return feedReader.getFeedContent();
	}

	protected String getUrl(int period) throws MalformedURLException {
		String periodShortName = converter.toRomanNumerals(period);
		return buildUrl(periodShortName);
	}

	/**
	 * Build an URL for periods
	 * 
	 * @return
	 */
	private String buildUrl(String periodShortName) {
		return RSS_FEED_URL.replace(PERIOD_PATTERN, periodShortName);
	}
}
