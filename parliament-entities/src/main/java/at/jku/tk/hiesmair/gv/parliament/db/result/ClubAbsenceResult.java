package at.jku.tk.hiesmair.gv.parliament.db.result;

import at.jku.tk.hiesmair.gv.parliament.entities.club.ParliamentClub;

public class ClubAbsenceResult extends AbsenceResult {

	private ParliamentClub club;

	public ClubAbsenceResult() {
	}

	public ClubAbsenceResult(String id, Long absenceCount, Long presenceCount, ParliamentClub club) {
		super(id, absenceCount, presenceCount);
		this.club = club;
	}

	public ParliamentClub getClub() {
		return club;
	}

	public void setClub(ParliamentClub club) {
		this.club = club;
	}

}
