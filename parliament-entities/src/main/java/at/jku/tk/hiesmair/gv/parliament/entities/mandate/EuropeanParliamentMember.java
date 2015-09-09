package at.jku.tk.hiesmair.gv.parliament.entities.mandate;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import at.jku.tk.hiesmair.gv.parliament.entities.club.ParliamentClub;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

@Entity
@DiscriminatorValue("EuropeanParliamentMember")
public class EuropeanParliamentMember extends CouncilMember {

	private static final long serialVersionUID = 1561917982783876007L;

	public EuropeanParliamentMember() {
	}

	public EuropeanParliamentMember(String description, ParliamentClub club, Politician politician, Date validFrom,
			Date validTo) {
		super(description, politician, validFrom, validTo, club);
	}

	@Override
	public String toString() {
		return "EuropeanParliamentMember [club=" + club + ", politician=" + getPolitician().getSurName()
				+ ", validFrom=" + getValidFrom() + ", validUntil=" + validUntil + "]";
	}

}
