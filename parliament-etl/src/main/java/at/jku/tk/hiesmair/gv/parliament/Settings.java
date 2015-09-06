package at.jku.tk.hiesmair.gv.parliament;

/**
 * Provides settings for retrieving data from the Austrian parliament
 * 
 * @author matthias
 */
public interface Settings {

	/** First available protocol period */
	public static final int FIRST_PERIOD = 1;

	/** Last available protocol period */
	public static final int LAST_PERIOD = 25;

	/** Base-Url of Austrian Parliament */
	public static final String BASE_URL = "http://www.parlament.gv.at";
}