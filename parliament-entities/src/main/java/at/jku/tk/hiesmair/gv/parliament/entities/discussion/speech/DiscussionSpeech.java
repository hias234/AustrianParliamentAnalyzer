package at.jku.tk.hiesmair.gv.parliament.entities.discussion.speech;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import at.jku.tk.hiesmair.gv.parliament.entities.discussion.Discussion;
import at.jku.tk.hiesmair.gv.parliament.entities.discussion.speech.sentiment.DiscussionSpeechSentiment;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

@Entity
@Table(name = "discussion_speech", uniqueConstraints = { @UniqueConstraint(columnNames = { "discussion_id",
		"speech_order" }) })
public class DiscussionSpeech implements Serializable {

	private static final long serialVersionUID = -3611117803439285095L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "speech_order", nullable = false)
	private Integer order;

	@JoinColumn(name = "discussion_id")
	@ManyToOne(optional = false)
	private Discussion discussion;

	@ManyToOne(optional = false)
	private Politician politician;

	@OneToMany(mappedBy = "speech", fetch = FetchType.EAGER)
	private List<DiscussionSpeechSentiment> sentiments = new ArrayList<DiscussionSpeechSentiment>();

	@Temporal(TemporalType.TIME)
	private Date startTime;

	@Temporal(TemporalType.TIME)
	private Date endTime;

	@Enumerated(EnumType.STRING)
	private SpeechType type;

	@Column(length = 1000000)
	private String text;

	public DiscussionSpeech() {
	}

	public DiscussionSpeech(Discussion discussion, Integer order, Politician politician,
			List<DiscussionSpeechSentiment> sentiments, Date startTime, Date endTime, SpeechType type, String text) {
		this.discussion = discussion;
		this.order = order;
		this.politician = politician;
		this.sentiments = sentiments;
		this.startTime = startTime;
		this.endTime = endTime;
		this.type = type;
		this.text = text;
	}

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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((discussion == null) ? 0 : discussion.hashCode());
		result = prime * result + ((order == null) ? 0 : order.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DiscussionSpeech other = (DiscussionSpeech) obj;
		if (discussion == null) {
			if (other.discussion != null)
				return false;
		}
		else if (!discussion.equals(other.discussion))
			return false;
		if (order == null) {
			if (other.order != null)
				return false;
		}
		else if (!order.equals(other.order))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DiscussionSpeech " + (text == null) + " [discussion="
				+ (getDiscussion() == null ? "null" : getDiscussion().getTopic()) + ", politician="
				+ (politician == null ? "null" : politician.getSurName()) + ", startTime=" + startTime + ", endTime="
				+ endTime + ", type=" + type + ", text=" + text + "]";
	}

}
