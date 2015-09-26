package at.jku.tk.hiesmair.gv.parliament.web.dto.absence;

import at.jku.tk.hiesmair.gv.parliament.web.dto.ParliamentClubDTO;

public class ClubAbsenceDTO extends AbsenceDTO {

	protected ParliamentClubDTO club;

	public ClubAbsenceDTO() {
		super();
	}

	public ClubAbsenceDTO(long absenceCount, long presenceCount, ParliamentClubDTO club) {
		super(absenceCount, presenceCount);
		this.club = club;
	}

	public ParliamentClubDTO getClub() {
		return club;
	}

	public void setClub(ParliamentClubDTO club) {
		this.club = club;
	}
}
