package at.jku.tk.hiesmair.gv.parliament.web.dto;

public class LegislativePeriodStatisticDataDTO {

	private Integer period;
	private Double absencePercentage;

	public LegislativePeriodStatisticDataDTO() {
		super();
	}

	public LegislativePeriodStatisticDataDTO(Integer period, Double absencePercentage) {
		super();
		this.period = period;
		this.absencePercentage = absencePercentage;
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

}
