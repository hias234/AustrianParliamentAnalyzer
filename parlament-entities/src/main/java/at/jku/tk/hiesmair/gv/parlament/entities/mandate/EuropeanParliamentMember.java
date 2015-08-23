package at.jku.tk.hiesmair.gv.parlament.entities.mandate;

public class EuropeanParliamentMember extends CouncilMember {

	@Override
	public String toString() {
		return "EuropeanParliamentMember [club=" + club + ", politician=" + politician.getSurName() + ", validFrom="
				+ validFrom + ", validUntil=" + validUntil + "]";
	}

}
