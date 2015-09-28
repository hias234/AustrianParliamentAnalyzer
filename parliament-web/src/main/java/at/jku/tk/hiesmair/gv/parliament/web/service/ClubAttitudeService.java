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
	
	public List<ClubAttitudeRelationByPeriod> getClubAttitudes(Integer period){
		List<ClubAttitudeRelationByPeriod> clubAttitudes = politicianRelationRep.getClubAttitudesByPeriod(period);
		
		// merge if ca1.getclub2 == ca1.getclub1 and vice verca
		for (int i = 0; i < clubAttitudes.size(); i++){
			ClubAttitudeRelationByPeriod ca1 = clubAttitudes.get(i);
			for (int j = i + 1; j < clubAttitudes.size(); j++){
				ClubAttitudeRelationByPeriod ca2 = clubAttitudes.get(j);
				
				if (ca1.getClub1().equals(ca2.getClub2()) && ca1.getClub2().equals(ca2.getClub1())){
					ca1.setWeight(ca1.getWeight() + ca2.getWeight());
					clubAttitudes.remove(j);
					j--;
				}
			}
		}
		
		return clubAttitudes;
	}
	
	public D3Graph getClubAttitudeGraph(Integer period){
		List<ClubAttitudeRelationByPeriod> clubAttitudes = getClubAttitudes(period);
		
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
		return new D3Node(club.getShortName(), club.getShortName(), club.getColor());
	}
	
	protected D3Link getLink(ClubAttitudeRelationByPeriod clubRelation, List<D3Node> nodes, Integer maxAbsWeight){
		Integer sourceIndex = getNodeIndex(clubRelation.getClub1(), nodes);
		Integer targetIndex = getNodeIndex(clubRelation.getClub2(), nodes);
		
//		Double weight = 1.0 - (clubRelation.getWeight() + maxAbsWeight) / Double.valueOf(maxAbsWeight * 2);
		Double weight = Double.valueOf(clubRelation.getWeight());
		
		return new D3Link(sourceIndex, targetIndex, weight);
	}
	
	protected Integer getNodeIndex(ParliamentClub club, List<D3Node> nodes) {
		return nodes.indexOf(getNode(club));
	}
}
