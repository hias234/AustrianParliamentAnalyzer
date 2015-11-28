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
import at.jku.tk.hiesmair.gv.parliament.entities.club.ParliamentClub;

@Entity
@Table(name = "club_attitude_relation_by_period")
public class ClubAttitudeRelationByPeriod {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Integer id;

	@ManyToOne(optional = false)
	protected ParliamentClub club1;

	@ManyToOne(optional = false)
	protected ParliamentClub club2;

	@ManyToOne(optional = false)
	protected LegislativePeriod period;

	@Column(nullable = false)
	protected Integer weight;

	@Transient
	protected Integer count;

	public ClubAttitudeRelationByPeriod(LegislativePeriod period, Long weight) {
		super();
		this.period = period;
		this.weight = weight.intValue();
	}

	public ClubAttitudeRelationByPeriod(ParliamentClub club1, ParliamentClub club2, LegislativePeriod period,
			Integer weight, Integer count) {
		super();
		this.club1 = club1;
		this.club2 = club2;
		this.period = period;
		this.weight = weight;
		this.count = count;
	}

	public ClubAttitudeRelationByPeriod(ParliamentClub club1, ParliamentClub club2, LegislativePeriod period,
			Long weight, Long count) {
		super();
		this.club1 = club1;
		this.club2 = club2;
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

	public ParliamentClub getClub1() {
		return club1;
	}

	public void setClub1(ParliamentClub club1) {
		this.club1 = club1;
	}

	public ParliamentClub getClub2() {
		return club2;
	}

	public void setClub2(ParliamentClub club2) {
		this.club2 = club2;
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

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Double getNormalizedWeight() {
		return weight / (double) count;
	}

}
