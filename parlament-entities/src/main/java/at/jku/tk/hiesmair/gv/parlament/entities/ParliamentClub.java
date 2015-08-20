package at.jku.tk.hiesmair.gv.parlament.entities;

public class ParliamentClub {
	private String shortName;
	private String longName;

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getLongName() {
		return longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}

	@Override
	public String toString() {
		return "ParliamentClub [shortName=" + shortName + ", longName=" + longName + "]";
	}

}
