package at.jku.tk.hiesmair.gv.parliament;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import at.jku.tk.hiesmair.gv.parliament.communities.CommunityDetector;
import at.jku.tk.hiesmair.gv.parliament.communities.graph.Graph;
import at.jku.tk.hiesmair.gv.parliament.communities.graph.Node;
import at.jku.tk.hiesmair.gv.parliament.db.repositories.relation.PoliticianAttitudeRelationRepository;
import at.jku.tk.hiesmair.gv.parliament.entities.club.ParliamentClub;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;
import at.jku.tk.hiesmair.gv.parliament.entities.relation.ClubAttitudeRelationByPeriod;
import at.jku.tk.hiesmair.gv.parliament.entities.relation.PoliticianAttitudeRelationByPeriod;

@SpringBootApplication
public class CommunityDetectorApp implements CommandLineRunner {

	private static Logger logger = Logger.getLogger(CommunityDetectorApp.class.getSimpleName());
	
	private CommunityDetector detector = new CommunityDetector();
	
	@Inject
	private PoliticianAttitudeRelationRepository politicianRelationRep;
	
	public static void main(String[] args) {
		SpringApplication.run(CommunityDetectorApp.class, args);
	}
	
	@Override
	public void run(String... arg0) throws Exception {
		
		
		for (int period = 20; period <= 25; period++) {
		
			System.out.println("PERIOD " + period);
			System.out.println("clubs");
			getClubCommunities(period);
			
			System.out.println();
			System.out.println("politicians");
			Map<Long, List<Node<Politician>>> pCommunities = getPoliticianCommunities(period);
			System.out.println("----------------------------------------------------------------");
			System.out.println();
			System.out.println();
		}
	}

	private Map<Long, List<Node<Politician>>> getPoliticianCommunities(int period) {
		List<PoliticianAttitudeRelationByPeriod> politicianAttitudes = politicianRelationRep.getPoliticianAttitudesByPeriod(period);
		
		Map<String, Node<Politician>> nodes = new HashMap<>();
		Long currentId = 0L;
		for (PoliticianAttitudeRelationByPeriod politicianAttitude : politicianAttitudes) {
			String politician1 = politicianAttitude.getPolitician1().getId();
			String politician2 = politicianAttitude.getPolitician2().getId();

			Node<Politician> politicianNode1 = nodes.get(politician1);
			if (politicianNode1 == null) {
				politicianNode1 = new Node<Politician>(currentId++, politicianAttitude.getPolitician1().getFullName(), politicianAttitude.getPolitician1());
			}
			Node<Politician> politicianNode2 = nodes.get(politician2);
			if (politicianNode2 == null) {
				politicianNode2 = new Node<Politician>(currentId++, politicianAttitude.getPolitician2().getFullName(), politicianAttitude.getPolitician2());
			}
			
			politicianNode1.addAdjacentNode(politicianNode2, politicianAttitude.getNormalizedWeight());
			politicianNode2.addAdjacentNode(politicianNode1, politicianAttitude.getNormalizedWeight());
			
			nodes.put(politician1, politicianNode1);
			nodes.put(politician2, politicianNode2);
		}
		
		Graph<Politician> graph = new Graph<Politician>();
		graph.setNodes(new HashSet<>(nodes.values()));

		Map<Long, List<Node<Politician>>> communities = detector.detectCommunitiesList(graph, 10); // wieviel iterationen? kanten threshold?
		showCommunities(communities);
		
		return communities;
	}

	private <T> void showCommunities(Map<Long, List<Node<T>>> communities) {
		for (Long id : communities.keySet()) {
			System.out.print(id + " ");
			for (Node<T> node : communities.get(id)) {
				System.out.print(node.getLabel() + ", ");
			}
			System.out.println();
		}
	}

	protected Map<Long, List<Node<ParliamentClub>>> getClubCommunities(int period) {
		List<ClubAttitudeRelationByPeriod> clubAttitudes = politicianRelationRep.getClubAttitudesByPeriod(period);
		
		Map<String, Node<ParliamentClub>> nodes = new HashMap<>();
		Long currentId = 0L;
		for (ClubAttitudeRelationByPeriod clubAttitude : clubAttitudes) {
			String club1 = clubAttitude.getClub1().getShortName();
			String club2 = clubAttitude.getClub2().getShortName();

			Node<ParliamentClub> clubNode1 = nodes.get(club1);
			if (clubNode1 == null) {
				clubNode1 = new Node<ParliamentClub>(currentId++, club1, clubAttitude.getClub1());
			}
			Node<ParliamentClub> clubNode2 = nodes.get(club2);
			if (clubNode2 == null) {
				clubNode2 = new Node<ParliamentClub>(currentId++, club2, clubAttitude.getClub2());
			}
			
			clubNode1.addAdjacentNode(clubNode2, clubAttitude.getNormalizedWeight());
			clubNode2.addAdjacentNode(clubNode1, clubAttitude.getNormalizedWeight());
			
			nodes.put(club1, clubNode1);
			nodes.put(club2, clubNode2);
		}
		
		Graph<ParliamentClub> graph = new Graph<ParliamentClub>();
		graph.setNodes(new HashSet<>(nodes.values()));

		Map<Long, List<Node<ParliamentClub>>> communities = detector.detectCommunitiesList(graph, 100);
		showCommunities(communities);
		
		return communities;
	}

	
	
}
