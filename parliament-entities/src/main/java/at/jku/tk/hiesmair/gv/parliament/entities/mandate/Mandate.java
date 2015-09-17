package at.jku.tk.hiesmair.gv.parliament.entities.mandate;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.time.DateUtils;

import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;
import at.jku.tk.hiesmair.gv.parliament.util.ParliamentDateUtils;

@Entity
@Table(name = "mandate", uniqueConstraints = { @UniqueConstraint(columnNames = { "politician_id", "description",
		"valid_from" }) })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "mandate_type")
@DiscriminatorValue("Mandate")
public class Mandate implements Serializable {

	private static final long serialVersionUID = -2080373900490962697L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Integer id;

	@JoinColumn(name = "politician_id")
	@ManyToOne(optional = false)
	protected Politician politician;

	@Column(name = "description", nullable = false)
	protected String description;

	@Column(name = "valid_from", nullable = false)
	@Temporal(TemporalType.DATE)
	protected Date validFrom;

	@Temporal(TemporalType.DATE)
	protected Date validUntil;

	public Mandate() {
	}

	public Mandate(String description, Politician politician, Date validFrom, Date validUntil) {
		super();
		this.politician = politician;
		this.description = description;
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

	public boolean isValidAt(Date date) {
		return ParliamentDateUtils.isDateBetween(DateUtils.truncate(date, Calendar.DATE), getValidFrom(),
				getValidUntil());
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

	@Override
	public String toString() {
		return "Mandate [description=" + getDescription() + ", politician=" + getPolitician().getSurName()
				+ ", validFrom=" + getValidFrom() + ", validUntil=" + validUntil + "]";
	}

}
