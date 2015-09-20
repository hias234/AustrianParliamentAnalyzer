package at.jku.tk.hiesmair.gv.parliament.db.result;



public class AbsenceResult {

	private String id;
	private long absenceCount;
	private long presenceCount;

	public AbsenceResult(String id, Long absenceCount, Long presenceCount) {
		super();
		this.id = id;
		this.absenceCount = absenceCount;
		this.presenceCount = presenceCount;
	}

	public AbsenceResult() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getAbsenceCount() {
		return absenceCount;
	}

	public void setAbsenceCount(long absenceCount) {
		this.absenceCount = absenceCount;
	}

	public long getPresenceCount() {
		return presenceCount;
	}

	public void setPresenceCount(long presenceCount) {
		this.presenceCount = presenceCount;
	}

}
