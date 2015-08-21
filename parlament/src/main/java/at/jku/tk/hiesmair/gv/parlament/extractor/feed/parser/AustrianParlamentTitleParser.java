package at.jku.tk.hiesmair.gv.parlament.extractor.feed.parser;

/**
 * Correctly parses the title tag from the Austrian parliament 
 * 
 * @author matthias
 *
 */
public class AustrianParlamentTitleParser implements TitleParser {

	@Override
	public String parse(String input) {
		if(input != null) {
			String[] arr = input.split(". ");
			return arr[0];
		}
		return input;
	}

}
