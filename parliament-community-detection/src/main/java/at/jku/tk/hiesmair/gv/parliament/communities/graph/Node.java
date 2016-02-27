package at.jku.tk.hiesmair.gv.parliament.communities.graph;

import java.util.HashMap;
import java.util.Map;

public class Node<T> {

	private Long id;
	private String label;
	private T object;
	private Map<Node<T>, Double> adjacentNodes;

	public Node() {
		super();
	}

	public Node(Long id, String label) {
		super();
		this.id = id;
		this.label = label;
	}

	public Node(Long id, String label, T object) {
		super();
		this.id = id;
		this.label = label;
		this.object = object;
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

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	public Map<Node<T>, Double> getAdjacentNodes() {
		return adjacentNodes;
	}

	public void setAdjacentNodes(Map<Node<T>, Double> adjacentNodes) {
		this.adjacentNodes = adjacentNodes;
	}

	public void addAdjacentNode(Node<T> node, Double weight) {
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
		Node<T> other = (Node<T>) obj;
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
