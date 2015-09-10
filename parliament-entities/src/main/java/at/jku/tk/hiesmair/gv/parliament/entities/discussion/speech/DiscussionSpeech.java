package at.jku.tk.hiesmair.gv.parliament.entities.discussion.speech;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import at.jku.tk.hiesmair.gv.parliament.entities.discussion.Discussion;
import at.jku.tk.hiesmair.gv.parliament.entities.discussion.speech.sentiment.DiscussionSpeechSentiment;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;

@Entity
public class DiscussionSpeech implements Serializable {

	private static final long serialVersionUID = -3611117803439285095L;

	public static class DiscussionSpeechId implements Serializable {

		private static final long serialVersionUID = -3881934045732709180L;

		@Column(name = "speech_order")
		private Integer order;

		@ManyToOne(optional = false)
		private Discussion discussion;

		public DiscussionSpeechId() {
			super();
		}

		public DiscussionSpeechId(Integer order, Discussion discussion) {
			super();
			this.order = order;
			this.discussion = discussion;
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
			DiscussionSpeechId other = (DiscussionSpeechId) obj;
			if (discussion == null) {
				if (other.discussion != null)
					return false;
			}
			else
				if (!discussion.equals(other.discussion))
					return false;
			if (order == null) {
				if (other.order != null)
					return false;
			}
			else
				if (!order.equals(other.order))
					return false;
			return true;
		}

	}

	@EmbeddedId
	private DiscussionSpeechId id;

	@ManyToOne(optional = false)
	private Politician politician;

	@OneToMany(mappedBy = "speech", cascade = CascadeType.ALL)
	private List<DiscussionSpeechSentiment> sentiments;

	@Temporal(TemporalType.TIME)
	private Date startTime;

	@Temporal(TemporalType.TIME)
	private Date endTime;

	@Enumerated(EnumType.STRING)
	private SpeechType type;

	@Column(length = 1000000)
	private String text;

	public DiscussionSpeech() {
		id = new DiscussionSpeechId();
	}

	public DiscussionSpeech(Discussion discussion, Integer order, Politician politician,
			List<DiscussionSpeechSentiment> sentiments, Date startTime, Date endTime, SpeechType type, String text) {
		this.id = new DiscussionSpeechId(order, discussion);
		this.politician = politician;
		this.sentiments = sentiments;
		this.startTime = startTime;
		this.endTime = endTime;
		this.type = type;
		this.text = text;
	}

	public DiscussionSpeechId getId() {
		return id;
	}

	public void setId(DiscussionSpeechId id) {
		this.id = id;
	}

	public Integer getOrder() {
		return id.getOrder();
	}

	public void setOrder(Integer order) {
		this.id.setOrder(order);
	}

	public Discussion getDiscussion() {
		return id.getDiscussion();
	}

	public void setDiscussion(Discussion discussion) {
		this.id.setDiscussion(discussion);
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
		return "DiscussionSpeech " + (text == null) + " [discussion="
				+ (getDiscussion() == null ? "null" : getDiscussion().getTopic()) + ", politician="
				+ (politician == null ? "null" : politician.getSurName()) + ", startTime=" + startTime + ", endTime="
				+ endTime + ", type=" + type + ", text=" + text + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else
			if (!id.equals(other.id))
				return false;
		return true;
	}

}
