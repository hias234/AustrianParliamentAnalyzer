package at.jku.tk.hiesmair.gv.parliament.entities.relation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import at.jku.tk.hiesmair.gv.parliament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

@Entity
@Table(name = "politician_attitude_relation_by_period")
public class PoliticianAttitudeRelationByPeriod {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Integer id;

	@ManyToOne(optional = false)
	protected Politician politician1;

	@ManyToOne(optional = false)
	protected Politician politician2;

	@ManyToOne(optional = false)
	protected LegislativePeriod period;

	@Column(nullable = false)
	protected Integer weight;
	
	@Transient
	protected Integer count;

	public PoliticianAttitudeRelationByPeriod() {
		super();
	}

	public PoliticianAttitudeRelationByPeriod(Politician politician1, Politician politician2, LegislativePeriod period,
			Integer weight, Integer count) {
		super();
		this.politician1 = politician1;
		this.politician2 = politician2;
		this.period = period;
		this.weight = weight;
		this.count = count;
	}
	
	public PoliticianAttitudeRelationByPeriod(Politician politician1, Politician politician2, LegislativePeriod period,
			Long weight, Long count) {
		super();
		this.politician1 = politician1;
		this.politician2 = politician2;
		this.period = period;
		this.weight = weight.intValue();
		this.count = count.intValue();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Politician getPolitician1() {
		return politician1;
	}

	public void setPolitician1(Politician politician1) {
		this.politician1 = politician1;
	}

	public Politician getPolitician2() {
		return politician2;
	}

	public void setPolitician2(Politician politician2) {
		this.politician2 = politician2;
	}

	public LegislativePeriod getPeriod() {
		return period;
	}

	public void setPeriod(LegislativePeriod period) {
		this.period = period;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	
	public Double getNormalizedWeight(){
		return weight / (double) count;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((period == null) ? 0 : period.hashCode());
		result = prime * result + ((politician1 == null) ? 0 : politician1.hashCode());
		result = prime * result + ((politician2 == null) ? 0 : politician2.hashCode());
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
		PoliticianAttitudeRelationByPeriod other = (PoliticianAttitudeRelationByPeriod) obj;
		if (period == null) {
			if (other.period != null)
				return false;
		}
		else
			if (!period.equals(other.period))
				return false;
		if (politician1 == null) {
			if (other.politician1 != null)
				return false;
		}
		else
			if (!politician1.equals(other.politician1))
				return false;
		if (politician2 == null) {
			if (other.politician2 != null)
				return false;
		}
		else
			if (!politician2.equals(other.politician2))
				return false;
		return true;
	}

}
