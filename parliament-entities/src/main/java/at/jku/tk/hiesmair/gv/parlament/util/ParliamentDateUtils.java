package at.jku.tk.hiesmair.gv.parlament.util;

import java.util.Date;

public class ParliamentDateUtils {

	public static boolean isDateBetween(Date date, Date from, Date to) {
		return date.compareTo(from) >= 0 && (to == null || date.compareTo(to) <= 0);
	}

	
}
