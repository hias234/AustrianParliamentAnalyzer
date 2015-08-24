package at.jku.tk.hiesmair.gv.parlament.entities.mandate;

import java.util.Date;

import at.jku.tk.hiesmair.gv.parlament.entities.Politician;

public class FederalChancellor extends Mandate {

	public FederalChancellor() {
	}
	
	public FederalChancellor(String description, Politician politician, Date validFrom, Date validTo) {
		super(description, politician, validFrom, validTo);
	}
	
	@Override
	public String toString() {
		return "FederalChancellor [politician=" + politician.getSurName() + ", validFrom=" + validFrom
				+ ", validUntil=" + validUntil + "]";
	}

}
