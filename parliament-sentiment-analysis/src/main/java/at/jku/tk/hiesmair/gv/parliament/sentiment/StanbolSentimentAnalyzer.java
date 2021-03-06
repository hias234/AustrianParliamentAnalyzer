package at.jku.tk.hiesmair.gv.parliament.sentiment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import at.jku.tk.hiesmair.gv.parliament.entities.discussion.speech.sentiment.Sentiment;

/**
 * Analyzes the sentiment of a text using the apache stanbol api.
 * 
 * @author Markus
 *
 */
//@Component
public class StanbolSentimentAnalyzer implements SentimentAnalyzer {

	private static final Logger logger = Logger.getLogger(StanbolSentimentAnalyzer.class.getSimpleName());

	public static final String STANBOL_GENERATOR = "STANBOL";

	//protected final Enhancer enhancer;
	//protected final EnhancerParametersBuilder parameterBuilder;
	protected final Integer retryCycles;

	@Inject
	public StanbolSentimentAnalyzer(@Value("${stanbol.endpointurl}") String endpointUrl,
			@Value("${stanbol.chainname}") String chainName, @Value("${stanbol.retryCycles}") Integer retryCycles) {
		this.retryCycles = retryCycles;

//		StanbolClientFactory factory = new StanbolClientFactory(endpointUrl);
//		enhancer = factory.createEnhancerClient();
//		parameterBuilder = EnhancerParameters.builder().setChain(chainName).setOutputFormat(OutputFormat.RDFXML);
	}

	@Override
	public List<Sentiment> getSentiments(String text) {
//		TextAnnotation documentSentimentAnnotation = null;
//
//		int waitAfterUnsuccessfulTry = 25;
//
//		for (int i = 0; i < retryCycles && documentSentimentAnnotation == null; i++) {
//			if (i > 0) {
//				try {
//					Thread.sleep(waitAfterUnsuccessfulTry);
//				} catch (InterruptedException e) {
//				}
//			}
//			EnhancementStructure result = enhance(text);
//			documentSentimentAnnotation = getDocumentSentimentTextAnnotation(text, result);
//
//			waitAfterUnsuccessfulTry = Math.min(waitAfterUnsuccessfulTry += 25, 1000);
//		}
//
//		if (documentSentimentAnnotation == null) {
//			throw new SentimentAnalyzerException(STANBOL_GENERATOR, text, new RuntimeException(
//					"Could not find Document Sentiment"));
//		}
//
//		return Arrays.asList(getSentiment(documentSentimentAnnotation));
		
		return new ArrayList<Sentiment>();
	}

//	protected Sentiment getSentiment(TextAnnotation documentSentimentAnnotation) {
//		Double sentiment = documentSentimentAnnotation.getSentiment();
//		Double positiveSentiment = documentSentimentAnnotation.getPositiveSentiment();
//		Double negativeSentiment = documentSentimentAnnotation.getNegativeSentiment();
//
//		return new Sentiment(STANBOL_GENERATOR, sentiment, positiveSentiment, negativeSentiment);
//	}
//
//	protected TextAnnotation getDocumentSentimentTextAnnotation(String text, EnhancementStructure result) {
//		for (TextAnnotation ta : result.getTextAnnotations()) {
//			if (ta.getType().equals(EnhancementStructureOntology.DOCUMENT_SENTIMENT.toString())) {
//				return ta;
//			}
//		}
//
//		return null;
//	}
//
//	protected EnhancementStructure enhance(String text) {
//		EnhancerParameters parameters = parameterBuilder.setContent(text).build();
//
//		EnhancementStructure result = null;
//
//		try {
//			result = enhancer.enhance(parameters);
//		} catch (StanbolServiceException | StanbolClientException e) {
//			throw new SentimentAnalyzerException(STANBOL_GENERATOR, text, e);
//		}
//		return result;
//	}

}
