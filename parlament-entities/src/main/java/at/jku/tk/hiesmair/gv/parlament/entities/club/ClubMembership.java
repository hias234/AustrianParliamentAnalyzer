package at.jku.tk.hiesmair.gv.parlament.entities.club;

import java.util.Date;

import at.jku.tk.hiesmair.gv.parlament.entities.Politician;

public class ClubMembership {

	private Politician politician;
	private ParliamentClub club;
	private Date validFrom;
	private Date validUntil;
	private String function;

	public Politician getPolitician() {
		return politician;
	}

	public void setPolitician(Politician politician) {
		this.politician = politician;
	}

	public ParliamentClub getClub() {
		return club;
	}

	public void setClub(ParliamentClub club) {
		this.club = club;
	}

	public Date getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public Date getValidUntil() {
		return validUntil;
	}

	public void setValidUntil(Date validUntil) {
		this.validUntil = validUntil;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	@Override
	public String toString() {
		return "ClubMembership [politician=" + politician.getSurName() + ", club=" + club.getShortName() + ", validFrom=" + validFrom + ", validUntil=" + validUntil + ", function="
				+ function + "]";
	}

}
