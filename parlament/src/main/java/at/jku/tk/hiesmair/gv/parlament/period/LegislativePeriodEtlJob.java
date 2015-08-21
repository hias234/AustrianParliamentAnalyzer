package at.jku.tk.hiesmair.gv.parlament.period;

import java.util.List;

import org.apache.log4j.Logger;

import at.jku.tk.hiesmair.gv.parlament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parlament.period.extractor.LegislativePeriodExtractor;
import at.jku.tk.hiesmair.gv.parlament.period.extractor.feed.ProtocolFeedItem;
import at.jku.tk.hiesmair.gv.parlament.period.loader.LegislativePeriodLoader;
import at.jku.tk.hiesmair.gv.parlament.period.transformer.LegislativePeriodTransformer;

public class LegislativePeriodEtlJob {
	
	private static Logger logger = Logger.getLogger(LegislativePeriodEtlJob.class.getSimpleName());

	protected LegislativePeriodExtractor extractor;
	protected LegislativePeriodTransformer transformer;
	protected LegislativePeriodLoader loader;
	
	public LegislativePeriodEtlJob(LegislativePeriodExtractor extractor, LegislativePeriodTransformer transformer,
			LegislativePeriodLoader loader) {
		super();
		this.extractor = extractor;
		this.transformer = transformer;
		this.loader = loader;
	}

	public void start(int period) throws Exception{
		logger.info("LegislativePeriodEtlJob started...");
		
		logger.debug("extracting...");
		List<ProtocolFeedItem> extractedData = extractor.extractProtocols(period);
		logger.debug("finished extracting, found " + extractedData.size() + " Protocols");

		logger.debug("transforming...");
		LegislativePeriod legislativePeriod = transformer.getLegislativePeriod(period, extractedData);
		logger.debug("finished transforming");
		
		logger.debug("loading...");
		loader.loadLegislativePeriod(legislativePeriod);
		logger.debug("finished loading");

		logger.info("LegislativePeriodEtlJob finished...");
	}
	
}