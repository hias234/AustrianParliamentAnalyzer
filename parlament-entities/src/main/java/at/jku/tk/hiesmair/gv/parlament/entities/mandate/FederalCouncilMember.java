package at.jku.tk.hiesmair.gv.parlament.entities.mandate;

/**
 * Bundesrat Member
 * @author Markus
 *
 */
public class FederalCouncilMember extends CouncilMember {

	@Override
	public String toString() {
		return "FederalCouncilMember [club=" + club + ", politician=" + politician.getSurName() + ", validFrom="
				+ validFrom + ", validUntil=" + validUntil + "]";
	}
	
}
