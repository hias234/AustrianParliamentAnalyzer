package at.jku.tk.hiesmair.gv.parlament.entities.mandate;

public class NationalCouncilMember extends CouncilMember {
	
	@Override
	public String toString() {
		return "NationalCouncilMember [club=" + club + ", politician=" + politician.getSurName() + ", validFrom="
				+ validFrom + ", validUntil=" + validUntil + "]";
	}
	
}
