package at.jku.tk.hiesmair.gv.parlament.period.transformer;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import at.jku.tk.hiesmair.gv.parlament.period.protocol.ProtocolUtil;

public class ProtocollSpeechesTransformerTest {

	@Test
	public void test() throws Exception {
		Document document = Jsoup.parse(new File("C:\\Temp\\parlament\\protocol_64.html"), "UTF-8");
		document = ProtocolUtil.filterPageBreaks(document);
		
		Elements spans = document.select("span");
		Element beginningElement = null;
		Element endingElement = null;
		for (Element el : spans){
			if (el.text().startsWith("Beginn der Sitzung:")){
				beginningElement = el.parent();
			}
			else if (el.text().startsWith("Schluss der Sitzung:")){
				endingElement = el.parent();
			}
		}
		
		Element chairMen = beginningElement.nextElementSibling();
		if (chairMen.text().startsWith("Vorsitzend")){
			
			Elements chairMenLinks = chairMen.select("a");
			assertEquals(3, chairMenLinks.size());
		}
		
		
	}
	
}
