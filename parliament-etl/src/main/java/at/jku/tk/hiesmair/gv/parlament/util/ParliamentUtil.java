package at.jku.tk.hiesmair.gv.parlament.util;

import java.io.File;

public class ParliamentUtil {

	public static String getBaseCachePath(){
		return System.getProperty("user.home");
	}
	
	public static String getCachePath(String specificDirectory, String fileName) {
		String cachePath = getBaseCachePath();
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(cachePath);
		if (!cachePath.endsWith(File.separator)) {
			sb.append(File.separatorChar);
		}
		
		if (specificDirectory != null && !specificDirectory.isEmpty()){
			sb.append(specificDirectory);
			if (!specificDirectory.endsWith(File.separator)) {
				sb.append(File.separatorChar);
			}
		}
		
		sb.append(fileName);
		return sb.toString();
	}
	
}
