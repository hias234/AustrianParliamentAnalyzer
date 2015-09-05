package at.jku.tk.hiesmair.gv.parlament.entities.mandate;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

import at.jku.tk.hiesmair.gv.parlament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parlament.entities.club.ParliamentClub;
import at.jku.tk.hiesmair.gv.parlament.entities.politician.Politician;

@Entity
@DiscriminatorValue("NationalCouncilMember")
public class NationalCouncilMember extends CouncilMember {

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	protected Set<LegislativePeriod> periods;

	public NationalCouncilMember() {
		super();
	}

	public NationalCouncilMember(String description, Politician politician, Date validFrom, Date validUntil,
			ParliamentClub club, Set<LegislativePeriod> periods) {
		super(description, politician, validFrom, validUntil, club);
		this.periods = periods;
	}

	public Set<LegislativePeriod> getPeriods() {
		return periods;
	}

	public void setPeriods(Set<LegislativePeriod> periods) {
		this.periods = periods;
	}

	@Override
	public String toString() {
		return "NationalCouncilMember [periods=" + periods + ", club=" + club + ", politician="
				+ politician.getSurName() + ", validFrom=" + validFrom + ", validUntil=" + validUntil + "]";
	}

}
