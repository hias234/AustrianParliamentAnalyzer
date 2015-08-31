package at.jku.tk.hiesmair.gv.parlament.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import at.jku.tk.hiesmair.gv.parlament.entities.mandate.Mandate;
import at.jku.tk.hiesmair.gv.parlament.entities.mandate.NationalCouncilMember;
import at.jku.tk.hiesmair.gv.parlament.util.DateUtils;

@Entity
public class Politician {

	@Id
	private String id;

	private String title;
	private String titleAfter;
	private String firstName;
	private String surName;

	@Temporal(TemporalType.DATE)
	private Date birthDate;

	@OneToMany(mappedBy = "politician", fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	private List<Mandate> mandates = new ArrayList<Mandate>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSurName() {
		return surName;
	}

	public void setSurName(String surName) {
		this.surName = surName;
	}

	public String getFullName() {
		return firstName + " " + surName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public List<Mandate> getMandates() {
		return mandates;
	}

	public void setMandates(List<Mandate> mandates) {
		this.mandates = mandates;
	}

	public String getTitleAfter() {
		return titleAfter;
	}

	public void setTitleAfter(String titleAfter) {
		this.titleAfter = titleAfter;
	}

	public List<NationalCouncilMember> getNationalCouncilMemberships() {
		return mandates.stream().filter(m -> m instanceof NationalCouncilMember).map(m -> (NationalCouncilMember) m)
				.collect(Collectors.toList());
	}

	public boolean isInNationalCouncilAt(Date date) {
		return getNationalCouncilMemberships().stream().anyMatch(
				ncm -> DateUtils.isDateBetween(date, ncm.getValidFrom(), ncm.getValidUntil()));
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
		return "Politician [id=" + id + ", title=" + title + ", firstName=" + firstName + ", surName=" + surName
				+ ", birthDate=" + birthDate + ", mandates (" + mandates.size() + ") =" + mandates + "]";
	}

}
