package at.jku.tk.hiesmair.gv.parliament.web.dto.graph;

public class D3Link {

	private Integer source;
	private Integer target;
	private Integer weight;

	public D3Link(Integer source, Integer target, Integer weight) {
		super();
		this.source = source;
		this.target = target;
		this.weight = weight;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public Integer getTarget() {
		return target;
	}

	public void setTarget(Integer target) {
		this.target = target;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

}
