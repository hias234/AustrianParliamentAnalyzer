package at.jku.tk.hiesmair.gv.parliament.communities.graph;

import java.util.Set;

public class Graph<T> {

	private Set<Node<T>> nodes;

	public Set<Node<T>> getNodes() {
		return nodes;
	}

	public void setNodes(Set<Node<T>> nodes) {
		this.nodes = nodes;
	}
	
	
	
}
