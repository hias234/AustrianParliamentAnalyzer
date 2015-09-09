package at.jku.tk.hiesmair.gv.parliament.entities.mandate;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

@Entity
@DiscriminatorValue("FederalPresident")
public class FederalPresident extends Mandate {

	private static final long serialVersionUID = -36627430236710397L;

	public FederalPresident() {
	}

	public FederalPresident(String description, Politician politician, Date validFrom, Date validTo) {
		super(description, politician, validFrom, validTo);
	}

	@Override
	public String toString() {
		return "FederalPresident [politician=" + getPolitician().getSurName() + ", validFrom=" + getValidFrom() + ", validUntil="
				+ validUntil + "]";
	}

}
