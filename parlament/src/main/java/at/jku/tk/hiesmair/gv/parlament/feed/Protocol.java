package at.jku.tk.hiesmair.gv.parlament.feed;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import at.jku.tk.hiesmair.gv.parlament.Settings;

/**
 * Holds all elements of a protocol
 * 
 * @author matthias
 */
public class Protocol implements Serializable {

	private static final long serialVersionUID = 4132582481442842541L;

	private URL url;

	private String title;

	private Date pubDate;

	private String description;

	private volatile String indexCacheName;

	private volatile String protocolCacheName;

	private volatile String indexContent;
	private volatile Document indexDocument;

	private volatile String protocolContent;
	private volatile Document protocolDocument;

	/**
	 * Default constructor
	 */
	public Protocol() {
		this.indexCacheName = null;
		this.protocolCacheName = null;
		this.indexContent = null;
		this.protocolContent = null;
	}

	/**
	 * Generate the filename for the cache file
	 * 
	 * @return
	 */
	public String getIndexCacheName() {
		if (this.indexCacheName == null) {
			StringBuilder sb = new StringBuilder();
			sb.append(Settings.CACHE_PATH);
			if (!Settings.CACHE_PATH.endsWith(File.separator)) {
				sb.append(File.separatorChar);
			}
			sb.append("index_");
			sb.append(this.title);
			sb.append(".html");
			this.indexCacheName = sb.toString();
		}
		return this.indexCacheName;
	}

	/**
	 * Generate the filename for the cache file
	 * 
	 * @return
	 */
	public String getProtocolCacheName() {
		if (this.protocolCacheName == null) {
			StringBuilder sb = new StringBuilder();
			sb.append(Settings.CACHE_PATH);
			if (!Settings.CACHE_PATH.endsWith(File.separator)) {
				sb.append(File.separatorChar);
			}
			sb.append("protocol_");
			sb.append(this.title);
			sb.append(".html");
			this.protocolCacheName = sb.toString();
		}
		return this.protocolCacheName;
	}

	/**
	 * @return a {@link File} object that points to the cache file
	 */
	public File getIndexCacheFile() {
		return new File(this.getIndexCacheName());
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
	public boolean isCached() {
		return getIndexCacheFile().exists() && getProtocolCacheFile().exists();
	}

	/**
	 * Retrieve the file content from web and write to cache
	 * 
	 * @throws IOException
	 */
	private void loadFromWeb() throws IOException {
		// download the index
		InputStream in = this.url.openStream();
		this.indexContent = IOUtils.toString(in);
		FileUtils.writeStringToFile(getIndexCacheFile(), this.indexContent);

		// download the protocol
		String protocolUrlStr = this.findProtocolUrl();
		if (protocolUrlStr != null) {
			URL protocolUrl = new URL(protocolUrlStr);
			in = protocolUrl.openStream();
			this.protocolContent = IOUtils.toString(in);
			FileUtils.writeStringToFile(getProtocolCacheFile(), this.protocolContent);
		}
	}

	/**
	 * Retrieve the file content from cache
	 * 
	 * @throws IOException
	 */
	private void loadFromCache() throws IOException {
		this.indexContent = FileUtils.readFileToString(getIndexCacheFile());
		this.protocolContent = FileUtils.readFileToString(getProtocolCacheFile());
	}

	/**
	 * @return the filecontent if not available locally retrieve
	 * @throws IOException
	 */
	public String getFileContent() throws IOException {
		if (this.indexContent == null) {
			if (this.isCached()) {
				loadFromCache();
			} else {
				loadFromWeb();
			}
		}
		return this.indexContent;
	}

	/**
	 * @return a Jsoup parsed document
	 */
	public Document getIndexDocument() {
		if (indexDocument == null) {
			indexDocument = createIndexDocument();
		}
		return indexDocument;
	}

	private Document createIndexDocument() {
		if (indexContent == null) {
			return null;
		}
		return Jsoup.parse(indexContent);
	}
	
	/**
	 * @return a Jsoup parsed document
	 */
	public Document getProtocolDocument() {
		if (protocolDocument == null) {
			protocolDocument = createProtocolDocument();
		}
		return protocolDocument;
	}

	private Document createProtocolDocument() {
		if (protocolContent == null) {
			return null;
		}
		return Jsoup.parse(protocolContent);
	}

	/** find the URL for the actual protocol document */
	public String findProtocolUrl() {
		String hostpart = this.url.getProtocol() + "://" + this.url.getHost();
		int locationProtocolHtml = this.description.indexOf("Protokoll HTML-Format:");
		if (locationProtocolHtml > 0) {
			String hrefPattern = "<a href='";
			int locationProtocolHref = this.description.indexOf(hrefPattern, locationProtocolHtml);
			locationProtocolHref += hrefPattern.length();
			int endLocationProtocolHref = this.description.indexOf('\'', locationProtocolHref);
			return hostpart + this.description.substring(locationProtocolHref, endLocationProtocolHref);
		}
		return null;
	}

	// getters and setters

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getPubDate() {
		return pubDate;
	}

	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Protocol [url=" + url + ", title=" + title + ", pubDate=" + pubDate + ", description=" + description
				+ ", indexCacheName=" + indexCacheName + ", protocolCacheName=" + protocolCacheName + ", indexContent="
				+ indexContent + ", protocolContent=" + protocolContent + "]";
	}

}
