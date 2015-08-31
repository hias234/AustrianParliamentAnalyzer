package at.jku.tk.hiesmair.gv.parlament.entities.mandate;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import at.jku.tk.hiesmair.gv.parlament.entities.Politician;

@Entity
@Inheritance
@DiscriminatorColumn(name = "mandate_type")
public class Mandate {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Integer id;

	protected String description;

	@ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = { CascadeType.ALL })
	protected Politician politician;

	@Temporal(TemporalType.DATE)
	protected Date validFrom;

	@Temporal(TemporalType.DATE)
	protected Date validUntil;

	public Mandate() {
		super();
	}

	public Mandate(String description, Politician politician, Date validFrom, Date validUntil) {
		super();
		this.description = description;
		this.politician = politician;
		this.validFrom = validFrom;
		this.validUntil = validUntil;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Date getValidUntil() {
		return validUntil;
	}

	public void setValidUntil(Date validUntil) {
		this.validUntil = validUntil;
	}

	@Override
	public String toString() {
		return "Mandate [description=" + description + ", politician=" + politician.getSurName() + ", validFrom="
				+ validFrom + ", validUntil=" + validUntil + "]";
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
		Mandate other = (Mandate) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		}
		else if (!description.equals(other.description))
			return false;
		if (politician == null) {
			if (other.politician != null)
				return false;
		}
		else if (!politician.equals(other.politician))
			return false;
		if (validFrom == null) {
			if (other.validFrom != null)
				return false;
		}
		else if (!validFrom.equals(other.validFrom))
			return false;
		return true;
	}

}
