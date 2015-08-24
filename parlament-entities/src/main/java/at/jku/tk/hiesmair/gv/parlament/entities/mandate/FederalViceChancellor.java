package at.jku.tk.hiesmair.gv.parlament.entities.mandate;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import at.jku.tk.hiesmair.gv.parlament.entities.Politician;

@Entity
@DiscriminatorValue("FederalViceChancellor")
public class FederalViceChancellor extends Mandate {

	public FederalViceChancellor() {
	}

	public FederalViceChancellor(String description, Politician politician, Date validFrom, Date validTo) {
		super(description, politician, validFrom, validTo);
	}
	
	
	@Override
	public String toString() {
		return "VizeFederalChancellor [politician=" + politician.getSurName() + ", validFrom=" + validFrom
				+ ", validUntil=" + validUntil + "]";
	}

}
