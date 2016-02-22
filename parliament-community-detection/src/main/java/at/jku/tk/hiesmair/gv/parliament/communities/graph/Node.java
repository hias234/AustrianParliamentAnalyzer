package at.jku.tk.hiesmair.gv.parliament.communities.graph;

import java.util.HashMap;
import java.util.Map;

public class Node {

	private Long id;
	private String label;
	private Map<Node, Double> adjacentNodes;

	public Node() {
		super();
	}

	public Node(Long id, String label) {
		super();
		this.id = id;
		this.label = label;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Map<Node, Double> getAdjacentNodes() {
		return adjacentNodes;
	}

	public void setAdjacentNodes(Map<Node, Double> adjacentNodes) {
		this.adjacentNodes = adjacentNodes;
	}
	
	public void addAdjacentNode(Node node, Double weight) {
		if (adjacentNodes == null) {
			adjacentNodes = new HashMap<>();
		}
		
		adjacentNodes.put(node, weight);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return label;
	}
}
