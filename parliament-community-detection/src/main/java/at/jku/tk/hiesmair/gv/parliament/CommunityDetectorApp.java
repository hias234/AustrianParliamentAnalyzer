package at.jku.tk.hiesmair.gv.parliament;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import at.jku.tk.hiesmair.gv.parliament.communities.CommunityDetector;
import at.jku.tk.hiesmair.gv.parliament.communities.graph.Graph;
import at.jku.tk.hiesmair.gv.parliament.communities.graph.Node;
import at.jku.tk.hiesmair.gv.parliament.db.repositories.relation.PoliticianAttitudeRelationRepository;
import at.jku.tk.hiesmair.gv.parliament.entities.relation.ClubAttitudeRelationByPeriod;

@SpringBootApplication
public class CommunityDetectorApp implements CommandLineRunner {

	private CommunityDetector detector = new CommunityDetector();
	
	@Inject
	private PoliticianAttitudeRelationRepository politicianRelationRep;
	
	public static void main(String[] args) {
		SpringApplication.run(CommunityDetectorApp.class, args);
	}
	
	@Override
	public void run(String... arg0) throws Exception {
		List<ClubAttitudeRelationByPeriod> clubAttitudes = politicianRelationRep.getClubAttitudesByPeriod(25);
		
		Map<String, Node> nodes = new HashMap<>();
		Long currentId = 0L;
		for (ClubAttitudeRelationByPeriod clubAttitude : clubAttitudes) {
			String club1 = clubAttitude.getClub1().getShortName();
			String club2 = clubAttitude.getClub2().getShortName();

			Node clubNode1 = nodes.get(club1);
			if (clubNode1 == null) {
				clubNode1 = new Node(currentId++, club1);
			}
			Node clubNode2 = nodes.get(club2);
			if (clubNode2 == null) {
				clubNode2 = new Node(currentId++, club2);
			}
			
			clubNode1.addAdjacentNode(clubNode2, clubAttitude.getNormalizedWeight());
			clubNode2.addAdjacentNode(clubNode1, clubAttitude.getNormalizedWeight());
			
			nodes.put(club1, clubNode1);
			nodes.put(club2, clubNode2);
		}
		
		Graph graph = new Graph();
		graph.setNodes(new HashSet<>(nodes.values()));
		
		Map<Node, Node> communityMap = detector.detectCommunities(graph, 100);
	}

	
	
}
