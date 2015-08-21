package at.jku.tk.hiesmair.gv.parlament.feed;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;

/**
 * Is capable of retrieving the feed from parliament and parses it
 * 
 * @author matthias
 */
public class FeedReader {

	/** We use a fake a Moziall user agent otherwise we get invalid data */
	public static final String USER_AGENT = "Mozilla/4.0";

	/** Atom document */
	private String feedContent;

	/** Original URL */
	private final URL url;

	/**
	 * Creates a new {@link FeedReader} using the default setting
	 * 
	 * @throws MalformedURLException
	 */
	public FeedReader(String url) throws MalformedURLException {
		this.url = new URL(url);
		this.feedContent = null;
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

}
