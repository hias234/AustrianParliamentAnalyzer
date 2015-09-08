package at.jku.tk.hiesmair.gv.parliament.entities.session;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

	@Embeddable
	public static class SessionId implements Serializable {

		private static final long serialVersionUID = -6013583214876970566L;

		@ManyToOne(optional = false)
		private LegislativePeriod period;

		/** e.g. 72. Sitzung */
		private String sessionTitle;

		public SessionId() {
			super();
		}

		public SessionId(LegislativePeriod period, String sessionTitle) {
			super();
			this.period = period;
			this.sessionTitle = sessionTitle;
		}

		public LegislativePeriod getPeriod() {
			return period;
		}

		public void setPeriod(LegislativePeriod period) {
			this.period = period;
		}

		public String getSessionTitle() {
			return sessionTitle;
		}

		public void setSessionTitle(String sessionTitle) {
			this.sessionTitle = sessionTitle;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((period == null) ? 0 : period.hashCode());
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
			SessionId other = (SessionId) obj;
			if (period == null) {
				if (other.period != null)
					return false;
			}
			else if (!period.equals(other.period))
				return false;
			if (sessionTitle == null) {
				if (other.sessionTitle != null)
					return false;
			}
			else if (!sessionTitle.equals(other.sessionTitle))
				return false;
			return true;
		}

	}

	@EmbeddedId
	private SessionId id;

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
		id = new SessionId();
	}

	public SessionId getId() {
		return id;
	}

	public void setId(SessionId id) {
		this.id = id;
	}

	public LegislativePeriod getPeriod() {
		return id.getPeriod();
	}

	public void setPeriod(LegislativePeriod period) {
		this.id.setPeriod(period);
	}

	public Integer getSessionNr() {
		return sessionNr;
	}

	public void setSessionNr(Integer sessionNr) {
		this.sessionNr = sessionNr;
	}
	
	public String getSessionTitle(){
		return id.getSessionTitle();
	}
	
	public void setSessionTitle(String sessionTitle){
		id.setSessionTitle(sessionTitle);
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
		return "Session [period=" + getPeriod().getPeriod() + ", sessionNr=" + getSessionNr() + ", startDate="
				+ startDate + ", endDate=" + endDate + ", absentPoliticians=" + absentNationalCouncilMembers
				+ ", discussions=" + discussions + "]";
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
		Session other = (Session) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}

}
