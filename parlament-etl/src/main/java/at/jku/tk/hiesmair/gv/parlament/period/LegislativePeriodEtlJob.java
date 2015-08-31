package at.jku.tk.hiesmair.gv.parlament.period;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import at.jku.tk.hiesmair.gv.parlament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parlament.period.extractor.LegislativePeriodExtractor;
import at.jku.tk.hiesmair.gv.parlament.period.extractor.feed.ProtocolFeedItem;
import at.jku.tk.hiesmair.gv.parlament.period.loader.LegislativePeriodLoader;
import at.jku.tk.hiesmair.gv.parlament.period.transformer.LegislativePeriodTransformer;
import at.jku.tk.hiesmair.gv.parlament.politician.PoliticiansEtlJob;

@Component
public class LegislativePeriodEtlJob {

	private static Logger logger = Logger.getLogger(LegislativePeriodEtlJob.class.getSimpleName());

	protected LegislativePeriodExtractor extractor;
	protected LegislativePeriodTransformer transformer;
	protected LegislativePeriodLoader loader;

	@Inject
	protected PoliticiansEtlJob politicianJob;

	@Inject
	public LegislativePeriodEtlJob(LegislativePeriodExtractor extractor, LegislativePeriodTransformer transformer,
			LegislativePeriodLoader loader) {
		super();
		this.extractor = extractor;
		this.transformer = transformer;
		this.loader = loader;
	}

	public void start(List<Integer> periods) throws Exception {
		loadPoliticians();

		logger.info("LegislativePeriodEtlJob started...");

		List<LegislativePeriod> legislativePeriods = new ArrayList<LegislativePeriod>();
		for (Integer period : periods){
			legislativePeriods.add(loadPeriod(period));
		}

		logger.info("loading " + legislativePeriods.size() + " elements...");
		loader.loadLegislativePeriods(legislativePeriods);
		logger.info("finished loading");

		logger.info("LegislativePeriodEtlJob finished...");
	}

	protected LegislativePeriod loadPeriod(int period) throws Exception {
		logger.info("extracting...");
		List<ProtocolFeedItem> extractedData = extractor.extractProtocols(period);
		logger.info("finished extracting, found " + extractedData.size() + " Protocols");

		logger.info("transforming...");
		LegislativePeriod legislativePeriod = transformer.getLegislativePeriod(period, extractedData);
		logger.info("finished transforming");

		return legislativePeriod;
	}
	
	private void loadPoliticians() throws Exception {
		politicianJob.start();
	}

}
