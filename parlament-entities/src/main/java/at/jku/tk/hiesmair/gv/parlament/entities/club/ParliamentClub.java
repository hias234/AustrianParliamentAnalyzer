package at.jku.tk.hiesmair.gv.parlament.entities.club;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ParliamentClub {

	@Id
	private String shortName;
	private String longName;

	public ParliamentClub() {
		super();
	}

	public ParliamentClub(String shortName, String longName) {
		super();
		this.shortName = shortName;
		this.longName = longName;
	}

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
