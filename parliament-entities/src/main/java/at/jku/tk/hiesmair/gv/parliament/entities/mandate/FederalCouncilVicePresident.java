package at.jku.tk.hiesmair.gv.parliament.entities.mandate;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

@Entity
@DiscriminatorValue("FederalCouncilVicePresident")
public class FederalCouncilVicePresident extends Mandate {

	private static final long serialVersionUID = -7703965939581048575L;

	public FederalCouncilVicePresident() {

	}

	public FederalCouncilVicePresident(String description, Politician politician, Date validFrom, Date validTo) {
		super(description, politician, validFrom, validTo);
	}

	@Override
	public String toString() {
		return "FederalCouncilVicePresident [politician=" + getPolitician().getSurName() + ", validFrom=" + getValidFrom()
				+ ", validUntil=" + validUntil + "]";
	}

}
