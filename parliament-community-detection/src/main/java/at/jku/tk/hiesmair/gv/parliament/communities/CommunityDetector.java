package at.jku.tk.hiesmair.gv.parliament.communities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import at.jku.tk.hiesmair.gv.parliament.communities.graph.Graph;
import at.jku.tk.hiesmair.gv.parliament.communities.graph.Node;

public class CommunityDetector {

	private static Logger logger = Logger.getLogger(CommunityDetector.class.getSimpleName());
	
	/**
	 * main computation method for the community detection.
	 * 
	 * @return map -> key = node, value = communityNode
	 */
	protected <T> Map<Node<T>, Node<T>> getCommunityMap(Graph<T> graph, int iterations) {
		Map<Node<T>, Node<T>> communityMap = getInitialCommunityMap(graph);

		for (int i = 0; i < iterations; i++) {
			//logger.info("ITERATION " + i + " --------------------------------------");
			communityMap = getIterationCommunityMap(communityMap);
//			logger.info("communityMap ");
//			logger.info(communityMap);
//			logger.info("----------------------------------------------------------");
		}

		return communityMap;
	}
	
	/**
	 * Returns the computed communities of the Graph.
	 * 
	 * @return Map with community-ID and List of containing nodes.
	 * 
	 */
	public <T> Map<Long, List<Node<T>>> detectCommunitiesList(Graph<T> graph, int iterations) {
		Map<Node<T>, Node<T>> communityMap = getCommunityMap(graph, iterations);
		
		return getCommunityList(communityMap);
	}

	protected <T> Map<Long, List<Node<T>>> getCommunityList(Map<Node<T>, Node<T>> communityMap) {
		Map<Long, List<Node<T>>> communities = new HashMap<>();
		
		for (Entry<Node<T>, Node<T>> communityEntry : communityMap.entrySet()) {
			Node<T> communityNode = communityEntry.getValue();
			Node<T> node = communityEntry.getKey();
			
			List<Node<T>> communityNodes = communities.get(communityNode.getId());
			if (communityNodes == null) {
				communityNodes = new ArrayList<>();
			}
			communityNodes.add(node);
			
			communities.put(communityNode.getId(), communityNodes);
		}
		
		return communities;
	}

	private <T> Map<Node<T>, Node<T>> getIterationCommunityMap(Map<Node<T>, Node<T>> communityMap) {
		Map<Node<T>, Node<T>> newCommunityMap = new HashMap<>();

		for (Node<T> node : communityMap.keySet()) {
			Node<T> communityNode = getCommunityNode(node, communityMap);

			newCommunityMap.put(node, communityNode);
		}

		return newCommunityMap;
	}

	private <T> Node<T> getCommunityNode(Node<T> node, Map<Node<T>, Node<T>> communityMap) {
		Map<Node<T>, Double> adjacentMap = new HashMap<>();

		for (Entry<Node<T>, Double> adjacent : node.getAdjacentNodes().entrySet()) {
			Node<T> adjacentNode = adjacent.getKey();
			Double edgeWeight = adjacent.getValue();
			
			Node<T> currentAdjacentCommunityNode = communityMap.get(adjacentNode);

			Double currentValue = adjacentMap.get(currentAdjacentCommunityNode);
			if (currentValue == null) {
				currentValue = 0.0;
			}

			adjacentMap.put(currentAdjacentCommunityNode, currentValue + edgeWeight);
		}

		Map.Entry<Node<T>, Double> maxEntry = getMaxMapEntry(adjacentMap);

		return maxEntry.getKey();
	}

	protected <T> Map.Entry<Node<T>, Double> getMaxMapEntry(Map<Node<T>, Double> adjacentMap) {
		Map.Entry<Node<T>, Double> maxEntry = null;
		for (Map.Entry<Node<T>, Double> entry : adjacentMap.entrySet()) {
			if (maxEntry == null || compareEntries(entry, maxEntry) > 0) {
				maxEntry = entry;
			}
		}
		return maxEntry;
	}

	private <T> int compareEntries(Entry<Node<T>, Double> entry, Entry<Node<T>, Double> maxEntry) {
		if (entry.getValue().equals(maxEntry.getValue())) {
			return -entry.getKey().getId().compareTo(maxEntry.getKey().getId());
		}

		return entry.getValue().compareTo(maxEntry.getValue());
	}

	private <T> Map<Node<T>, Node<T>> getInitialCommunityMap(Graph<T> graph) {
		return graph.getNodes().stream().collect(Collectors.toMap(Function.identity(), Function.identity()));
	}

}
