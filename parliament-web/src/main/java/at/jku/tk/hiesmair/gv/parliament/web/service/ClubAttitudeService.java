package at.jku.tk.hiesmair.gv.parliament.web.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import at.jku.tk.hiesmair.gv.parliament.db.repositories.relation.PoliticianAttitudeRelationRepository;
import at.jku.tk.hiesmair.gv.parliament.entities.club.ParliamentClub;
import at.jku.tk.hiesmair.gv.parliament.entities.relation.ClubAttitudeRelationByPeriod;
import at.jku.tk.hiesmair.gv.parliament.web.dto.graph.D3Graph;
import at.jku.tk.hiesmair.gv.parliament.web.dto.graph.D3Link;
import at.jku.tk.hiesmair.gv.parliament.web.dto.graph.D3Node;

@Service
public class ClubAttitudeService {

	@Inject
	private PoliticianAttitudeRelationRepository politicianRelationRep;
	
	public List<ClubAttitudeRelationByPeriod> getClubAttitudes(){
		return politicianRelationRep.getClubAttitudesByPeriods();
	}
	
	public D3Graph getClubAttitudeGraph(Integer period){
		List<ClubAttitudeRelationByPeriod> clubAttitudes = getClubAttitudes();
		
		List<D3Node> nodes = getNodes(clubAttitudes);
		List<D3Link> links = getLinks(clubAttitudes, nodes);
		
		return new D3Graph(nodes, links);
	}

	protected List<D3Link> getLinks(List<ClubAttitudeRelationByPeriod> clubAttitudes, List<D3Node> nodes) {
		List<D3Link> links = new ArrayList<D3Link>();

		Integer maxAbsWeight = clubAttitudes.stream().mapToInt(ca -> Math.abs(ca.getWeight())).max().getAsInt();
		
		for (ClubAttitudeRelationByPeriod clubRelation : clubAttitudes){
			if (!clubRelation.getClub1().equals(clubRelation.getClub2())){
				links.add(getLink(clubRelation, nodes, maxAbsWeight));
			}
		}
		return links;
	}

	protected List<D3Node> getNodes(List<ClubAttitudeRelationByPeriod> clubAttitudes) {
		Set<D3Node> nodes = new HashSet<D3Node>();
		for (ClubAttitudeRelationByPeriod clubRelation : clubAttitudes){
			nodes.add(getNode(clubRelation.getClub1()));
			nodes.add(getNode(clubRelation.getClub2()));
		}
		return new ArrayList<D3Node>(nodes);
	}
	
	protected D3Node getNode(ParliamentClub club) {
		return new D3Node(club.getShortName(), club.getShortName());
	}
	
	protected D3Link getLink(ClubAttitudeRelationByPeriod clubRelation, List<D3Node> nodes, Integer maxAbsWeight){
		Integer sourceIndex = getNodeIndex(clubRelation.getClub1(), nodes);
		Integer targetIndex = getNodeIndex(clubRelation.getClub2(), nodes);
		Double weight = clubRelation.getWeight() / Double.valueOf(maxAbsWeight);
		
		return new D3Link(sourceIndex, targetIndex, weight);
	}
	
	protected Integer getNodeIndex(ParliamentClub club, List<D3Node> nodes) {
		return nodes.indexOf(getNode(club));
	}
}
