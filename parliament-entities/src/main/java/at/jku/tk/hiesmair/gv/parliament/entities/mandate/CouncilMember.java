package at.jku.tk.hiesmair.gv.parliament.entities.mandate;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import at.jku.tk.hiesmair.gv.parliament.entities.club.ParliamentClub;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

@Entity
public abstract class CouncilMember extends Mandate {

	private static final long serialVersionUID = 1216373484049667243L;
	
	@ManyToOne
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
