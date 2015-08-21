package at.jku.tk.hiesmair.gv.parlament.extractor.feed.parser;

/**
 * Interface that can be used to parse titles
 * 
 * @author matthias
 *
 */
public interface TitleParser {

	/** Can be used to convert the title tag */
	public String parse(String input);
	
}
