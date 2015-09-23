package at.jku.tk.hiesmair.gv.parliament.web.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import at.jku.tk.hiesmair.gv.parliament.db.repositories.relation.PoliticianAttitudeRelationRepository;
import at.jku.tk.hiesmair.gv.parliament.entities.mandate.NationalCouncilMember;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;
import at.jku.tk.hiesmair.gv.parliament.entities.relation.PoliticianAttitudeRelationByPeriod;
import at.jku.tk.hiesmair.gv.parliament.web.dto.graph.D3Graph;
import at.jku.tk.hiesmair.gv.parliament.web.dto.graph.D3Link;
import at.jku.tk.hiesmair.gv.parliament.web.dto.graph.D3Node;

@Service
public class PoliticianAttitudeService {

	@Inject
	private PoliticianAttitudeRelationRepository politicianRelationRep;
	
	public List<PoliticianAttitudeRelationByPeriod> getPoliticianAttitudes(){
		return politicianRelationRep.getPoliticianAttitudesByPeriods();
	}
	
	public List<PoliticianAttitudeRelationByPeriod> getPoliticianAttitudes(Integer period){
		return politicianRelationRep.getPoliticianAttitudesByPeriod(period);
	}
	
	public D3Graph getPoliticianAttitudeGraph(Integer period){
		List<PoliticianAttitudeRelationByPeriod> clubAttitudes = getPoliticianAttitudes(period);
		
		List<D3Node> nodes = getNodes(clubAttitudes, period);
		List<D3Link> links = getLinks(clubAttitudes, nodes, period);
		
		return new D3Graph(nodes, links);
	}

	protected List<D3Link> getLinks(List<PoliticianAttitudeRelationByPeriod> politicianAttitudes, List<D3Node> nodes, Integer period) {
		List<D3Link> links = new ArrayList<D3Link>();

		for (PoliticianAttitudeRelationByPeriod politicianRelation : politicianAttitudes){
			if (Math.abs(politicianRelation.getWeight()) > 3 && !politicianRelation.getPolitician1().equals(politicianRelation.getPolitician2())) {
				links.add(getLink(politicianRelation, nodes, period));
			}
		}
		return links;
	}

	protected List<D3Node> getNodes(List<PoliticianAttitudeRelationByPeriod> politicianAttitudes, Integer period) {
		Set<D3Node> nodes = new HashSet<D3Node>();
		for (PoliticianAttitudeRelationByPeriod politicianAttitude : politicianAttitudes){
			nodes.add(getNode(politicianAttitude.getPolitician1(), period));
			nodes.add(getNode(politicianAttitude.getPolitician2(), period));
		}
		return new ArrayList<D3Node>(nodes);
	}
	
	protected D3Node getNode(Politician politician, Integer period) {
		List<NationalCouncilMember> ncMandates = politician.getNationalCouncilMemberMandates(period);
		String color = null;
		if (!ncMandates.isEmpty()){
			color = ncMandates.get(0).getClub().getColor();
		}
		
		return new D3Node(politician.getId(), politician.getSurName(), color);
	}
	
	protected D3Link getLink(PoliticianAttitudeRelationByPeriod clubRelation, List<D3Node> nodes, Integer period){
		Integer sourceIndex = getNodeIndex(clubRelation.getPolitician1(), nodes, period);
		Integer targetIndex = getNodeIndex(clubRelation.getPolitician2(), nodes, period);
		Double weight = Double.valueOf(clubRelation.getWeight());
		
		return new D3Link(sourceIndex, targetIndex, weight);
	}
	
	protected Integer getNodeIndex(Politician politician, List<D3Node> nodes, Integer period) {
		return nodes.indexOf(getNode(politician, period));
	}
}
