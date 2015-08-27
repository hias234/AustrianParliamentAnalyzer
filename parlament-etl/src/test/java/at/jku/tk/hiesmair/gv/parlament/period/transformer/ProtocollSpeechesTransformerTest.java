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
		if (start.className().equals("ZM")) {
			start = start.nextElementSibling();
		}

		if (start.tagName().equals("b")) {
			start = start.nextElementSibling();
		}

		Elements speechBeginnings = document.select("p.RB");
		for (Element speechBegin : speechBeginnings) {
			String speechText = "";

			Element speechPart = speechBegin.nextElementSibling();
			if (speechPart != null) {
				String firstText = speechPart.text();
				int indexOfColon = firstText.indexOf(":");
				if (indexOfColon != -1) {
					System.out.print(firstText.substring(0, indexOfColon) + ": ");
					speechText += firstText.substring(indexOfColon + 1).trim();
				}
				else {
					speechText += firstText;
				}

				speechPart = speechPart.nextElementSibling();
				for (; speechPart != null && !speechPart.className().equals("RE"); speechPart = speechPart
						.nextElementSibling()) {
					speechText += " " + speechPart.text();
				}

				System.out.println(speechText);
			}
		}

	}

	private Element getBeginningOfSessionElement(Document protocol) {
		Elements spans = protocol.select("span");
		for (Element el : spans) {
			if (el.text().startsWith("Beginn der Sitzung:")) {
				return el.parent();
			}
		}
		return null;
	}
}
