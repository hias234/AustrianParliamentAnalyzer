package at.jku.tk.hiesmair.gv.parliament.web.dto.absence;

public class AbsenceDTO {

	private long absenceCount;
	private long presenceCount;

	public AbsenceDTO() {
		super();
	}

	public AbsenceDTO(long absenceCount, long presenceCount) {
		super();
		this.absenceCount = absenceCount;
		this.presenceCount = presenceCount;
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

	public double getAbsencePercentage(){
		return Double.valueOf(absenceCount) / (absenceCount + presenceCount);
	}
}
