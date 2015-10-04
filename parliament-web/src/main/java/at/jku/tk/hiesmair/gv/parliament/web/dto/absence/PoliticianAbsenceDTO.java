package at.jku.tk.hiesmair.gv.parliament.web.dto.absence;

import at.jku.tk.hiesmair.gv.parliament.web.dto.politician.PoliticianDTO;

public class PoliticianAbsenceDTO extends AbsenceDTO {

	protected PoliticianDTO politician;

	public PoliticianAbsenceDTO() {
		super();
	}

	public PoliticianAbsenceDTO(long absenceCount, long presenceCount, PoliticianDTO politician) {
		super(absenceCount, presenceCount);
		this.politician = politician;
	}

	public PoliticianDTO getPolitician() {
		return politician;
	}

	public void setPolitician(PoliticianDTO politician) {
		this.politician = politician;
	}
}
