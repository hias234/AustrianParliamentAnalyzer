package at.jku.tk.hiesmair.gv.parlament.entities.mandate;

public class NationalCouncilPresident extends Mandate {

	/** first, second or third president */
	private Integer position;

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "NationalCouncilPresident [position=" + position + ", politician=" + politician.getSurName() + ", validFrom="
				+ validFrom + ", validUntil=" + validUntil + "]";
	}

	
}
