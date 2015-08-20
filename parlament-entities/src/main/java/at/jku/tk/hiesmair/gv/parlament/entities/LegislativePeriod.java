package at.jku.tk.hiesmair.gv.parlament.entities;

import java.util.List;

public class LegislativePeriod {

	private Integer period;
	private List<Session> sessions;

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public List<Session> getSessions() {
		return sessions;
	}

	public void setSessions(List<Session> sessions) {
		this.sessions = sessions;
	}
	
	
	
}
