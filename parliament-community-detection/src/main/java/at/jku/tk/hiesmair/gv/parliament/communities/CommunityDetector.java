package at.jku.tk.hiesmair.gv.parliament.communities;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import at.jku.tk.hiesmair.gv.parliament.communities.graph.Graph;
import at.jku.tk.hiesmair.gv.parliament.communities.graph.Node;

public class CommunityDetector {

	private static Logger logger = Logger.getLogger(CommunityDetector.class.getSimpleName());
	
	public Map<Node, Node> detectCommunities(Graph graph, int iterations) {
		Map<Node, Node> communityMap = getInitialCommunityMap(graph);

		for (int i = 0; i < iterations; i++) {
			logger.info("ITERATION " + i + " --------------------------------------");
			communityMap = getIterationCommunityMap(communityMap);
			logger.info("communityMap ");
			logger.info(communityMap);
			logger.info("----------------------------------------------------------");
		}

		return communityMap;
	}

	private Map<Node, Node> getIterationCommunityMap(Map<Node, Node> communityMap) {
		Map<Node, Node> newCommunityMap = new HashMap<>();

		for (Node node : communityMap.keySet()) {
			Node communityNode = getCommunityNode(node, communityMap);

			newCommunityMap.put(node, communityNode);
		}

		return newCommunityMap;
	}

	private Node getCommunityNode(Node node, Map<Node, Node> communityMap) {
		Map<Node, Double> adjacentMap = new HashMap<>();

		for (Entry<Node, Double> adjacent : node.getAdjacentNodes().entrySet()) {
			Node adjacentNode = adjacent.getKey();
			Double edgeWeight = adjacent.getValue();
			
			Node currentAdjacentCommunityNode = communityMap.get(adjacentNode);

			Double currentValue = adjacentMap.get(currentAdjacentCommunityNode);
			if (currentValue == null) {
				currentValue = 0.0;
			}

			adjacentMap.put(currentAdjacentCommunityNode, currentValue + edgeWeight);
		}

		Map.Entry<Node, Double> maxEntry = getMaxMapEntry(adjacentMap);

		return maxEntry.getKey();
	}

	protected Map.Entry<Node, Double> getMaxMapEntry(Map<Node, Double> adjacentMap) {
		Map.Entry<Node, Double> maxEntry = null;
		for (Map.Entry<Node, Double> entry : adjacentMap.entrySet()) {
			if (maxEntry == null || compareEntries(entry, maxEntry) > 0) {
				maxEntry = entry;
			}
		}
		return maxEntry;
	}

	private int compareEntries(Entry<Node, Double> entry, Entry<Node, Double> maxEntry) {
		if (entry.getValue().equals(maxEntry.getValue())) {
			return -entry.getKey().getId().compareTo(maxEntry.getKey().getId());
		}

		return entry.getValue().compareTo(maxEntry.getValue());
	}

	private Map<Node, Node> getInitialCommunityMap(Graph graph) {
		return graph.getNodes().stream().collect(Collectors.toMap(Function.identity(), Function.identity()));
	}

}
