package at.jku.tk.hiesmair.gv.parlament.entities.mandate;

public class FederalViceChancellor extends Mandate {

	@Override
	public String toString() {
		return "VizeFederalChancellor [politician=" + politician.getSurName() + ", validFrom=" + validFrom
				+ ", validUntil=" + validUntil + "]";
	}

}
