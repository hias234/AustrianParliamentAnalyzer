package at.jku.tk.hiesmair.gv.parliament.db.result;

import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

public class PoliticianActivityResult {

	private Politician politician;
	private Long count;

	public PoliticianActivityResult() {
		super();
	}

	public PoliticianActivityResult(Politician politician, Long count) {
		super();
		this.politician = politician;
		this.count = count;
	}

	public Politician getPolitician() {
		return politician;
	}

	public void setPolitician(Politician politician) {
		this.politician = politician;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

}
