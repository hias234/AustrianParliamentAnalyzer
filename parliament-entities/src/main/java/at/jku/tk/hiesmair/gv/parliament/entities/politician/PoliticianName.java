package at.jku.tk.hiesmair.gv.parliament.entities.politician;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class PoliticianName {

	@Embeddable
	public static class PoliticianNameId implements Serializable {

		private static final long serialVersionUID = -975500665494352271L;

		@ManyToOne(optional = false)
		private Politician politician;

		@Temporal(TemporalType.DATE)
		private Date validUntil;

		public PoliticianNameId() {
			super();
		}

		public PoliticianNameId(Politician politician, Date validUntil) {
			super();
			this.politician = politician;
			this.validUntil = validUntil;
		}

		public Politician getPolitician() {
			return politician;
		}

		public void setPolitician(Politician politician) {
			this.politician = politician;
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
			result = prime * result
					+ ((politician == null) ? 0 : politician.hashCode());
			result = prime * result
					+ ((validUntil == null) ? 0 : validUntil.hashCode());
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
			PoliticianNameId other = (PoliticianNameId) obj;
			if (politician == null) {
				if (other.politician != null)
					return false;
			} else if (!politician.equals(other.politician))
				return false;
			if (validUntil == null) {
				if (other.validUntil != null)
					return false;
			} else if (!validUntil.equals(other.validUntil))
				return false;
			return true;
		}

	}

	@EmbeddedId
	private PoliticianNameId id;

	@Embedded
	private Name name;

	public PoliticianName() {
		this.id = new PoliticianNameId();
		this.name = new Name();
	}

	public PoliticianName(Politician politician, Name name, Date validUntil) {
		super();
		this.id = new PoliticianNameId(politician, validUntil);
		this.name = name;
	}

	public Politician getPolitician() {
		return id.getPolitician();
	}

	public void setPolitician(Politician politician) {
		this.id.setPolitician(politician);
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
		return id.getValidUntil();
	}

	public void setValidUntil(Date validUntil) {
		this.id.setValidUntil(validUntil);
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
		PoliticianName other = (PoliticianName) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
