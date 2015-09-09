package at.jku.tk.hiesmair.gv.parliament.entities.mandate;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

@Entity
@DiscriminatorValue("FederalCouncilPresident")
public class FederalCouncilPresident extends Mandate {

	private static final long serialVersionUID = -3898433872481956252L;

	public FederalCouncilPresident() {
	}
	
	public FederalCouncilPresident(String description, Politician politician, Date validFrom, Date validTo) {
		super(description, politician, validFrom, validTo);
	}
	
	@Override
	public String toString() {
		return "FederalCouncilPresident [politician=" + getPolitician().getSurName()
				+ ", validFrom=" + getValidFrom() + ", validUntil=" + validUntil + "]";
	}

	

	
	
}
