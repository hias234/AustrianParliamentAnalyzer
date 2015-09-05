package at.jku.tk.hiesmair.gv.parliament.sentiment;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.stanbol.client.Enhancer;
import org.apache.stanbol.client.StanbolClientFactory;
import org.apache.stanbol.client.enhancer.impl.EnhancerParameters;
import org.apache.stanbol.client.enhancer.impl.EnhancerParameters.EnhancerParametersBuilder;
import org.apache.stanbol.client.enhancer.impl.EnhancerParameters.OutputFormat;
import org.apache.stanbol.client.enhancer.model.EnhancementStructure;
import org.apache.stanbol.client.enhancer.model.EnhancementStructureOntology;
import org.apache.stanbol.client.enhancer.model.TextAnnotation;
import org.apache.stanbol.client.exception.StanbolClientException;
import org.apache.stanbol.client.services.exception.StanbolServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import at.jku.tk.hiesmair.gv.parliament.entities.discussion.speech.sentiment.Sentiment;

@Component
public class StanbolSentimentAnalyzer implements SentimentAnalyzer {

	public static final String STANBOL_GENERATOR = "STANBOL";

	protected final Enhancer enhancer;
	protected final EnhancerParametersBuilder parameterBuilder;

	@Inject
	public StanbolSentimentAnalyzer(@Value("stanbol.endpointurl") String endpointUrl,
			@Value("stanbol.chainname") String chainName) {
		StanbolClientFactory factory = new StanbolClientFactory(endpointUrl);
		enhancer = factory.createEnhancerClient();
		parameterBuilder = EnhancerParameters.builder().setChain(chainName).setOutputFormat(OutputFormat.RDFXML);
	}

	@Override
	public Sentiment getSentiment(String text) {
		EnhancementStructure result = enhance(text);
		TextAnnotation documentSentimentAnnotation = getDocumentSentimentTextAnnotation(text, result);
		return getSentiment(documentSentimentAnnotation);
	}

	protected Sentiment getSentiment(TextAnnotation documentSentimentAnnotation) {
		Double sentiment = documentSentimentAnnotation.getSentiment();
		Double positiveSentiment = documentSentimentAnnotation.getPositiveSentiment();
		Double negativeSentiment = documentSentimentAnnotation.getNegativeSentiment();

		return new Sentiment(STANBOL_GENERATOR, sentiment, positiveSentiment, negativeSentiment);
	}

	protected TextAnnotation getDocumentSentimentTextAnnotation(String text, EnhancementStructure result) {
		List<TextAnnotation> documentSentimentAnnotations = result.getTextAnnotations().stream()
				.filter(ta -> ta.getType().equals(EnhancementStructureOntology.DOCUMENT_SENTIMENT))
				.collect(Collectors.toList());

		if (documentSentimentAnnotations.size() != 1) {
			throw new SentimentAnalyzerException(STANBOL_GENERATOR, text, new RuntimeException(
					"Could not find Document Sentiment"));
		}
		
		TextAnnotation documentSentimentAnnotation = documentSentimentAnnotations.get(0);
		return documentSentimentAnnotation;
	}

	protected EnhancementStructure enhance(String text) {
		EnhancerParameters parameters = parameterBuilder.setContent(text).build();

		EnhancementStructure result = null;

		try {
			result = enhancer.enhance(parameters);
		} catch (StanbolServiceException | StanbolClientException e) {
			throw new SentimentAnalyzerException(STANBOL_GENERATOR, text, e);
		}
		return result;
	}

}
