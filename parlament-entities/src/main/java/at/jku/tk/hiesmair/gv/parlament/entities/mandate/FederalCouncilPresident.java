package at.jku.tk.hiesmair.gv.parlament.entities.mandate;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import at.jku.tk.hiesmair.gv.parlament.entities.Politician;

@Entity
@DiscriminatorValue("FederalCouncilPresident")
public class FederalCouncilPresident extends Mandate {

	public FederalCouncilPresident() {
	}
	
	public FederalCouncilPresident(String description, Politician politician, Date validFrom, Date validTo) {
		super(description, politician, validFrom, validTo);
	}
	
	@Override
	public String toString() {
		return "FederalCouncilPresident [politician=" + politician.getSurName()
				+ ", validFrom=" + validFrom + ", validUntil=" + validUntil + "]";
	}

	

	
	
}
