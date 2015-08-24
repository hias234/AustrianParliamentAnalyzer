package at.jku.tk.hiesmair.gv.parlament.entities.mandate;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import at.jku.tk.hiesmair.gv.parlament.entities.Politician;
import at.jku.tk.hiesmair.gv.parlament.entities.club.ParliamentClub;

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
