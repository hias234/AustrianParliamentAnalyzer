package at.jku.tk.hiesmair.gv.parlament.feed.parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import at.jku.tk.hiesmair.gv.parlament.util.ParliamentUtil;

/**
 * Item of the feed - loads the html-file
 * 
 * @author Markus
 *
 */
public class FeedItem {

	protected URL url;
	protected String title;
	protected Date pubDate;
	protected String description;

	protected String indexCachePrefix;
	protected volatile String specificCacheDirectory;

	protected volatile String indexCacheName;

	protected volatile String indexContent;
	protected volatile Document indexDocument;

	public FeedItem(String spectivicCacheDirectory, String indexCachePrefix) {
		super();
		this.specificCacheDirectory = spectivicCacheDirectory;
		this.indexCachePrefix = indexCachePrefix;
	}

	/**
	 * Generate the filename for the cache file
	 * 
	 * @return
	 */
	public String getIndexCacheName() {
		if (this.indexCacheName == null) {
			this.indexCacheName = ParliamentUtil.getCachePath(
					specificCacheDirectory, indexCachePrefix + this.title
							+ ".html");
		}
		return this.indexCacheName;
	}

	/**
	 * @return a {@link File} object that points to the cache file
	 */
	public File getIndexCacheFile() {
		return new File(this.getIndexCacheName());
	}

	/**
	 * @return checks whether a cache file already exists
	 */
	public boolean isCached() {
		return getIndexCacheFile().exists();
	}

	/**
	 * Retrieve the file content from web and write to cache
	 * 
	 * @throws IOException
	 */
	protected void loadFromWeb() throws IOException {
		// download the index
		InputStream in = this.url.openStream();
		this.indexContent = IOUtils.toString(in);
		FileUtils.writeStringToFile(getIndexCacheFile(), this.indexContent);
	}

	/**
	 * Retrieve the file content from cache
	 * 
	 * @throws IOException
	 */
	protected void loadFromCache() throws IOException {
		this.indexContent = FileUtils.readFileToString(getIndexCacheFile());
	}

	/**
	 * @return the filecontent if not available locally retrieve
	 * @throws IOException
	 */
	public String getIndexContent() throws IOException {
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
	 * @throws IOException
	 */
	public Document getIndexDocument() throws IOException {
		if (indexDocument == null) {
			indexDocument = createIndexDocument();
		}
		return indexDocument;
	}

	private Document createIndexDocument() throws IOException {
		if (indexContent == null) {
			indexContent = getIndexContent();
		}
		return Jsoup.parse(indexContent);
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
}
