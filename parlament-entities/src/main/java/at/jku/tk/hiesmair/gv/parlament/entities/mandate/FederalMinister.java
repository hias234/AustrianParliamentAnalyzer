package at.jku.tk.hiesmair.gv.parlament.entities.mandate;

public class FederalMinister extends Mandate {

	private String department;

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	@Override
	public String toString() {
		return "FederalMinister [department=" + department + ", politician=" + politician.getSurName() + ", validFrom="
				+ validFrom + ", validUntil=" + validUntil + "]";
	}

}
