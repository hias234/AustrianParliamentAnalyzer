package at.jku.tk.hiesmair.gv.parlament.period.transformer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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

		Element beginningElement = getBeginningOfSessionElement(document);

		Element chairMen = beginningElement.nextElementSibling();
		if (chairMen.text().startsWith("Vorsitzend")) {

			Elements chairMenLinks = chairMen.select("a");
			assertEquals(3, chairMenLinks.size());
		}
		else {
			fail();
		}

		Element start = chairMen.nextElementSibling();
		if (start.className().equals("ZM")){
			start = start.nextElementSibling();
		}

		if (start.tagName().equals("b")){
			start = start.nextElementSibling();
		}
		
		
	}

	private Element getBeginningOfSessionElement(Document protocol){
		Elements spans = protocol.select("span");
		Element beginningElement = null;
		for (Element el : spans){
			if (el.text().startsWith("Beginn der Sitzung:")){
				return el.parent();
			}
		}
		return null;
	}
}
