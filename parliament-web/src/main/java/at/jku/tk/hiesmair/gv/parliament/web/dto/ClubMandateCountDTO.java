package at.jku.tk.hiesmair.gv.parliament.web.dto;

public class ClubMandateCountDTO {

	private ParliamentClubDTO club;
	private Integer mandateCount;

	public ClubMandateCountDTO(ParliamentClubDTO club, Integer mandateCount) {
		super();
		this.club = club;
		this.mandateCount = mandateCount;
	}

	public ParliamentClubDTO getClub() {
		return club;
	}

	public void setClub(ParliamentClubDTO club) {
		this.club = club;
	}

	public Integer getMandateCount() {
		return mandateCount;
	}

	public void setMandateCount(Integer mandateCount) {
		this.mandateCount = mandateCount;
	}

}
