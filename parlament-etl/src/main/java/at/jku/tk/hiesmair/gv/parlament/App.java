package at.jku.tk.hiesmair.gv.parlament;

import java.util.Arrays;

import at.jku.tk.hiesmair.gv.parlament.period.LegislativePeriodEtlJob;
import at.jku.tk.hiesmair.gv.parlament.period.extractor.LegislativePeriodExtractor;
import at.jku.tk.hiesmair.gv.parlament.period.loader.ConsoleLegislativePeriodLoader;
import at.jku.tk.hiesmair.gv.parlament.period.transformer.LegislativePeriodTransformer;

/**
 * Main Application kicking off the analysis process
 */
public class App {

	/**
	 * download everything and analyse everything
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		LegislativePeriodEtlJob job = new LegislativePeriodEtlJob(new LegislativePeriodExtractor(),
				new LegislativePeriodTransformer(), new ConsoleLegislativePeriodLoader());
		
		job.start(Arrays.asList(25, 24, 23, 22, 21, 20));
	}
}
