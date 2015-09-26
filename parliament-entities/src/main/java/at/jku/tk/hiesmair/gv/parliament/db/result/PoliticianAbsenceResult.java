package at.jku.tk.hiesmair.gv.parliament.db.result;

import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

public class PoliticianAbsenceResult extends AbsenceResult {

	private Politician politician;

	public PoliticianAbsenceResult() {
		super();
	}

	public PoliticianAbsenceResult(String id, Long absenceCount, Long presenceCount, Politician politician) {
		super(id, absenceCount, presenceCount);
		this.politician = politician;
	}

	public Politician getPolitician() {
		return politician;
	}

	public void setPolitician(Politician politician) {
		this.politician = politician;
	}

}
