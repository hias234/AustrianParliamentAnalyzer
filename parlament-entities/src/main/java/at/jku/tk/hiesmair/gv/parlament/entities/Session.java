package at.jku.tk.hiesmair.gv.parlament.entities;

import java.util.Date;

public class Session {
	
	private Integer sessionNr;
	private Date startDate;
	private Date endDate;
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
	
	
}
