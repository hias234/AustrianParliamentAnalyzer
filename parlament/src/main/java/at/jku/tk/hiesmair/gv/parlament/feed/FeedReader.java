package at.jku.tk.hiesmair.gv.parlament.feed;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;

import at.jku.tk.hiesmair.gv.parlament.Settings;
import at.jku.tk.hiesmair.gv.parlament.curl.CurlRetrieval;

import com.frequal.romannumerals.Converter;

/**
 * Is capable of retrieving the feed from parliament and parses it
 * 
 * @author matthias
 */
public class FeedReader implements Settings {

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
		return Settings.RSS_FEED_URL.replace(PERIOD_PATTERN, periodShortName);
	}
 
	/**
	 * Uses the field url to retrieve the current feed
	 * 
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public void loadFeedContent() throws IOException, InterruptedException {
		switch (FEED_SOURCE) {
		case Java:
			System.setProperty("http.agent", USER_AGENT);
			InputStream in = this.url.openStream();
			this.feedContent = IOUtils.toString(in);
			break;
		case Curl:
			this.feedContent = CurlRetrieval.retrieve(this.url);
			break;
		case File:
			// TODO
			this.feedContent = IOUtils.toString(new FileInputStream(LOCAL_XML_FILE));
			break;
		default:
			break;
		}
	}
	
	// general getters and setters

	public String getFeedContent() throws IOException, InterruptedException {
		if(this.feedContent == null) {
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
