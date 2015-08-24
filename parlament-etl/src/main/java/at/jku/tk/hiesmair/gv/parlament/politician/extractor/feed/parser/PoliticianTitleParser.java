package at.jku.tk.hiesmair.gv.parlament.politician.extractor.feed.parser;

import at.jku.tk.hiesmair.gv.parlament.feed.parser.title.TitleParser;

public class PoliticianTitleParser implements TitleParser {

	@Override
	public String parse(String input) {
		return input.replaceAll("[ \\.,-]", "");
	}

}
