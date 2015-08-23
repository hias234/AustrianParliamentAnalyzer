package at.jku.tk.hiesmair.gv.parlament.entities.mandate;

import at.jku.tk.hiesmair.gv.parlament.entities.club.ParliamentClub;

public abstract class CouncilMember extends Mandate {

	protected ParliamentClub club;

	public ParliamentClub getClub() {
		return club;
	}

	public void setClub(ParliamentClub club) {
		this.club = club;
	}

	
}
