package at.jku.tk.hiesmair.gv.parliament.sentiment;

import java.util.Properties;

import org.junit.Test;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

public class Test2 {
	
	@Test
	public void test(){
		Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        props.setProperty("sentiment.model", "models-german");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        
        int mainSentiment = 0;
        String line = "Sehr geehrte Dame und sehr geehrte Herren aus Süd­tirol!\u00A0\u2013 Wir haben einander ja gestern schon getroffen.\u00A0\u2013 Meine sehr geehrten Damen und Herren, wie Sie alle wissen, bin ich burgenländische Kroatin, und darum bin ich an dieser Entschließung, die schon im Außenpolitischen Ausschuss mit großer Mehrheit getroffen worden ist, sehr interessiert, weil diese, zumindest auf den ersten Blick, den Eindruck erweckt, als wäre das eine enorm wichtige Angelegenheit und Sache den Minderheitenschutz in Österreich betreffend. Auf den ersten Blick könnte man sich davon täuschen lassen, denn Worte, die auch manchmal sehr martialisch aufgefasst werden können, wie zum Beispiel das Wort \u201ESchutzfunktion\u201C\u00A0\u2013 das Wort \u201EMacht\u201C kommt ja nicht mehr in dieser Entschließung vor, aber \u201ESchutzfunktion\u201C\u00A0\u2013, können ja auch positive Emotion auslösen, weil sie bedeuten: Da gibt es jemanden, der sich ganz intensiv und heftig für etwas einsetzt\u00A0\u2013 und in die­sem Fall auch mit dem Wunsch, etwas in der österreichischen Bundesverfassung zu verankern, was sozusagen die intensivste politische Möglichkeit ist, einem Anliegen Ausdruck zu verleihen.";
        if (line != null && line.length() > 0) {
            int longest = 0;
            Annotation annotation = pipeline.process(line);
            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                String partText = sentence.toString();
                if (partText.length() > longest) {
                    mainSentiment = sentiment;
                    longest = partText.length();
                }
 
            }
        }
        if (mainSentiment == 2 || mainSentiment > 4 || mainSentiment < 0) {
            
        }
        
        System.out.println(mainSentiment);
	}
	
}
