package at.jku.tk.hiesmair.gv.parlament;

import java.util.List;

import at.jku.tk.hiesmair.gv.parlament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parlament.extractor.LegislativePeriodExtractor;
import at.jku.tk.hiesmair.gv.parlament.extractor.feed.Protocol;
import at.jku.tk.hiesmair.gv.parlament.loader.LegislativePeriodLoader;
import at.jku.tk.hiesmair.gv.parlament.transformer.LegislativePeriodTransformer;

public class LegislativePeriodEtlJob {

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
		List<Protocol> extractedData = extractor.extractProtocols(period);
		LegislativePeriod legislativePeriod = transformer.getLegislativePeriod(period, extractedData);
		loader.loadLegislativePeriod(legislativePeriod);
	}
	
}
