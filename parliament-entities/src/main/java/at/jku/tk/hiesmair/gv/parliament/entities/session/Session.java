package at.jku.tk.hiesmair.gv.parliament.entities.session;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import at.jku.tk.hiesmair.gv.parliament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parliament.entities.discussion.Discussion;
import at.jku.tk.hiesmair.gv.parliament.entities.mandate.NationalCouncilMember;

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

	// @ManyToMany(fetch = FetchType.EAGER)
	@Transient
	private Set<NationalCouncilMember> presentNationalCouncilMembers;

	// @ManyToMany(fetch = FetchType.EAGER)
	@Transient
	private Set<NationalCouncilMember> absentNationalCouncilMembers;

	@OneToMany(mappedBy = "session", fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	private List<Discussion> discussions;

	@OneToMany(mappedBy = "session", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<SessionChairMan> chairMen;

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

	public Set<NationalCouncilMember> getPresentNationalCouncilMembers() {
		return presentNationalCouncilMembers;
	}

	public void setPresentNationalCouncilMembers(Set<NationalCouncilMember> presentNationalCouncilMembers) {
		this.presentNationalCouncilMembers = presentNationalCouncilMembers;
	}

	public Set<NationalCouncilMember> getAbsentNationalCouncilMembers() {
		return absentNationalCouncilMembers;
	}

	public void setAbsentNationalCouncilMembers(Set<NationalCouncilMember> absentNationalCouncilMembers) {
		this.absentNationalCouncilMembers = absentNationalCouncilMembers;
	}

	public List<Discussion> getDiscussions() {
		return discussions;
	}

	public void setDiscussions(List<Discussion> discussions) {
		this.discussions = discussions;
	}

	public List<SessionChairMan> getChairMen() {
		return chairMen;
	}

	public void setChairMen(List<SessionChairMan> chairMen) {
		this.chairMen = chairMen;
	}

	@Override
	public String toString() {
		return "Session [period=" + period.getPeriod() + ", sessionNr=" + sessionNr + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", absentPoliticians=" + absentNationalCouncilMembers + ", discussions="
				+ discussions + "]";
	}

}
