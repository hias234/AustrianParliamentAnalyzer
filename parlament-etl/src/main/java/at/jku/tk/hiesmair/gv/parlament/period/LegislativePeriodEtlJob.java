package at.jku.tk.hiesmair.gv.parlament.period;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import at.jku.tk.hiesmair.gv.parlament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parlament.entities.Politician;
import at.jku.tk.hiesmair.gv.parlament.period.extractor.LegislativePeriodExtractor;
import at.jku.tk.hiesmair.gv.parlament.period.extractor.feed.ProtocolFeedItem;
import at.jku.tk.hiesmair.gv.parlament.period.loader.LegislativePeriodLoader;
import at.jku.tk.hiesmair.gv.parlament.period.transformer.LegislativePeriodTransformer;
import at.jku.tk.hiesmair.gv.parlament.politician.PoliticiansEtlJob;
import at.jku.tk.hiesmair.gv.parlament.politician.extractor.PoliticiansExtractor;
import at.jku.tk.hiesmair.gv.parlament.politician.loader.PoliticiansLoader;
import at.jku.tk.hiesmair.gv.parlament.politician.transformer.PoliticiansTransformer;

@Component
public class LegislativePeriodEtlJob {

	private static Logger logger = Logger.getLogger(LegislativePeriodEtlJob.class.getSimpleName());

	protected LegislativePeriodExtractor extractor;
	protected LegislativePeriodTransformer transformer;
	protected LegislativePeriodLoader loader;

//	@Inject
	protected PoliticiansEtlJob politicianJob;

	@Inject
	public LegislativePeriodEtlJob(LegislativePeriodExtractor extractor, LegislativePeriodTransformer transformer,
			LegislativePeriodLoader loader) {
		super();
		this.extractor = extractor;
		this.transformer = transformer;
		this.loader = loader;
		
		politicianJob = new PoliticiansEtlJob(new PoliticiansExtractor(), new PoliticiansTransformer(), new PoliticiansLoader() {
			
			@Override
			public void loadPoliticians(List<Politician> politicians) {
				
			}
		});
	}

	public void start(List<Integer> periods) throws Exception {
		loadPoliticians();

		logger.info("LegislativePeriodEtlJob started...");

		List<LegislativePeriod> legislativePeriods = new ArrayList<LegislativePeriod>();
		for (Integer period : periods){
			legislativePeriods.add(loadPeriod(period));
		}

		logger.debug("loading " + legislativePeriods.size() + " elements...");
		loader.loadLegislativePeriods(legislativePeriods);
		logger.debug("finished loading");

		logger.info("LegislativePeriodEtlJob finished...");
	}

	protected LegislativePeriod loadPeriod(int period) throws Exception {
		logger.debug("extracting...");
		List<ProtocolFeedItem> extractedData = extractor.extractProtocols(period);
		logger.debug("finished extracting, found " + extractedData.size() + " Protocols");

		logger.debug("transforming...");
		LegislativePeriod legislativePeriod = transformer.getLegislativePeriod(period, extractedData);
		logger.debug("finished transforming");

		return legislativePeriod;
	}
	
	private void loadPoliticians() throws Exception {
		politicianJob.start();
	}

}
