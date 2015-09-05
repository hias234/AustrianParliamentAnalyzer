package at.jku.tk.hiesmair.gv.parliament.entities.discussion;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

@Entity
public class DiscussionSpeech {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "speech_order")
	private Integer order;

	@ManyToOne(optional = false, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Discussion discussion;

	@ManyToOne(optional = false, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Politician politician;

	@OneToMany(mappedBy = "speech", cascade = CascadeType.ALL)
	private List<DiscussionSpeechSentiment> sentiments;

	@Temporal(TemporalType.TIME)
	private Date startTime;

	@Temporal(TemporalType.TIME)
	private Date endTime;

	@Enumerated(EnumType.STRING)
	private SpeechType type;

	@Column(length = 100000)
	private String text;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<DiscussionSpeechSentiment> getSentiments() {
		return sentiments;
	}

	public void setSentiments(List<DiscussionSpeechSentiment> sentiments) {
		this.sentiments = sentiments;
	}

	@Override
	public String toString() {
		return "DiscussionSpeech [discussion=" + (discussion == null ? "null" : discussion.getTopic())
				+ ", politician=" + (politician == null ? "null" : politician.getSurName()) + ", startTime="
				+ startTime + ", endTime=" + endTime + ", type=" + type + "]";
	}

}
