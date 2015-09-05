package at.jku.tk.hiesmair.gv.parliament.entities.mandate;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import at.jku.tk.hiesmair.gv.parliament.entities.club.ParliamentClub;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

/**
 * Bundesrat Member
 * @author Markus
 *
 */
@Entity
@DiscriminatorValue("FederalCouncilMember")
public class FederalCouncilMember extends CouncilMember {

	public FederalCouncilMember() {
	}
	
	public FederalCouncilMember(ParliamentClub club, String description, Politician politician, Date validFrom, Date validTo) {
		super(description, politician, validFrom, validTo, club);
	}
	
	@Override
	public String toString() {
		return "FederalCouncilMember [club=" + club + ", politician=" + politician.getSurName() + ", validFrom="
				+ validFrom + ", validUntil=" + validUntil + "]";
	}
	
}
