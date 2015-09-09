package at.jku.tk.hiesmair.gv.parliament.entities.mandate;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

@Entity
@DiscriminatorValue("FederalViceChancellor")
public class FederalViceChancellor extends Mandate {

	private static final long serialVersionUID = 2743913069633523964L;

	public FederalViceChancellor() {
	}

	public FederalViceChancellor(String description, Politician politician, Date validFrom, Date validTo) {
		super(description, politician, validFrom, validTo);
	}

	@Override
	public String toString() {
		return "VizeFederalChancellor [politician=" + getPolitician().getSurName() + ", validFrom=" + getValidFrom()
				+ ", validUntil=" + validUntil + "]";
	}

}
