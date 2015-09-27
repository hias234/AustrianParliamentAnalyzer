package at.jku.tk.hiesmair.gv.parliament.web.dto.activity;

import at.jku.tk.hiesmair.gv.parliament.web.dto.PoliticianDTO;

public class PoliticianActivityDTO {

	private PoliticianDTO politician;
	private Long count;

	public PoliticianDTO getPolitician() {
		return politician;
	}

	public void setPolitician(PoliticianDTO politician) {
		this.politician = politician;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

}
