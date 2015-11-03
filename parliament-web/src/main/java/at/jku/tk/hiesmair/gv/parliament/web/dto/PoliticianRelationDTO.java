package at.jku.tk.hiesmair.gv.parliament.web.dto;

import at.jku.tk.hiesmair.gv.parliament.web.dto.politician.PoliticianDTO;

public class PoliticianRelationDTO {

	private PoliticianDTO politician1;
	private PoliticianDTO politician2;
	private Integer weight;

	public PoliticianRelationDTO() {
		super();
	}

	public PoliticianRelationDTO(PoliticianDTO politician1, PoliticianDTO politician2, Integer weight) {
		super();
		this.politician1 = politician1;
		this.politician2 = politician2;
		this.weight = weight;
	}

	public PoliticianDTO getPolitician1() {
		return politician1;
	}

	public void setPolitician1(PoliticianDTO politician1) {
		this.politician1 = politician1;
	}

	public PoliticianDTO getPolitician2() {
		return politician2;
	}

	public void setPolitician2(PoliticianDTO politician2) {
		this.politician2 = politician2;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

}
