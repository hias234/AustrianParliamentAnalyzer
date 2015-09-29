package at.jku.tk.hiesmair.gv.parliament.web.dto;

import java.util.Date;
import java.util.List;

public class MandateDTO {

	protected Integer id;
	protected String description;
	protected Date validFrom;
	protected Date validUntil;
	protected String type;
	protected ParliamentClubDTO club;
	protected List<SimpleLegislativePeriodDTO> periods;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public ParliamentClubDTO getClub() {
		return club;
	}

	public void setClub(ParliamentClubDTO club) {
		this.club = club;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<SimpleLegislativePeriodDTO> getPeriods() {
		return periods;
	}

	public void setPeriods(List<SimpleLegislativePeriodDTO> periods) {
		this.periods = periods;
	}

}
