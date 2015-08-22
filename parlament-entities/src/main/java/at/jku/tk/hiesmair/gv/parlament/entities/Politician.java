package at.jku.tk.hiesmair.gv.parlament.entities;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import at.jku.tk.hiesmair.gv.parlament.entities.club.ClubMembership;

public class Politician {

	private String id;
	private String title;
	private String firstName;
	private String surName;
	private Date birthDate;
	private List<ClubMembership> clubMemberships;

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

	public List<ClubMembership> getClubMemberships() {
		return clubMemberships;
	}

	public void setClubMemberships(List<ClubMembership> clubMemberships) {
		this.clubMemberships = clubMemberships;
	}

	public ClubMembership getClubMembership(Date date) {
		Optional<ClubMembership> optional = clubMemberships.stream().filter(m -> m.isValidAtDate(date)).findFirst();
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
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
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Politician [id=" + id + ", title=" + title + ", firstName=" + firstName + ", surName=" + surName
				+ ", birthDate=" + birthDate + ", clubMemberships (" + clubMemberships.size() + ") =" + clubMemberships
				+ "]";
	}

}
