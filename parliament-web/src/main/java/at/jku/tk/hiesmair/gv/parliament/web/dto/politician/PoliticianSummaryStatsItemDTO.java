package at.jku.tk.hiesmair.gv.parliament.web.dto.politician;

import at.jku.tk.hiesmair.gv.parliament.web.dto.absence.AbsenceDTO;

public class PoliticianSummaryStatsItemDTO {

	private PoliticianDTO politician;
	private AbsenceDTO absence;
	private Integer speechCount;

	public PoliticianSummaryStatsItemDTO() {
		super();
	}

	public PoliticianSummaryStatsItemDTO(PoliticianDTO politician, AbsenceDTO absence, Integer speechCount) {
		super();
		this.politician = politician;
		this.absence = absence;
		this.speechCount = speechCount;
	}

	public PoliticianDTO getPolitician() {
		return politician;
	}

	public void setPolitician(PoliticianDTO politician) {
		this.politician = politician;
	}

	public AbsenceDTO getAbsence() {
		return absence;
	}

	public void setAbsence(AbsenceDTO absence) {
		this.absence = absence;
	}

	public Integer getSpeechCount() {
		return speechCount;
	}

	public void setSpeechCount(Integer speechCount) {
		this.speechCount = speechCount;
	}

}
