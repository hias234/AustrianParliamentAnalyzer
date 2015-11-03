package at.jku.tk.hiesmair.gv.parliament.web.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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

	public Page<PoliticianAttitudeRelation> getMostRelatedPoliticians(String politicianId, int offset, int length){
		Page<PoliticianAttitudeRelation> relatedPoliticiansPage = politicianRelationRep.getMostRelatedPoliticians(politicianId, new PageRequest(offset, length));
		
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

	public D3Graph getPoliticianAttitudeGraph(Integer period, String discussionTopic) {
		List<PoliticianAttitudeRelationByPeriod> clubAttitudes = getPoliticianAttitudesByPeriodAndDiscussionTopic(
				period, discussionTopic);

		return getPoliticianAttitudeGraph(period, clubAttitudes);
	}

	public D3Graph getPoliticianAttitudeGraph(Integer period) {
		List<PoliticianAttitudeRelationByPeriod> clubAttitudes = getPoliticianAttitudesByPeriod(period);

		return getPoliticianAttitudeGraph(period, clubAttitudes);
	}

	protected D3Graph getPoliticianAttitudeGraph(Integer period, List<PoliticianAttitudeRelationByPeriod> clubAttitudes) {
		List<D3Node> nodes = getNodes(clubAttitudes, period);
		List<D3Link> links = getLinks(clubAttitudes, nodes, period);

		D3Graph graph = new D3Graph(nodes, links);
		graph.removeLinks(0.1);

		return graph;
	}

	protected List<D3Link> getLinks(List<PoliticianAttitudeRelationByPeriod> politicianAttitudes, List<D3Node> nodes,
			Integer period) {
		List<D3Link> links = new ArrayList<D3Link>();

		for (PoliticianAttitudeRelationByPeriod politicianRelation : politicianAttitudes) {
			if (!politicianRelation.getPolitician1().equals(politicianRelation.getPolitician2())) {
				links.add(getLink(politicianRelation, nodes, period));
			}
		}
		return links;
	}

	protected List<D3Node> getNodes(List<PoliticianAttitudeRelationByPeriod> politicianAttitudes, Integer period) {
		Set<D3Node> nodes = new HashSet<D3Node>();
		for (PoliticianAttitudeRelationByPeriod politicianAttitude : politicianAttitudes) {
			nodes.add(getNode(politicianAttitude.getPolitician1(), period));
			nodes.add(getNode(politicianAttitude.getPolitician2(), period));
		}
		return new ArrayList<D3Node>(nodes);
	}

	protected D3Node getNode(Politician politician, Integer period) {
		String color = getNodeColor(politician, period);

		return new D3Node(politician.getId(), politician.getSurName(), color);
	}

	protected String getNodeColor(Politician politician, Integer period) {
		List<NationalCouncilMember> ncMandates = politician.getNationalCouncilMemberMandates(period);
		String color = null;
		if (!ncMandates.isEmpty()) {
			color = ncMandates.get(0).getClub().getColor();
		}
		return color;
	}

	protected D3Link getLink(PoliticianAttitudeRelationByPeriod politicianRelation, List<D3Node> nodes, Integer period) {
		Integer sourceIndex = getNodeIndex(politicianRelation.getPolitician1(), nodes, period);
		Integer targetIndex = getNodeIndex(politicianRelation.getPolitician2(), nodes, period);
		Double weight = Double.valueOf(politicianRelation.getWeight());

		String color = null;
		String color1 = getNodeColor(politicianRelation.getPolitician1(), period);
		String color2 = getNodeColor(politicianRelation.getPolitician2(), period);
		if (color1 != null && color1.equals(color2)) {
			color = color1;
		}

		return new D3Link(sourceIndex, targetIndex, weight, color);
	}

	protected Integer getNodeIndex(Politician politician, List<D3Node> nodes, Integer period) {
		return nodes.indexOf(getNode(politician, period));
	}
}
