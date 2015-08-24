package at.jku.tk.hiesmair.gv.parlament.entities.mandate;

import java.util.Date;

import at.jku.tk.hiesmair.gv.parlament.entities.Politician;
import at.jku.tk.hiesmair.gv.parlament.entities.club.ParliamentClub;

public abstract class CouncilMember extends Mandate {

	protected ParliamentClub club;

	public CouncilMember() {
	}

	public CouncilMember(String description, Politician politician, Date validFrom, Date validUntil, ParliamentClub club) {
		super(description, politician, validFrom, validUntil);
		this.club = club;
	}

	public ParliamentClub getClub() {
		return club;
	}

	public void setClub(ParliamentClub club) {
		this.club = club;
	}

}
