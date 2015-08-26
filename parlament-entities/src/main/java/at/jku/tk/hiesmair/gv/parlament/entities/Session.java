package at.jku.tk.hiesmair.gv.parlament.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import at.jku.tk.hiesmair.gv.parlament.entities.discussion.Discussion;

@Entity
public class Session {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne(optional = false)
	private LegislativePeriod period;

	private Integer sessionNr;

	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	private List<Politician> politicians;

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	private List<Discussion> discussions;

	public Session() {
		super();
	}

	public Session(Integer sessionNr, Date startDate, Date endDate) {
		super();
		this.sessionNr = sessionNr;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LegislativePeriod getPeriod() {
		return period;
	}

	public void setPeriod(LegislativePeriod period) {
		this.period = period;
	}

	public Integer getSessionNr() {
		return sessionNr;
	}

	public void setSessionNr(Integer sessionNr) {
		this.sessionNr = sessionNr;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<Politician> getPoliticians() {
		return politicians;
	}

	public void setPoliticians(List<Politician> politicians) {
		this.politicians = politicians;
	}

	public List<Discussion> getDiscussions() {
		return discussions;
	}

	public void setDiscussions(List<Discussion> discussions) {
		this.discussions = discussions;
	}

	@Override
	public String toString() {
		return "Session [period=" + period.getPeriod() + ", sessionNr=" + sessionNr + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", politicians=" + politicians + ", discussions=" + discussions + "]";
	}

}
