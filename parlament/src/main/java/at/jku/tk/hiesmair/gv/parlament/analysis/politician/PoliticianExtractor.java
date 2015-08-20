package at.jku.tk.hiesmair.gv.parlament.analysis.politician;

import at.jku.tk.hiesmair.gv.parlament.entities.ParliamentData;
import at.jku.tk.hiesmair.gv.parlament.entities.Politician;

public interface PoliticianExtractor {

	Politician getPolitician(String url, ParliamentData parliamentData);
	
}
