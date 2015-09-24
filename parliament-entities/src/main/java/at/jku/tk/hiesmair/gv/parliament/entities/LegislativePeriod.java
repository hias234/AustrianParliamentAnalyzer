package at.jku.tk.hiesmair.gv.parliament.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;

import org.apache.commons.lang.time.DateUtils;

import at.jku.tk.hiesmair.gv.parliament.db.DBConstants;
import at.jku.tk.hiesmair.gv.parliament.entities.club.ParliamentClub;
import at.jku.tk.hiesmair.gv.parliament.entities.mandate.NationalCouncilMember;
import at.jku.tk.hiesmair.gv.parliament.entities.session.Session;
import at.jku.tk.hiesmair.gv.parliament.util.ParliamentDateUtils;

@Entity
@Table(name = DBConstants.TAB_NAME_LEGISLATIVE_PERIOD)
@SecondaryTable(name = DBConstants.TAB_NAME_LEGISLATIVE_PERIOD)
public class LegislativePeriod implements Serializable {

	private static final long serialVersionUID = -1229415676697571846L;

	@Id
	private Integer period;

	@OneToMany(mappedBy = "period")
	private List<Session> sessions = new ArrayList<Session>();

	@ManyToMany(mappedBy = "periods")
	private Set<NationalCouncilMember> nationalCouncilMembers = new HashSet<NationalCouncilMember>();

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

	public Set<NationalCouncilMember> getNationalCouncilMembers() {
		return nationalCouncilMembers;
	}

	public void setNationalCouncilMembers(Set<NationalCouncilMember> nationalCouncilMembers) {
		this.nationalCouncilMembers = nationalCouncilMembers;
	}

	public Set<NationalCouncilMember> getNationalCouncilMembersAt(Date date) {
		return nationalCouncilMembers
				.stream()
				.filter(ncm -> ParliamentDateUtils.isDateBetween(DateUtils.truncate(date, Calendar.DATE),
						ncm.getValidFrom(), ncm.getValidUntil())).collect(Collectors.toSet());
	}

	public Map<ParliamentClub, Long> getMandateCountByClubAtDate(Date date) {
		Set<NationalCouncilMember> ncms = getNationalCouncilMembersAt(date);

		return ncms.stream().collect(Collectors.groupingBy(ncm -> ncm.getClub(), Collectors.counting()));
	}

	@Override
	public String toString() {
		return "LegislativePeriod [period=" + period + ", sessions=" + sessions + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((period == null) ? 0 : period.hashCode());
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
		LegislativePeriod other = (LegislativePeriod) obj;
		if (period == null) {
			if (other.period != null)
				return false;
		}
		else if (!period.equals(other.period))
			return false;
		return true;
	}

}
