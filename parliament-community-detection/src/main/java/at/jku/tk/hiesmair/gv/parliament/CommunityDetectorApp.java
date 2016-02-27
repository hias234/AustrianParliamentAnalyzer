package at.jku.tk.hiesmair.gv.parliament;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import at.jku.tk.hiesmair.gv.parliament.communities.CommunityDetector;
import at.jku.tk.hiesmair.gv.parliament.communities.graph.Graph;
import at.jku.tk.hiesmair.gv.parliament.communities.graph.Node;
import at.jku.tk.hiesmair.gv.parliament.db.repositories.MandateRepository;
import at.jku.tk.hiesmair.gv.parliament.db.repositories.relation.PoliticianAttitudeRelationRepository;
import at.jku.tk.hiesmair.gv.parliament.entities.club.ParliamentClub;
import at.jku.tk.hiesmair.gv.parliament.entities.mandate.NationalCouncilMember;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;
import at.jku.tk.hiesmair.gv.parliament.entities.relation.ClubAttitudeRelationByPeriod;
import at.jku.tk.hiesmair.gv.parliament.entities.relation.PoliticianAttitudeRelationByPeriod;

@SpringBootApplication
public class CommunityDetectorApp implements CommandLineRunner {

	private static Logger logger = Logger.getLogger(CommunityDetectorApp.class.getSimpleName());
	
	private CommunityDetector detector = new CommunityDetector();
	
	@Inject
	private PoliticianAttitudeRelationRepository politicianRelationRep;
	
	@Inject
	private MandateRepository mandateRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(CommunityDetectorApp.class, args);
	}
	
	@Override
	public void run(String... arg0) throws Exception {
		Map<Integer, List<String>> governmentParties = new HashMap<>();
		governmentParties.put(20, Arrays.asList("ÖVP", "SPÖ"));
		governmentParties.put(21, Arrays.asList("ÖVP", "F"));
		governmentParties.put(22, Arrays.asList("ÖVP", "F", "F-BZÖ"));
		governmentParties.put(23, Arrays.asList("ÖVP", "SPÖ"));
		governmentParties.put(24, Arrays.asList("ÖVP", "SPÖ"));
		governmentParties.put(25, Arrays.asList("ÖVP", "SPÖ"));
		
		Map<Integer, Integer> thresholdWeights = new HashMap<>();
		thresholdWeights.put(20, 3);
		thresholdWeights.put(21, 3);
		thresholdWeights.put(22, 3);
		thresholdWeights.put(23, 3);
		thresholdWeights.put(24, 3);
		thresholdWeights.put(25, 3);
		
		
		for (int period = 20; period <= 25; period++) {
		
			System.out.println("PERIOD " + period);
			System.out.println("clubs");
			getClubCommunities(period);
			
			System.out.println();
			System.out.println("politicians");
			Map<Long, List<Node<Politician>>> pCommunities = getPoliticianCommunities(period, thresholdWeights.get(period));
			Double percentage = getPercentage(pCommunities, governmentParties.get(period), period);
			System.out.println("Correctness: " + percentage);
			System.out.println("----------------------------------------------------------------");
			System.out.println();
			System.out.println();
		}
	}

	private Map<Long, List<Node<Politician>>> getPoliticianCommunities(int period, int thresholdWeight) {
		List<PoliticianAttitudeRelationByPeriod> politicianAttitudes = politicianRelationRep.getPoliticianAttitudesByPeriod(period);
		
		// filter by weight
		politicianAttitudes = politicianAttitudes.stream().filter(pa -> pa.getWeight() >= thresholdWeight).collect(Collectors.toList());
		
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

		Map<Long, List<Node<Politician>>> communities = detector.detectCommunitiesList(graph, 30); // wieviel iterationen? kanten threshold?
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

	protected Double getPercentage(Map<Long, List<Node<Politician>>> pCommunities, List<String> governingParties, Integer period) {
		
		if (pCommunities.size() != 2) {
			return 0.0;
		}
		
		Iterator<List<Node<Politician>>> communityIter = pCommunities.values().iterator();
		List<Node<Politician>> community1 = communityIter.next();
		List<Node<Politician>> community2 = communityIter.next();
		
		Double percentOfGovernmentPoliticiansInCommunity1 = getPercentOfGovernmentPoliticians(community1, governingParties, period);
		Double percentOfGovernmentPoliticiansInCommunity2 = getPercentOfGovernmentPoliticians(community2, governingParties, period);
		
		if (percentOfGovernmentPoliticiansInCommunity1 > percentOfGovernmentPoliticiansInCommunity2) {
			return (percentOfGovernmentPoliticiansInCommunity1 + 1 - percentOfGovernmentPoliticiansInCommunity2) / 2;
		}
		
		return (percentOfGovernmentPoliticiansInCommunity2 + 1 - percentOfGovernmentPoliticiansInCommunity1) / 2;
	}

	private Double getPercentOfGovernmentPoliticians(List<Node<Politician>> community, List<String> governingParties, Integer period) {
		Integer inGov = 0;
		Integer inNcm = 0;
		
		List<Politician> politiciansInGov = new ArrayList<>();
		List<Politician> politiciansInOpp = new ArrayList<>();
		
		for (Node<Politician> node : community) {
			Politician politician = node.getObject();
			List<NationalCouncilMember> ncmMandatesInPeriod = mandateRepository.findNationalCouncilMembersOfPoliticianAndPeriod(politician.getId(), period);
			
			boolean foundGovParty = false;
			for (NationalCouncilMember ncm : ncmMandatesInPeriod) {
				for (String governingParty : governingParties) {
					if (ncm.getClub().getShortName().equals(governingParty)) {
						inGov++;
						foundGovParty = true;
						break;
					}
				}
				if (foundGovParty) {
					break;
				}
			}
			
			if (!ncmMandatesInPeriod.isEmpty()) {
				inNcm++;
				
				if (foundGovParty) {
					politiciansInGov.add(politician);
				}
				else {
					politiciansInOpp.add(politician);
				}
			}
		}
		
		if (politiciansInGov.size() > politiciansInOpp.size()) {
			System.out.print("Wrong in Gov: ");
			politiciansInOpp.forEach(p -> System.out.print(p.getFullName() + ", "));
		}
		else {
			System.out.print("Wrong in Opp: ");
			politiciansInGov.forEach(p -> System.out.print(p.getFullName() + ", "));
		}
		System.out.println();
		
		return inGov / (double)inNcm;
	}
	
}
