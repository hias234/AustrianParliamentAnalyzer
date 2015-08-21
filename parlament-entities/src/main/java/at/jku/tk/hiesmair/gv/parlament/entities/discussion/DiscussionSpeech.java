package at.jku.tk.hiesmair.gv.parlament.entities.discussion;

import java.util.Date;

import at.jku.tk.hiesmair.gv.parlament.entities.Politician;

public class DiscussionSpeech {

	private Discussion discussion;
	private Politician politician;
	private Date startTime;
	private Date endTime;
	private SpeechType type;

	public Discussion getDiscussion() {
		return discussion;
	}

	public void setDiscussion(Discussion discussion) {
		this.discussion = discussion;
	}

	public Politician getPolitician() {
		return politician;
	}

	public void setPolitician(Politician politician) {
		this.politician = politician;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public SpeechType getType() {
		return type;
	}

	public void setType(SpeechType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "DiscussionSpeech [discussion=" + (discussion == null ? "null" : discussion.getTopic()) + ", politician=" + (politician == null ? "null" : politician.getSurName()) + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", type=" + type + "]";
	}

	
}
