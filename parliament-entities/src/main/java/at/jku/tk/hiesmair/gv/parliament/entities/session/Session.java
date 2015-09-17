package at.jku.tk.hiesmair.gv.parliament.entities.session;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import at.jku.tk.hiesmair.gv.parliament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parliament.entities.discussion.Discussion;
import at.jku.tk.hiesmair.gv.parliament.entities.mandate.NationalCouncilMember;

@Entity
@Table(name = "session", uniqueConstraints = { @UniqueConstraint(columnNames = { "period", "session_nr" }) })
public class Session implements Serializable {

	private static final long serialVersionUID = -4429469608872326606L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@JoinColumn(name = "period")
	@ManyToOne(optional = false)
	private LegislativePeriod period;

	/** e.g. 72. Sitzung */
	private String sessionTitle;

	@Column(name = "session_nr")
	private Integer sessionNr;

	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	@ManyToMany
	private Set<NationalCouncilMember> presentNationalCouncilMembers = new HashSet<NationalCouncilMember>();

	@ManyToMany
	private Set<NationalCouncilMember> absentNationalCouncilMembers = new HashSet<NationalCouncilMember>();

	@OneToMany(mappedBy = "id.session")
	private List<Discussion> discussions;

	@OneToMany(mappedBy = "session")
	private List<SessionChairMan> chairMen;

	public Session() {
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

	public String getSessionTitle() {
		return sessionTitle;
	}

	public void setSessionTitle(String sessionTitle) {
		this.sessionTitle = sessionTitle;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((period == null) ? 0 : period.hashCode());
		result = prime * result + ((sessionNr == null) ? 0 : sessionNr.hashCode());
		result = prime * result + ((sessionTitle == null) ? 0 : sessionTitle.hashCode());
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
		Session other = (Session) obj;
		if (period == null) {
			if (other.period != null)
				return false;
		}
		else if (!period.equals(other.period))
			return false;
		if (sessionNr == null) {
			if (other.sessionNr != null)
				return false;
		}
		else if (!sessionNr.equals(other.sessionNr))
			return false;
		if (sessionTitle == null) {
			if (other.sessionTitle != null)
				return false;
		}
		else if (!sessionTitle.equals(other.sessionTitle))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Session [period=" + getPeriod().getPeriod() + ", sessionNr=" + getSessionNr() + ", startDate="
				+ startDate + ", endDate=" + endDate + ", absentPoliticians=" + absentNationalCouncilMembers
				+ ", discussions=" + discussions + "]";
	}

}
