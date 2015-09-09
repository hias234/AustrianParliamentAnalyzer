package at.jku.tk.hiesmair.gv.parliament.entities.mandate;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

@Entity
@DiscriminatorValue("FederalMinister")
public class FederalMinister extends Mandate {

	private static final long serialVersionUID = -190979824888219416L;
	
	protected String department;

	public FederalMinister() {
		super();
	}

	public FederalMinister(String description, Politician politician, Date validFrom, Date validUntil, String department) {
		super(description, politician, validFrom, validUntil);
		this.department = department;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	@Override
	public String toString() {
		return "FederalMinister [department=" + department + ", politician=" + getPolitician().getSurName() + ", validFrom="
				+ getValidFrom() + ", validUntil=" + validUntil + "]";
	}

}
