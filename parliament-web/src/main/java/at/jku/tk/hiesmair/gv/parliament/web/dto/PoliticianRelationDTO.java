package at.jku.tk.hiesmair.gv.parliament.web.dto;

import at.jku.tk.hiesmair.gv.parliament.web.dto.politician.PoliticianDTO;

public class PoliticianRelationDTO {

	private PoliticianDTO politician1;
	private PoliticianDTO politician2;
	private Integer weight;
	private Integer count;

	public PoliticianRelationDTO() {
		super();
	}

	public PoliticianRelationDTO(PoliticianDTO politician1, PoliticianDTO politician2, Integer weight, Integer count) {
		super();
		this.politician1 = politician1;
		this.politician2 = politician2;
		this.weight = weight;
		this.count = count;
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

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Double getNormalizedWeight(){
		return weight / (double) count;
	}
}
