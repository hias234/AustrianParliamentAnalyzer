package at.jku.tk.hiesmair.gv.parliament.entities.mandate;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

@Entity
@DiscriminatorValue("FederalChancellor")
public class FederalChancellor extends Mandate {

	private static final long serialVersionUID = -4451036452058488232L;

	public FederalChancellor() {
	}
	
	public FederalChancellor(String description, Politician politician, Date validFrom, Date validTo) {
		super(description, politician, validFrom, validTo);
	}
	
	@Override
	public String toString() {
		return "FederalChancellor [politician=" + getPolitician().getSurName() + ", validFrom=" + getValidFrom()
				+ ", validUntil=" + validUntil + "]";
	}

}
