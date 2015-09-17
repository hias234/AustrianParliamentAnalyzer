package at.jku.tk.hiesmair.gv.parliament.etl.period.extractor.feed.parser;

import at.jku.tk.hiesmair.gv.parliament.feed.parser.title.TitleParser;

/**
 * Correctly parses the title tag from the Austrian parliament 
 * 
 * @author matthias
 *
 */
public class SessionTitleParser implements TitleParser {

	protected String period;
	
	public SessionTitleParser(String period) {
		this.period = period;
	}
	
	@Override
	public String parse(String input) {
		return input;
	}

}
