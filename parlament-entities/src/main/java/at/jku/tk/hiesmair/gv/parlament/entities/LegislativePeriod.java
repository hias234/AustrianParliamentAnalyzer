package at.jku.tk.hiesmair.gv.parlament.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class LegislativePeriod {

	@Id
	private Integer period;

	@OneToMany(fetch = FetchType.EAGER)
	private List<Session> sessions;

	public LegislativePeriod() {
		super();
	}

	public LegislativePeriod(Integer period) {
		super();
		this.period = period;
	}

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

	@Override
	public String toString() {
		return "LegislativePeriod [period=" + period + ", sessions=" + sessions + "]";
	}

}
