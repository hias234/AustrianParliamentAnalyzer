package at.jku.tk.hiesmair.gv.parliament.entities.politician;

import javax.persistence.Embeddable;

@Embeddable
public class Name {

	private String title = "";
	private String titleAfter = "";
	private String firstName = "";
	private String surName = "";

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleAfter() {
		return titleAfter;
	}

	public void setTitleAfter(String titleAfter) {
		this.titleAfter = titleAfter;
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

	@Override
	public String toString() {
		return "Name [title=" + title + ", titleAfter=" + titleAfter
				+ ", firstName=" + firstName + ", surName=" + surName + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((surName == null) ? 0 : surName.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result
				+ ((titleAfter == null) ? 0 : titleAfter.hashCode());
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
		Name other = (Name) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (surName == null) {
			if (other.surName != null)
				return false;
		} else if (!surName.equals(other.surName))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (titleAfter == null) {
			if (other.titleAfter != null)
				return false;
		} else if (!titleAfter.equals(other.titleAfter))
			return false;
		return true;
	}

}
