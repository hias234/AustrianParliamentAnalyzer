package at.jku.tk.hiesmair.gv.parliament.etl.politician.extractor.feed.parser;

import at.jku.tk.hiesmair.gv.parliament.feed.parser.title.TitleParser;

public class PoliticianTitleParser implements TitleParser {

	@Override
	public String parse(String input) {
		return input.replaceAll("[ \\.,-]", "");
	}

}
