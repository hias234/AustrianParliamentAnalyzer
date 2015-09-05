package at.jku.tk.hiesmair.gv.parliament.entities.mandate;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import at.jku.tk.hiesmair.gv.parliament.entities.club.ParliamentClub;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

@Entity
@DiscriminatorValue("EuropeanParliamentMember")
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
