package at.jku.tk.hiesmair.gv.parliament.etl.period.extractor.feed;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import at.jku.tk.hiesmair.gv.parliament.feed.parser.FeedItem;
import at.jku.tk.hiesmair.gv.parliament.util.ParliamentUtil;

/**
 * Holds all elements of a protocol
 * 
 * @author matthias
 */
public class ProtocolFeedItem extends FeedItem {

	private static final String PROTOCOL_CACHE_PREFIX = "protocol_";
	protected static final String CACHE_SPECIFIC_DIRECTORY = ".parliamentdata\\periods";
	protected static final String CACHE_PREFIX = "index_";

	private volatile String protocolCacheName;
	private volatile String protocolContent;
	private volatile Document protocolDocument;

	public ProtocolFeedItem() {
		super(CACHE_SPECIFIC_DIRECTORY, CACHE_PREFIX);
	}

	/**
	 * Generate the filename for the cache file
	 * 
	 * @return
	 */
	public String getProtocolCacheName() {
		if (this.protocolCacheName == null) {
			this.protocolCacheName = ParliamentUtil.getCachePath(
					CACHE_SPECIFIC_DIRECTORY, PROTOCOL_CACHE_PREFIX + this.url.toString().replaceAll("[ \\/\\.\\(\\):]", "") + ".html");
		}
		return this.protocolCacheName;
	}

	/**
	 * @return a {@link File} object that points to the cache file
	 */
	public File getProtocolCacheFile() {
		return new File(this.getProtocolCacheName());
	}

	/**
	 * @return checks whether a cache file already exists
	 */
	@Override
	public boolean isCached() {
		return getIndexCacheFile().exists() && getProtocolCacheFile().exists();
	}

	/**
	 * Retrieve the file content from web and write to cache
	 * 
	 * @throws IOException
	 */
	@Override
	protected void loadFromWeb() throws IOException {
		super.loadFromWeb();

		// download the protocol
		String protocolUrlStr = this.findProtocolUrl();
		if (protocolUrlStr != null) {
			URL protocolUrl = new URL(protocolUrlStr);
			InputStream in = protocolUrl.openStream();
			this.protocolContent = IOUtils.toString(in);
			FileUtils.writeStringToFile(getProtocolCacheFile(),
					this.protocolContent);
		}
	}

	/**
	 * Retrieve the file content from cache
	 * 
	 * @throws IOException
	 */
	@Override
	protected void loadFromCache() throws IOException {
		super.loadFromCache();
		this.protocolContent = FileUtils
				.readFileToString(getProtocolCacheFile());
	}

	/**
	 * @return the filecontent if not available locally retrieve
	 * @throws IOException
	 */
	public String getProtocolContent() throws IOException {
		if (this.indexContent == null) {
			if (this.isCached()) {
				loadFromCache();
			} else {
				loadFromWeb();
			}
		}
		return this.protocolContent;
	}

	/**
	 * @return a Jsoup parsed document
	 * @throws IOException
	 */
	public Document getProtocolDocument() throws IOException {
		if (protocolDocument == null) {
			protocolDocument = createProtocolDocument();
		}
		return protocolDocument;
	}

	private Document createProtocolDocument() throws IOException {
		if (protocolContent == null) {
			protocolContent = getProtocolContent();
		}
		if (protocolContent != null) {
			return Jsoup.parse(protocolContent);
		}
		return null;
	}

	/** find the URL for the actual protocol document */
	public String findProtocolUrl() {
		String hostpart = this.url.getProtocol() + "://" + this.url.getHost();
		int locationProtocolHtml = this.description
				.indexOf("Protokoll HTML-Format:");
		if (locationProtocolHtml > 0) {
			String hrefPattern = "<a href='";
			int locationProtocolHref = this.description.indexOf(hrefPattern,
					locationProtocolHtml);
			locationProtocolHref += hrefPattern.length();
			int endLocationProtocolHref = this.description.indexOf('\'',
					locationProtocolHref);
			return hostpart
					+ this.description.substring(locationProtocolHref,
							endLocationProtocolHref);
		}
		return null;
	}

	// getters and setters

	@Override
	public URL getUrl() {
		return url;
	}

	@Override
	public void setUrl(URL url) {
		this.url = url;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public Date getPubDate() {
		return pubDate;
	}

	@Override
	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Protocol [url=" + url + ", title=" + title + ", pubDate="
				+ pubDate + ", description=" + description
				+ ", indexCacheName=" + indexCacheName + ", protocolCacheName="
				+ protocolCacheName + ", indexContent=" + indexContent
				+ ", protocolContent=" + protocolContent + "]";
	}

}
