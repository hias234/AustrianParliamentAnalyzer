package at.jku.tk.hiesmair.gv.parliament.entities.politician;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "politician_name", uniqueConstraints = { @UniqueConstraint(columnNames = { "politician_id", "valid_until" }) })
public class PoliticianName implements Serializable {

	private static final long serialVersionUID = -6403166498610007401L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@JoinColumn(name = "politician_id")
	@ManyToOne(optional = false)
	private Politician politician;

	@Column(name = "valid_until", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date validUntil;

	@Embedded
	private Name name;

	public PoliticianName() {
		this.name = new Name();
	}

	public PoliticianName(Politician politician, Name name, Date validUntil) {
		super();
		this.politician = politician;
		this.validUntil = validUntil;
		this.name = name;
	}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((politician == null) ? 0 : politician.hashCode());
		result = prime * result + ((validUntil == null) ? 0 : validUntil.hashCode());
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
		PoliticianName other = (PoliticianName) obj;
		if (politician == null) {
			if (other.politician != null)
				return false;
		}
		else if (!politician.equals(other.politician))
			return false;
		if (validUntil == null) {
			if (other.validUntil != null)
				return false;
		}
		else if (!validUntil.equals(other.validUntil))
			return false;
		return true;
	}

}
