package at.jku.tk.hiesmair.gv.parlament.entities.mandate;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import at.jku.tk.hiesmair.gv.parlament.entities.Politician;
import at.jku.tk.hiesmair.gv.parlament.entities.club.ParliamentClub;

@Entity
public abstract class CouncilMember extends Mandate {

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
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
