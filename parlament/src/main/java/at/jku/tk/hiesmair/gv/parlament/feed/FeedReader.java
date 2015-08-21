package at.jku.tk.hiesmair.gv.parlament.feed;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;

import at.jku.tk.hiesmair.gv.parlament.Settings;

import com.frequal.romannumerals.Converter;

/**
 * Is capable of retrieving the feed from parliament and parses it
 * 
 * @author matthias
 */
public class FeedReader implements Settings {

	/** URL the feed is sitting on */
	public static final String RSS_FEED_URL = "http://www.parlament.gv.at/PAKT/STPROT/filter.psp?view=RSS&jsMode=RSS&xdocumentUri=%2FPAKT%2FSTPROT%2Findex.shtml&NRBRBV=NR&anwenden=Anwenden&NUR_VORL=N&R_PLSO=PL&GP=##PERIOD##&SUCH=&listeId=211&FBEZ=FP_011";

	/** Pattern that needs to be replaced with the period */
	public static final String PERIOD_PATTERN = "##PERIOD##";

	/** We use a fake a Moziall user agent otherwise we get invalid data */
	public static final String USER_AGENT = "Mozilla/4.0";

	/** Atom document */
	private String feedContent;

	/** Original URL */
	private final URL url;

	/** Period short name */
	private final String periodShortName;

	/**
	 * Creates a new {@link FeedReader} using the default setting
	 * 
	 * @throws MalformedURLException
	 */
	public FeedReader(int period) throws MalformedURLException {
		Converter converter = new Converter();
		this.periodShortName = converter.toRomanNumerals(period);
		this.url = new URL(buildUrl(this.periodShortName));
		this.feedContent = null;
	}

	/**
	 * Build an URL for periods
	 * 
	 * @return
	 */
	private static String buildUrl(String periodShortName) {
		return RSS_FEED_URL.replace(PERIOD_PATTERN, periodShortName);
	}

	/**
	 * Uses the field url to retrieve the current feed
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void loadFeedContent() throws IOException, InterruptedException {
		System.setProperty("http.agent", USER_AGENT);
		InputStream in = this.url.openStream();
		this.feedContent = IOUtils.toString(in);
	}

	// general getters and setters

	public String getFeedContent() throws IOException, InterruptedException {
		if (this.feedContent == null) {
			loadFeedContent();
		}
		return feedContent;
	}

	public void setFeedContent(String feedContent) {
		this.feedContent = feedContent;
	}

	public URL getUrl() {
		return url;
	}

	public String getPeriodShortName() {
		return periodShortName;
	}

}
