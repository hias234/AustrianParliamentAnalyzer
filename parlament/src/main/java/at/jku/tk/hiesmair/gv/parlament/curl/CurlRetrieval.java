package at.jku.tk.hiesmair.gv.parlament.curl;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.IOUtils;

/**
 * Wrapper that uses the curl commandline to retrieve the feed
 * 
 * @author matthias
 */
public class CurlRetrieval {

	/**
	 * Retrieve data from the specified URL
	 * 
	 * @param url
	 * @return
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static String retrieve(String url) throws IOException, InterruptedException {
		ProcessBuilder pb = new ProcessBuilder("curl", url);
		Process process = pb.start();
		process.waitFor();
		return IOUtils.toString(process.getInputStream());
	}
	
	/**
	 * Retrieve data from the specified URL
	 * 
	 * @param url
	 * @return
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static String retrieve(URL url) throws IOException, InterruptedException {
		return retrieve(url.toString());
	}
	
}
