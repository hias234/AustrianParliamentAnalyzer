package at.jku.tk.hiesmair.gv.parliament.entities.mandate;

import java.util.Date;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import at.jku.tk.hiesmair.gv.parliament.db.DBConstants;
import at.jku.tk.hiesmair.gv.parliament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parliament.entities.club.ParliamentClub;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

@Entity
@DiscriminatorValue("NationalCouncilMember")
public class NationalCouncilMember extends CouncilMember {

	private static final long serialVersionUID = -7309695226285808929L;

	@JoinTable(name = DBConstants.JOIN_TAB_NAME_NCM_PERIOD, joinColumns = {
			@JoinColumn(table = DBConstants.TAB_NAME_MANDATE, name = "ncm_id", referencedColumnName = "id")
	}, inverseJoinColumns = {
			@JoinColumn(table = DBConstants.TAB_NAME_LEGISLATIVE_PERIOD, name = "period", referencedColumnName = "period")
	})
	@ManyToMany
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
				+ getPolitician().getSurName() + ", validFrom=" + getValidFrom() + ", validUntil=" + validUntil + "]";
	}

}
