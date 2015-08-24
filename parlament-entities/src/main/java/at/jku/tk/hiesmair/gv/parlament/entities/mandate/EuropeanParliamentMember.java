package at.jku.tk.hiesmair.gv.parlament.entities.mandate;

import java.util.Date;

import at.jku.tk.hiesmair.gv.parlament.entities.Politician;
import at.jku.tk.hiesmair.gv.parlament.entities.club.ParliamentClub;

public class EuropeanParliamentMember extends CouncilMember {

	public EuropeanParliamentMember() {
	}
	
	public EuropeanParliamentMember(String description, ParliamentClub club, Politician politician, Date validFrom, Date validTo) {
		super(description, politician, validFrom, validTo, club);
	}
	
	@Override
	public String toString() {
		return "EuropeanParliamentMember [club=" + club + ", politician=" + politician.getSurName() + ", validFrom="
				+ validFrom + ", validUntil=" + validUntil + "]";
	}

}
