package at.jku.tk.hiesmair.gv.parliament.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import at.jku.tk.hiesmair.gv.parliament.communities.CommunityDetector;
import at.jku.tk.hiesmair.gv.parliament.communities.graph.Graph;
import at.jku.tk.hiesmair.gv.parliament.communities.graph.Node;
import at.jku.tk.hiesmair.gv.parliament.db.repositories.relation.PoliticianAttitudeRelationRepository;
import at.jku.tk.hiesmair.gv.parliament.entities.mandate.NationalCouncilMember;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;
import at.jku.tk.hiesmair.gv.parliament.entities.relation.PoliticianAttitudeRelation;
import at.jku.tk.hiesmair.gv.parliament.entities.relation.PoliticianAttitudeRelationByPeriod;
import at.jku.tk.hiesmair.gv.parliament.web.dto.graph.D3Graph;
import at.jku.tk.hiesmair.gv.parliament.web.dto.graph.D3Link;
import at.jku.tk.hiesmair.gv.parliament.web.dto.graph.D3Node;

@Service
public class PoliticianAttitudeService {

	@Inject
	private PoliticianAttitudeRelationRepository politicianRelationRep;

	@Inject
	private CommunityDetector detector;
	
	private List<String> communityColors = new ArrayList<>();
	
	public PoliticianAttitudeService() {
		communityColors.add("#FF0000");
		communityColors.add("#00FF00");
		communityColors.add("#0000FF");
		communityColors.add("#00FFFF");
		communityColors.add("#FF00FF");
		communityColors.add("#FFFF00");
	}
	
	public Page<PoliticianAttitudeRelation> getMostRelatedPoliticians(String politicianId, int offset, int length){
		Page<PoliticianAttitudeRelation> relatedPoliticiansPage = politicianRelationRep.getMostRelatedPoliticians(politicianId, new PageRequest(offset, length));
		
		return relatedPoliticiansPage;
	}
	
	public Page<PoliticianAttitudeRelation> getLeastRelatedPoliticians(String politicianId, int offset, int length){
		Page<PoliticianAttitudeRelation> relatedPoliticiansPage = politicianRelationRep.getLeastRelatedPoliticians(politicianId, new PageRequest(offset, length));
		
		return relatedPoliticiansPage;
	}
	
	public List<PoliticianAttitudeRelationByPeriod> getPoliticianAttitudes() {
		return politicianRelationRep.getPoliticianAttitudesByPeriods();
	}

	public List<PoliticianAttitudeRelationByPeriod> getPoliticianAttitudesByPeriod(Integer period) {
		return politicianRelationRep.getPoliticianAttitudesByPeriod(period);
	}

	public List<PoliticianAttitudeRelationByPeriod> getPoliticianAttitudesByPeriodAndDiscussionTopic(Integer period,
			String discussionTopic) {
		return politicianRelationRep.getPoliticianAttitudesByPeriodAndDiscussionTopic(period, discussionTopic);
	}

	public D3Graph getPoliticianAttitudeGraph(Integer period, Integer minCount, String discussionTopic) {
		List<PoliticianAttitudeRelationByPeriod> politicianAttitudes = getPoliticianAttitudesByPeriodAndDiscussionTopic(
				period, discussionTopic);

		politicianAttitudes = politicianAttitudes.stream().filter(pa -> pa.getCount() >= minCount).collect(Collectors.toList());
		return getPoliticianAttitudeGraph(period, politicianAttitudes);
	}

	public D3Graph getPoliticianAttitudeGraph(Integer period, Integer minCount) {
		List<PoliticianAttitudeRelationByPeriod> politicianAttitudes = getPoliticianAttitudesByPeriod(period);
		
		politicianAttitudes = politicianAttitudes.stream().filter(pa -> pa.getCount() >= minCount).collect(Collectors.toList());	
		return getPoliticianAttitudeGraph(period, politicianAttitudes);
	}

	protected D3Graph getPoliticianAttitudeGraph(Integer period, List<PoliticianAttitudeRelationByPeriod> politicianAttitudes) {
		Graph<Politician> communityGraph = getPoliticianCommunityGraph(politicianAttitudes);
		List<List<Node<Politician>>> communities = computeCommunities(communityGraph);
		
		List<D3Node> nodes = getNodes(politicianAttitudes, period, communities);
		List<D3Link> links = getLinks(politicianAttitudes, nodes, period, communities);

		D3Graph graph = new D3Graph(nodes, links);
//		graph.removeLinks(0.2);

		return graph;
	}

	protected List<D3Link> getLinks(List<PoliticianAttitudeRelationByPeriod> politicianAttitudes, List<D3Node> nodes,
			Integer period, List<List<Node<Politician>>> communities) {
		List<D3Link> links = new ArrayList<D3Link>();
		
		for (PoliticianAttitudeRelationByPeriod politicianRelation : politicianAttitudes) {
			if (!politicianRelation.getPolitician1().equals(politicianRelation.getPolitician2())) {
				links.add(getLink(politicianRelation, nodes, period, communities));
			}
		}
		return links;
	}

	protected Graph<Politician> getPoliticianCommunityGraph(List<PoliticianAttitudeRelationByPeriod> politicianAttitudes) {
		Map<String, Node<Politician>> nodes = new HashMap<>();
		Long currentId = 0L;
		for (PoliticianAttitudeRelationByPeriod politicianAttitude : politicianAttitudes) {
			String politician1 = politicianAttitude.getPolitician1().getId();
			String politician2 = politicianAttitude.getPolitician2().getId();

			Node<Politician> politicianNode1 = nodes.get(politician1);
			if (politicianNode1 == null) {
				politicianNode1 = new Node<Politician>(currentId++, politicianAttitude.getPolitician1().getId(), politicianAttitude.getPolitician1());
			}
			Node<Politician> politicianNode2 = nodes.get(politician2);
			if (politicianNode2 == null) {
				politicianNode2 = new Node<Politician>(currentId++, politicianAttitude.getPolitician2().getId(), politicianAttitude.getPolitician2());
			}
			
			politicianNode1.addAdjacentNode(politicianNode2, politicianAttitude.getNormalizedWeight());
			politicianNode2.addAdjacentNode(politicianNode1, politicianAttitude.getNormalizedWeight());
			
			nodes.put(politician1, politicianNode1);
			nodes.put(politician2, politicianNode2);
		}
		
		Graph<Politician> graph = new Graph<Politician>();
		graph.setNodes(new HashSet<>(nodes.values()));
		return graph;
	}
	
	protected List<List<Node<Politician>>> computeCommunities(Graph<Politician> graph) {
		Map<Long, List<Node<Politician>>> communities = detector.detectCommunitiesList(graph, 30);
		
		return communities.values().stream().sorted((l1, l2) -> l1.size() - l2.size()).collect(Collectors.toList());
	}
	
	protected List<D3Node> getNodes(List<PoliticianAttitudeRelationByPeriod> politicianAttitudes, Integer period, List<List<Node<Politician>>> communities) {
		
		Set<D3Node> nodes = new HashSet<D3Node>();
		for (PoliticianAttitudeRelationByPeriod politicianAttitude : politicianAttitudes) {
			nodes.add(getNode(politicianAttitude.getPolitician1(), period, communities));
			nodes.add(getNode(politicianAttitude.getPolitician2(), period, communities));
		}
		return new ArrayList<D3Node>(nodes);
	}

	protected D3Node getNode(Politician politician, Integer period, List<List<Node<Politician>>> communities) {
		String color = getNodeColor(politician, period);
		String communityColor = getCommunityNodeColor(politician, communities);

		return new D3Node(politician.getId(), politician.getSurName(), color, communityColor);
	}

	private String getCommunityNodeColor(Politician politician, List<List<Node<Politician>>> communities) {

		for (int i = 0; i < communities.size(); i++) {
			List<Node<Politician>> communityPoliticians = communities.get(i);
			for (Node<Politician> node : communityPoliticians) {
				if (node.getObject().equals(politician)) {
					return communityColors.get(i % communityColors.size());
				}
			}
		}
		
		return null;
	}

	protected String getNodeColor(Politician politician, Integer period) {
		List<NationalCouncilMember> ncMandates = politician.getNationalCouncilMemberMandates(period);
		ncMandates.sort((ncm1, ncm2) -> ncm2.getValidFrom().compareTo(ncm1.getValidFrom()));
		
		String color = null;
		if (!ncMandates.isEmpty()) {
			color = ncMandates.get(0).getClub().getColor();
		}
		return color;
	}

	protected D3Link getLink(PoliticianAttitudeRelationByPeriod politicianRelation, List<D3Node> nodes, Integer period, List<List<Node<Politician>>> communities) {
		Integer sourceIndex = getNodeIndex(politicianRelation.getPolitician1(), nodes, period, communities);
		Integer targetIndex = getNodeIndex(politicianRelation.getPolitician2(), nodes, period, communities);
		Double weight = politicianRelation.getNormalizedWeight();

		String color = null;
		String color1 = getNodeColor(politicianRelation.getPolitician1(), period);
		String color2 = getNodeColor(politicianRelation.getPolitician2(), period);
		if (color1 != null && color1.equals(color2)) {
			color = color1;
		}

		return new D3Link(sourceIndex, targetIndex, weight, color);
	}

	protected Integer getNodeIndex(Politician politician, List<D3Node> nodes, Integer period, List<List<Node<Politician>>> communities) {
		return nodes.indexOf(getNode(politician, period, communities));
	}
}
