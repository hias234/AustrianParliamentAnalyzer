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
		return "Name [title=" + title + ", titleAfter=" + titleAfter + ", firstName=" + firstName + ", surName="
				+ surName + "]";
	}

}
