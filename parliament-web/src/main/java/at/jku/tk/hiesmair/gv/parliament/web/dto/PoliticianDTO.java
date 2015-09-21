package at.jku.tk.hiesmair.gv.parliament.web.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import at.jku.tk.hiesmair.gv.parliament.entities.politician.Name;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

public class PoliticianDTO {

	private String id;
	private Name name = new Name();
	private Date birthDate;
	private List<MandateDTO> mandates = new ArrayList<MandateDTO>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public List<MandateDTO> getMandates() {
		return mandates;
	}

	public void setMandates(List<MandateDTO> mandates) {
		this.mandates = mandates;
	}

	public static PoliticianDTO fromPolitician(ModelMapper mapper, Politician politician) {
		return mapper.map(politician, PoliticianDTO.class);
	}

	public static List<PoliticianDTO> fromPoliticians(ModelMapper mapper, List<Politician> politicians) {
		return politicians.stream().map(p -> fromPolitician(mapper, p)).collect(Collectors.toList());
	}
}
