package at.jku.tk.hiesmair.gv.parliament.entities.politician;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import at.jku.tk.hiesmair.gv.parliament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parliament.entities.mandate.Mandate;
import at.jku.tk.hiesmair.gv.parliament.entities.mandate.NationalCouncilMember;

@Entity
public class Politician implements Serializable {

	private static final long serialVersionUID = -384408204868853820L;

	@Id
	private String id;

	@Embedded
	private Name name = new Name();

	@Temporal(TemporalType.DATE)
	private Date birthDate;

	@OneToMany(mappedBy = "politician")
	private Set<Mandate> mandates = new HashSet<Mandate>();

	@OneToMany(mappedBy = "politician")
	private List<PoliticianName> previousNames = new ArrayList<PoliticianName>();

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

	public String getTitle() {
		return name.getTitle();
	}

	public void setTitle(String title) {
		name.setTitle(title);
	}

	public String getFirstName() {
		return name.getFirstName();
	}

	public void setFirstName(String firstName) {
		name.setFirstName(firstName);
	}

	public String getSurName() {
		return name.getSurName();
	}

	public void setSurName(String surName) {
		name.setSurName(surName);
	}

	public String getFullName() {
		return name.getFullName();
	}

	public String getTitleAfter() {
		return name.getTitleAfter();
	}

	public void setTitleAfter(String titleAfter) {
		name.setTitleAfter(titleAfter);
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Set<Mandate> getMandates() {
		return mandates;
	}

	public void setMandates(Set<Mandate> mandates) {
		this.mandates = mandates;
	}

	public List<PoliticianName> getPreviousNames() {
		return previousNames;
	}

	public void setPreviousNames(List<PoliticianName> previousNames) {
		this.previousNames = previousNames;
	}

	public Name getNameAt(Date date) {
		for (PoliticianName politicianName : previousNames) {
			if (date.compareTo(politicianName.getValidUntil()) <= 0) {
				return politicianName.getName();
			}
		}
		return name;
	}

	public List<NationalCouncilMember> getNationalCouncilMemberships() {
		return mandates.stream().filter(m -> m instanceof NationalCouncilMember).map(m -> (NationalCouncilMember) m)
				.collect(Collectors.toList());
	}

	public boolean isInNationalCouncilAt(Date date) {
		return getNationalCouncilMemberships().stream().anyMatch(ncm -> ncm.isValidAt(date));
	}
	
	public Set<Mandate> getMandatesAt(Date date){
		return mandates.stream().filter(m -> m.isValidAt(date)).collect(Collectors.toSet());
	}

	/**
	 * Returns the periods that the politician was/is in the national council
	 * 
	 * @return
	 */
	public List<LegislativePeriod> getNationalCouncilPeriods() {
		List<LegislativePeriod> periods = new ArrayList<LegislativePeriod>();

		for (NationalCouncilMember ncm : getNationalCouncilMemberships()) {
			periods.addAll(ncm.getPeriods());
		}

		return periods;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Politician other = (Politician) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Politician [id=" + id + ", name=" + name + ", birthDate=" + birthDate + ", mandates ("
				+ mandates.size() + ") =" + mandates + "]";
	}

}
