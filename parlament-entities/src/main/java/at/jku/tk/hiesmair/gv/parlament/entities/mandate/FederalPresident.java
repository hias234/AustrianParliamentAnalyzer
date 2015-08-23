package at.jku.tk.hiesmair.gv.parlament.entities.mandate;

public class FederalPresident extends Mandate {

	@Override
	public String toString() {
		return "FederalPresident [politician=" + politician.getSurName() + ", validFrom=" + validFrom + ", validUntil=" + validUntil
				+ "]";
	}

	
	
}
