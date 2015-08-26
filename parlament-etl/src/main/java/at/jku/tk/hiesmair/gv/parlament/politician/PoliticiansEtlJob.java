package at.jku.tk.hiesmair.gv.parlament.politician;

import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import at.jku.tk.hiesmair.gv.parlament.entities.Politician;
import at.jku.tk.hiesmair.gv.parlament.politician.extractor.PoliticiansExtractor;
import at.jku.tk.hiesmair.gv.parlament.politician.extractor.feed.PoliticianFeedItem;
import at.jku.tk.hiesmair.gv.parlament.politician.loader.PoliticiansLoader;
import at.jku.tk.hiesmair.gv.parlament.politician.transformer.PoliticiansTransformer;

@Component
public class PoliticiansEtlJob {

	private static Logger logger = Logger.getLogger(PoliticiansEtlJob.class.getSimpleName());

	private PoliticiansExtractor extractor;
	private PoliticiansTransformer transformer;
	private PoliticiansLoader loader;

	@Inject
	public PoliticiansEtlJob(PoliticiansExtractor extractor, PoliticiansTransformer transformer,
			PoliticiansLoader loader) {
		super();
		this.extractor = extractor;
		this.transformer = transformer;
		this.loader = loader;
	}

	public void start() throws Exception {
		logger.info("PoliticiansEtlJob started...");

		logger.debug("extracting...");
		List<PoliticianFeedItem> extractedData = extractor.getPoliticianFeedItems();
		logger.debug("finished extracting, found " + extractedData.size() + " PoliticianFeeds");

		logger.debug("transforming...");
		List<Politician> politicians = transformer.getPoliticians(extractedData);
		logger.debug("finished transforming");

		logger.debug("loading...");
		loader.loadPoliticians(politicians);
		logger.debug("finished loading");

		logger.info("PoliticiansEtlJob finished...");
	}
}
