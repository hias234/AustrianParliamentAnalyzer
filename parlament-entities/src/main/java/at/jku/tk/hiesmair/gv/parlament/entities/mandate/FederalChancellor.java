package at.jku.tk.hiesmair.gv.parlament.entities.mandate;

public class FederalChancellor extends Mandate {

	@Override
	public String toString() {
		return "FederalChancellor [politician=" + politician.getSurName() + ", validFrom=" + validFrom
				+ ", validUntil=" + validUntil + "]";
	}

}
