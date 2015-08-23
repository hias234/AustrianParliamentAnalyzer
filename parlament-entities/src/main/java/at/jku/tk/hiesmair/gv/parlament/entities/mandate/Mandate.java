package at.jku.tk.hiesmair.gv.parlament.entities.mandate;

import java.util.Date;

import at.jku.tk.hiesmair.gv.parlament.entities.Politician;

public class Mandate {

	protected String description;
	protected Politician politician;
	protected Date validFrom;
	protected Date validUntil;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Politician getPolitician() {
		return politician;
	}

	public void setPolitician(Politician politician) {
		this.politician = politician;
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

	@Override
	public String toString() {
		return "Mandate [description=" + description + ", politician=" + politician.getSurName() + ", validFrom=" + validFrom
				+ ", validUntil=" + validUntil + "]";
	}

	

}
