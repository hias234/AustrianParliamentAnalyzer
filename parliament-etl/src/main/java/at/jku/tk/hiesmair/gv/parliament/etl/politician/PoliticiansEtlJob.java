package at.jku.tk.hiesmair.gv.parliament.etl.politician;

import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;
import at.jku.tk.hiesmair.gv.parliament.etl.politician.extractor.PoliticiansExtractor;
import at.jku.tk.hiesmair.gv.parliament.etl.politician.extractor.feed.PoliticianFeedItem;
import at.jku.tk.hiesmair.gv.parliament.etl.politician.loader.PoliticiansLoader;
import at.jku.tk.hiesmair.gv.parliament.etl.politician.transformer.PoliticiansTransformer;

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

		logger.info("extracting...");
		List<PoliticianFeedItem> extractedData = extractor.getPoliticianFeedItems();
		logger.info("finished extracting, found " + extractedData.size() + " PoliticianFeeds");

		logger.info("transforming...");
		List<Politician> politicians = transformer.getPoliticians(extractedData);
		logger.info("finished transforming");

		logger.info("loading...");
		loader.loadPoliticians(politicians);
		logger.info("finished loading");

		logger.info("PoliticiansEtlJob finished...");
	}
}
