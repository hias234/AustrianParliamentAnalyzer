package at.jku.tk.hiesmair.gv.parlament;

import at.jku.tk.hiesmair.gv.parlament.feed.FeedSource;

/**
 * Provides settings for retrieving data from the Austrian parliament
 * 
 * @author matthias
 */
public interface Settings {

	/** URL the feed is sitting on */
	public static final String RSS_FEED_URL = "http://www.parlament.gv.at/PAKT/STPROT/filter.psp?view=RSS&jsMode=RSS&xdocumentUri=%2FPAKT%2FSTPROT%2Findex.shtml&NRBRBV=NR&anwenden=Anwenden&NUR_VORL=N&R_PLSO=PL&GP=##PERIOD##&SUCH=&listeId=211&FBEZ=FP_011";
	
	/** Pattern that needs to be replaced with the period */
	public static final String PERIOD_PATTERN = "##PERIOD##";
	
	/** First available protocol period */
	public static final int FIRST_PERIOD = 1;
	
	/** Last available protocol period */
	public static final int LAST_PERIOD = 25;
	
	/** We use a fake a Moziall user agent otherwise we get invalid data */
	public static final String USER_AGENT = "Mozilla/4.0";
	
	/** Uses CURL to retrieve the feed */
	public static final FeedSource FEED_SOURCE = FeedSource.Java;
	
	/** Date-format to parse XML files */
	public static final String DATE_FORMAT = "d MMM yyyy HH:mm:ss Z";
	
	/** Cache-path where the downloaded HTML documents are stored */
	public static final String CACHE_PATH = "C:\\Temp\\parlament";
	
	/** Filepath for local feed */
	@Deprecated
	public static final String LOCAL_XML_FILE= "null";
	
	/** Base-Url of Austrian Parliament */
	public static final String BASE_URL = "http://www.parlament.gv.at/";
}
