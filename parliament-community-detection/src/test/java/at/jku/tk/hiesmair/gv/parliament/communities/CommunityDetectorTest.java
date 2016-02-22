package at.jku.tk.hiesmair.gv.parliament.communities;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Test;

import at.jku.tk.hiesmair.gv.parliament.communities.graph.Graph;
import at.jku.tk.hiesmair.gv.parliament.communities.graph.Node;

public class CommunityDetectorTest {

	private CommunityDetector detector = new CommunityDetector();

	@Test
	public void testGraph1Interation1() {
		Graph graph = setupGraph1();
		Map<Node, Node> communityMap = detector.detectCommunities(graph, 1);
		
		Map<Long, Long> idResult = new HashMap<>();
		idResult.put(0L, 1L);
		idResult.put(1L, 0L);
		idResult.put(2L, 0L);
		idResult.put(3L, 0L);
		idResult.put(4L, 1L);
		idResult.put(5L, 2L);
		idResult.put(6L, 4L);
		idResult.put(7L, 6L);
		idResult.put(8L, 6L);
		idResult.put(9L, 5L);
		idResult.put(10L, 6L);
		
		checkResult(communityMap, idResult);
	}

	protected void checkResult(Map<Node, Node> communityMap, Map<Long, Long> idResult) {
		for (Node node : communityMap.keySet()) {
			Long nodeId = node.getId();
			Long communityId = communityMap.get(node).getId();
			
			assertEquals(idResult.get(nodeId), communityId);
		}
	}
	
	@Test
	public void testGraph1Interation2() {
		Graph graph = setupGraph1();
		Map<Node, Node> communityMap = detector.detectCommunities(graph, 2);
		
		Map<Long, Long> idResult = new HashMap<>();
		idResult.put(0L, 0L);
		idResult.put(1L, 1L);
		idResult.put(2L, 0L);
		idResult.put(3L, 0L);
		idResult.put(4L, 0L);
		idResult.put(5L, 0L);
		idResult.put(6L, 6L);
		idResult.put(7L, 4L);
		idResult.put(8L, 6L);
		idResult.put(9L, 2L);
		idResult.put(10L, 4L);
		
		checkResult(communityMap, idResult);
	}

	private Graph setupGraph1() {
		Graph graph = new Graph();

		Node node0 = new Node(0L, "node 0");
		Node node1 = new Node(1L, "node 1");
		Node node2 = new Node(2L, "node 2");
		Node node3 = new Node(3L, "node 3");
		Node node4 = new Node(4L, "node 4");
		Node node5 = new Node(5L, "node 5");
		Node node6 = new Node(6L, "node 6");
		Node node7 = new Node(7L, "node 7");
		Node node8 = new Node(8L, "node 8");
		Node node9 = new Node(9L, "node 9");
		Node node10 = new Node(10L, "node 10");
		
		node0.setAdjacentNodes(Arrays.asList(node1, node2, node3).stream().collect(Collectors.toMap(Function.identity(), n -> 1.0)));
		node1.setAdjacentNodes(Arrays.asList(node0, node2, node4).stream().collect(Collectors.toMap(Function.identity(), n -> 1.0)));
		node2.setAdjacentNodes(Arrays.asList(node1, node4, node3, node5, node0).stream().collect(Collectors.toMap(Function.identity(), n -> 1.0)));
		node3.setAdjacentNodes(Arrays.asList(node0, node2, node5).stream().collect(Collectors.toMap(Function.identity(), n -> 1.0)));
		node4.setAdjacentNodes(Arrays.asList(node5, node2, node1, node6).stream().collect(Collectors.toMap(Function.identity(), n -> 1.0)));
		node5.setAdjacentNodes(Arrays.asList(node3, node2, node4, node6, node9).stream().collect(Collectors.toMap(Function.identity(), n -> 1.0)));
		node6.setAdjacentNodes(Arrays.asList(node4, node5, node9, node7, node8, node10).stream().collect(Collectors.toMap(Function.identity(), n -> 1.0)));
		node7.setAdjacentNodes(Arrays.asList(node6, node8).stream().collect(Collectors.toMap(Function.identity(), n -> 1.0)));
		node8.setAdjacentNodes(Arrays.asList(node7, node6, node10).stream().collect(Collectors.toMap(Function.identity(), n -> 1.0)));
		node9.setAdjacentNodes(Arrays.asList(node5, node6, node10).stream().collect(Collectors.toMap(Function.identity(), n -> 1.0)));
		node10.setAdjacentNodes(Arrays.asList(node9, node8, node6).stream().collect(Collectors.toMap(Function.identity(), n -> 1.0)));

		graph.setNodes(new HashSet<Node>(Arrays.asList(node0, node1, node2, node3, node4, node5, node6, node7, node8, node9, node10)));
		return graph;
	}
	
	/*private Graph setupGraph2() {
		Graph graph = new Graph();
		
		Node node0 = new Node(0L, "node 0");
		Node node1 = new Node(1L, "node 1");
		Node node2 = new Node(2L, "node 2");
		Node node3 = new Node(3L, "node 3");
		Node node4 = new Node(4L, "node 4");
		Node node5 = new Node(5L, "node 5");
		Node node6 = new Node(6L, "node 6");
		Node node7 = new Node(7L, "node 7");
		Node node8 = new Node(8L, "node 8");
		
		node0.setAdjacentNodes(Arrays.asList(node1, node2, node3).stream().collect(Collectors.toMap(Function.identity(), n -> 1.0)));
		node1.setAdjacentNodes(Arrays.asList(node0, node2, node3, node4).stream().collect(Collectors.toMap(Function.identity(), n -> 1.0)));
		node2.setAdjacentNodes(Arrays.asList(node0, node1, node3, node8).stream().collect(Collectors.toMap(Function.identity(), n -> 1.0)));
		node3.setAdjacentNodes(Arrays.asList(node0, node1, node2, node4).stream().collect(Collectors.toMap(Function.identity(), n -> 1.0)));
		node4.setAdjacentNodes(Arrays.asList(node1, node3, node5, node6, node7, node8).stream().collect(Collectors.toMap(Function.identity(), n -> 1.0)));
		node5.setAdjacentNodes(Arrays.asList(node3, node2, node4, node6, node9).stream().collect(Collectors.toMap(Function.identity(), n -> 1.0)));
		node6.setAdjacentNodes(Arrays.asList(node4, node5, node9, node7, node8, node10).stream().collect(Collectors.toMap(Function.identity(), n -> 1.0)));
		node7.setAdjacentNodes(Arrays.asList(node6, node8).stream().collect(Collectors.toMap(Function.identity(), n -> 1.0)));
		node8.setAdjacentNodes(Arrays.asList(node7, node6, node10).stream().collect(Collectors.toMap(Function.identity(), n -> 1.0)));
		
		graph.setNodes(Arrays.asList(node0, node1, node2, node3, node4, node5, node6, node7, node8, node9, node10));
		return graph;
	}*/
	
	private Node createNode(Long id) {
		return new Node(id, "node " + id);
	}
}
