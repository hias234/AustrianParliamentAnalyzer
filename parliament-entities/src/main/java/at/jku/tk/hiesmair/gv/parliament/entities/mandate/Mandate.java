package at.jku.tk.hiesmair.gv.parliament.entities.mandate;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

@Entity
@Inheritance
@DiscriminatorColumn(name = "mandate_type")
@DiscriminatorValue("Mandate")
public class Mandate implements Serializable {

	private static final long serialVersionUID = -2080373900490962697L;

	@Embeddable
	public static class MandateId implements Serializable {

		private static final long serialVersionUID = -5347053715424840563L;

		@ManyToOne(optional = false)
		protected Politician politician;

		protected String description;

		@Temporal(TemporalType.DATE)
		protected Date validFrom;

		public MandateId() {
			super();
		}

		public MandateId(Politician politician, String description, Date validFrom) {
			super();
			this.politician = politician;
			this.description = description;
			this.validFrom = validFrom;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public Politician getPolitician() {
			return politician;
		}

		public void setPolitician(Politician politician) {
			this.politician = politician;
		}

		public Date getValidFrom() {
			return validFrom;
		}

		public void setValidFrom(Date validFrom) {
			this.validFrom = validFrom;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((description == null) ? 0 : description.hashCode());
			result = prime * result + ((politician == null) ? 0 : politician.hashCode());
			result = prime * result + ((validFrom == null) ? 0 : validFrom.hashCode());
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
			MandateId other = (MandateId) obj;
			if (description == null) {
				if (other.description != null)
					return false;
			}
			else
				if (!description.equals(other.description))
					return false;
			if (politician == null) {
				if (other.politician != null)
					return false;
			}
			else
				if (!politician.equals(other.politician))
					return false;
			if (validFrom == null) {
				if (other.validFrom != null)
					return false;
			}
			else
				if (!validFrom.equals(other.validFrom))
					return false;
			return true;
		}

		@Override
		public String toString() {
			return "MandateId [politician=" + politician.getSurName() + ", description=" + description + ", validFrom=" + validFrom
					+ "]";
		}
	}

	@EmbeddedId
	protected MandateId id;

	@Temporal(TemporalType.DATE)
	protected Date validUntil;

	public Mandate() {
		this.id = new MandateId();
	}

	public Mandate(String description, Politician politician, Date validFrom, Date validUntil) {
		super();
		this.id = new MandateId(politician, description, validFrom);
		this.validUntil = validUntil;
	}

	public MandateId getId() {
		return id;
	}

	public void setId(MandateId id) {
		this.id = id;
	}

	public String getDescription() {
		return id.getDescription();
	}

	public void setDescription(String description) {
		this.id.setDescription(description);
	}

	public Politician getPolitician() {
		return id.getPolitician();
	}

	public void setPolitician(Politician politician) {
		this.id.setPolitician(politician);
	}

	public Date getValidFrom() {
		return id.getValidFrom();
	}

	public void setValidFrom(Date validFrom) {
		this.id.setValidFrom(validFrom);
	}

	public Date getValidUntil() {
		return validUntil;
	}

	public void setValidUntil(Date validUntil) {
		this.validUntil = validUntil;
	}

	@Override
	public String toString() {
		return "Mandate [description=" + id.getDescription() + ", politician=" + id.getPolitician().getSurName()
				+ ", validFrom=" + id.getValidFrom() + ", validUntil=" + validUntil + "]";
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
		Mandate other = (Mandate) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else
			if (!id.equals(other.id))
				return false;
		return true;
	}

}
