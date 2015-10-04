package at.jku.tk.hiesmair.gv.parliament.web.dto.period;

import java.util.List;

import at.jku.tk.hiesmair.gv.parliament.web.dto.ClubMandateCountDTO;

public class LegislativePeriodStatisticDataDTO {

	private Integer period;
	private Double absencePercentage;
	private Integer sessionCount;
	private List<ClubMandateCountDTO> nationalCouncilMemberCount;

	public LegislativePeriodStatisticDataDTO() {
		super();
	}

	public LegislativePeriodStatisticDataDTO(Integer period, Double absencePercentage) {
		super();
		this.period = period;
		this.absencePercentage = absencePercentage;
	}

	public LegislativePeriodStatisticDataDTO(Integer period, Double absencePercentage, Integer sessionCount) {
		super();
		this.period = period;
		this.absencePercentage = absencePercentage;
		this.sessionCount = sessionCount;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public Double getAbsencePercentage() {
		return absencePercentage;
	}

	public void setAbsencePercentage(Double absencePercentage) {
		this.absencePercentage = absencePercentage;
	}

	public Integer getSessionCount() {
		return sessionCount;
	}

	public void setSessionCount(Integer sessionCount) {
		this.sessionCount = sessionCount;
	}

	public List<ClubMandateCountDTO> getNationalCouncilMemberCount() {
		return nationalCouncilMemberCount;
	}

	public void setNationalCouncilMemberCount(List<ClubMandateCountDTO> nationalCouncilMemberCount) {
		this.nationalCouncilMemberCount = nationalCouncilMemberCount;
	}

}
