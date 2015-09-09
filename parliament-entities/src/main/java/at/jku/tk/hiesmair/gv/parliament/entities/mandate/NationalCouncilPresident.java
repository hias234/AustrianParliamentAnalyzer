package at.jku.tk.hiesmair.gv.parliament.entities.mandate;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

@Entity
@DiscriminatorValue("NationalCouncilPresident")
public class NationalCouncilPresident extends Mandate {

	private static final long serialVersionUID = -2867183974599105084L;
	
	/** first, second or third president */
	private Integer position;

	public NationalCouncilPresident() {
		super();
	}

	public NationalCouncilPresident(String description, Politician politician, Date validFrom, Date validUntil,
			Integer position) {
		super(description, politician, validFrom, validUntil);
		this.position = position;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "NationalCouncilPresident [position=" + position + ", politician=" + getPolitician().getSurName()
				+ ", validFrom=" + getValidFrom() + ", validUntil=" + validUntil + "]";
	}

}
