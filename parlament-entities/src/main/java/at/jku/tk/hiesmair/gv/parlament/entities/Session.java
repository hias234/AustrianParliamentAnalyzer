package at.jku.tk.hiesmair.gv.parlament.entities;

import java.util.Date;
import java.util.List;

import at.jku.tk.hiesmair.gv.parlament.entities.discussion.Discussion;

public class Session {

	private Integer sessionNr;
	private Date startDate;
	private Date endDate;
	private List<Politician> politicians;
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
		return "Session [sessionNr=" + sessionNr + ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}

}
