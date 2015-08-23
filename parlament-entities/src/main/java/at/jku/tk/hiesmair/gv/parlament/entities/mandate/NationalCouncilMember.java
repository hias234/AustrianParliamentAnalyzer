package at.jku.tk.hiesmair.gv.parlament.entities.mandate;

import java.util.List;

public class NationalCouncilMember extends CouncilMember {

	protected List<Integer> periods;

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
