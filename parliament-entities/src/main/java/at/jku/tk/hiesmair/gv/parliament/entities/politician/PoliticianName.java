package at.jku.tk.hiesmair.gv.parliament.entities.politician;

import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class PoliticianName {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne(optional = false)
	private Politician politician;

	@Embedded
	private Name name = new Name();

	@Temporal(TemporalType.DATE)
	private Date validUntil;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Politician getPolitician() {
		return politician;
	}

	public void setPolitician(Politician politician) {
		this.politician = politician;
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

	public Date getValidUntil() {
		return validUntil;
	}

	public void setValidUntil(Date validUntil) {
		this.validUntil = validUntil;
	}

}
