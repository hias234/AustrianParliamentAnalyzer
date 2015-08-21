package at.jku.tk.hiesmair.gv.parlament.period.extractor.feed.parser;

import at.jku.tk.hiesmair.gv.parlament.feed.parser.title.TitleParser;

/**
 * Correctly parses the title tag from the Austrian parliament 
 * 
 * @author matthias
 *
 */
public class SessionTitleParser implements TitleParser {

	@Override
	public String parse(String input) {
		if(input != null) {
			String[] arr = input.split(". ");
			return arr[0];
		}
		return input;
	}

}
