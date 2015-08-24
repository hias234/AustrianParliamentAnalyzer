package at.jku.tk.hiesmair.gv.parlament.entities.discussion;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import at.jku.tk.hiesmair.gv.parlament.entities.Politician;

@Entity
public class DiscussionSpeech {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;

	@ManyToOne(optional = false)
	private Discussion discussion;
	
	@ManyToOne(optional = false)
	private Politician politician;
	
	@Temporal(TemporalType.TIME)
	private Date startTime;
	
	@Temporal(TemporalType.TIME)
	private Date endTime;

	@Enumerated(EnumType.STRING)
	private SpeechType type;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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
		return "DiscussionSpeech [discussion=" + (discussion == null ? "null" : discussion.getTopic())
				+ ", politician=" + (politician == null ? "null" : politician.getSurName()) + ", startTime="
				+ startTime + ", endTime=" + endTime + ", type=" + type + "]";
	}

}
