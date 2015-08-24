package at.jku.tk.hiesmair.gv.parlament.entities.mandate;

import java.util.Date;

import at.jku.tk.hiesmair.gv.parlament.entities.Politician;

public class NationalCouncilPresident extends Mandate {

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
		return "NationalCouncilPresident [position=" + position + ", politician=" + politician.getSurName()
				+ ", validFrom=" + validFrom + ", validUntil=" + validUntil + "]";
	}

}
