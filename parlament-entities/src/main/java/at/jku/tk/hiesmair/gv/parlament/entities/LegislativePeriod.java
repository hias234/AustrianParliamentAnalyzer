package at.jku.tk.hiesmair.gv.parlament.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import at.jku.tk.hiesmair.gv.parlament.entities.mandate.NationalCouncilMember;
import at.jku.tk.hiesmair.gv.parlament.entities.session.Session;
import at.jku.tk.hiesmair.gv.parlament.util.DateUtils;

@Entity
public class LegislativePeriod {

	@Id
	private Integer period;

	@OneToMany(mappedBy = "period", fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	private List<Session> sessions;

	@ManyToMany(mappedBy = "periods", fetch = FetchType.EAGER)
	private List<NationalCouncilMember> nationalCouncilMembers = new ArrayList<NationalCouncilMember>();

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

	public List<NationalCouncilMember> getNationalCouncilMembers() {
		return nationalCouncilMembers;
	}

	public void setNationalCouncilMembers(List<NationalCouncilMember> nationalCouncilMembers) {
		this.nationalCouncilMembers = nationalCouncilMembers;
	}

	public Set<NationalCouncilMember> getNationalCouncilMembersAt(Date date) {
		return nationalCouncilMembers.stream()
				.filter(ncm -> DateUtils.isDateBetween(date, ncm.getValidFrom(), ncm.getValidUntil()))
				.collect(Collectors.toSet());
	}

	@Override
	public String toString() {
		return "LegislativePeriod [period=" + period + ", sessions=" + sessions + "]";
	}

}
