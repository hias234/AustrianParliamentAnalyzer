package at.jku.tk.hiesmair.gv.parliament.web.dto.graph;

import java.util.List;

public class D3Graph {

	private List<D3Node> nodes;
	private List<D3Link> links;

	public D3Graph(List<D3Node> nodes, List<D3Link> links) {
		super();
		this.nodes = nodes;
		this.links = links;
	}

	public List<D3Node> getNodes() {
		return nodes;
	}

	public void setNodes(List<D3Node> nodes) {
		this.nodes = nodes;
	}

	public List<D3Link> getLinks() {
		return links;
	}

	public void setLinks(List<D3Link> links) {
		this.links = links;
	}
}
