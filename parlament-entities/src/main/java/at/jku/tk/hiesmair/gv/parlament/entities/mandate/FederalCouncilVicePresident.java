package at.jku.tk.hiesmair.gv.parlament.entities.mandate;

public class FederalCouncilVicePresident extends Mandate {

	@Override
	public String toString() {
		return "FederalCouncilVicePresident [politician=" + politician.getSurName() + ", validFrom="
				+ validFrom + ", validUntil=" + validUntil + "]";
	}

}
