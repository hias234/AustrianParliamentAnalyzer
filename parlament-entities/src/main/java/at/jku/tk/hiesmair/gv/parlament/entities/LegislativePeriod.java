package at.jku.tk.hiesmair.gv.parlament.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import at.jku.tk.hiesmair.gv.parlament.entities.mandate.NationalCouncilMember;
import at.jku.tk.hiesmair.gv.parlament.entities.session.Session;

@Entity
public class LegislativePeriod {

	@Id
	private Integer period;

	@OneToMany(mappedBy = "period", fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	private List<Session> sessions;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<NationalCouncilMember> nationalCouncilMembers;

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
