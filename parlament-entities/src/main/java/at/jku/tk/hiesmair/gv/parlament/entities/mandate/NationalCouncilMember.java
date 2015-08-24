package at.jku.tk.hiesmair.gv.parlament.entities.mandate;

import java.util.Date;
import java.util.List;

import at.jku.tk.hiesmair.gv.parlament.entities.Politician;
import at.jku.tk.hiesmair.gv.parlament.entities.club.ParliamentClub;

public class NationalCouncilMember extends CouncilMember {

	protected List<Integer> periods;

	public NationalCouncilMember() {
		super();
	}

	public NationalCouncilMember(String description, Politician politician, Date validFrom, Date validUntil,
			ParliamentClub club, List<Integer> periods) {
		super(description, politician, validFrom, validUntil, club);
		this.periods = periods;
	}

	public List<Integer> getPeriods() {
		return periods;
	}

	public void setPeriods(List<Integer> periods) {
		this.periods = periods;
	}

	@Override
	public String toString() {
		return "NationalCouncilMember [periods=" + periods + ", club=" + club + ", politician="
				+ politician.getSurName() + ", validFrom=" + validFrom + ", validUntil=" + validUntil + "]";
	}

}
