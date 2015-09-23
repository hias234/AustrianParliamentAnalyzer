package at.jku.tk.hiesmair.gv.parliament.web.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import at.jku.tk.hiesmair.gv.parliament.db.repositories.ParliamentClubRepository;
import at.jku.tk.hiesmair.gv.parliament.db.repositories.relation.PoliticianAttitudeRelationRepository;
import at.jku.tk.hiesmair.gv.parliament.entities.club.ParliamentClub;
import at.jku.tk.hiesmair.gv.parliament.web.dto.PoliticianDTO;
import at.jku.tk.hiesmair.gv.parliament.web.dto.graph.D3Link;
import at.jku.tk.hiesmair.gv.parliament.web.dto.graph.D3Node;
import at.jku.tk.hiesmair.gv.parliament.web.service.PoliticianService;

@RestController
@RequestMapping("politician/")
public class PoliticianController {

	@Inject
	private PoliticianService politicianService;

	@Inject
	private ModelMapper mapper;

	@Inject
	private ParliamentClubRepository clubRepository;

	@Inject
	private PoliticianAttitudeRelationRepository relationPeriod;

	@RequestMapping(value = "period/{period}", method = RequestMethod.GET)
	public List<PoliticianDTO> findPolticiansByPeriod(@PathVariable("period") Integer period) {
		return PoliticianDTO.fromPoliticians(mapper, politicianService.findNationalCouncilMembersOfPeriod(period));
	}

	@RequestMapping(value = "test", method = RequestMethod.GET)
	public PoliticianDTO test() {
		return PoliticianDTO.fromPoliticians(mapper, politicianService.findNationalCouncilMembersOfPeriod(25)).get(0);
	}

	@RequestMapping(value = "nodes", method = RequestMethod.GET)
	public List<D3Node> getNodes() {
		return StreamSupport.stream(clubRepository.findAll().spliterator(), false).map(club -> getNode(club))
				.collect(Collectors.toList());
	}

	protected D3Node getNode(ParliamentClub club) {
		return new D3Node(club.getShortName(), club.getShortName());
	}

	@RequestMapping(value = "links", method = RequestMethod.GET)
	public List<D3Link> getLinks() {
		List<D3Node> nodes = getNodes();

		return relationPeriod
				.getClubAttitudesByPeriods()
				.stream()
				.filter(ca -> !ca.getClub1().equals(ca.getClub2()))
				.map(ca -> new D3Link(nodes.indexOf(getNode(ca.getClub1())), nodes.indexOf(getNode(ca.getClub2())), ca
						.getWeight())).collect(Collectors.toList());
	}
}
