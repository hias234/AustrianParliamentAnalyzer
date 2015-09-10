package at.jku.tk.hiesmair.gv.parliament.entities.discussion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import at.jku.tk.hiesmair.gv.parliament.entities.discussion.speech.DiscussionSpeech;
import at.jku.tk.hiesmair.gv.parliament.entities.session.Session;

@Entity
public class Discussion implements Serializable {

	private static final long serialVersionUID = -2386763775742073194L;

	public static class DiscussionId implements Serializable {

		private static final long serialVersionUID = 1778296173315289747L;

		@ManyToOne(optional = false)
		private Session session;

		@Column(name = "discussion_order")
		private Integer order;

		public DiscussionId() {
			super();
		}

		public DiscussionId(Session session, Integer order) {
			super();
			this.session = session;
			this.order = order;
		}

		public Integer getOrder() {
			return order;
		}

		public void setOrder(Integer order) {
			this.order = order;
		}

		public Session getSession() {
			return session;
		}

		public void setSession(Session session) {
			this.session = session;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((order == null) ? 0 : order.hashCode());
			result = prime * result + ((session == null) ? 0 : session.hashCode());
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
			DiscussionId other = (DiscussionId) obj;
			if (order == null) {
				if (other.order != null)
					return false;
			}
			else
				if (!order.equals(other.order))
					return false;
			if (session == null) {
				if (other.session != null)
					return false;
			}
			else
				if (!session.equals(other.session))
					return false;
			return true;
		}

	}

	@EmbeddedId
	private DiscussionId id;

	private String topic;
	private String type;

	@OneToMany(mappedBy = "id.discussion")
	private List<DiscussionSpeech> speeches = new ArrayList<DiscussionSpeech>();

	public Discussion() {
		id = new DiscussionId();
	}

	public Discussion(Session session, Integer order, String topic, String type, List<DiscussionSpeech> speeches) {
		this.id = new DiscussionId(session, order);
		this.topic = topic;
		this.type = type;
		this.speeches = speeches;
	}

	public DiscussionId getId() {
		return id;
	}

	public void setId(DiscussionId id) {
		this.id = id;
	}

	public Integer getOrder() {
		return id.getOrder();
	}

	public void setOrder(Integer order) {
		this.id.setOrder(order);
	}

	public Session getSession() {
		return id.getSession();
	}

	public void setSession(Session session) {
		this.id.setSession(session);
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<DiscussionSpeech> getSpeeches() {
		return speeches;
	}

	public void setSpeeches(List<DiscussionSpeech> speeches) {
		this.speeches = speeches;
	}

	@Override
	public String toString() {
		return "Discussion [topic=" + topic + ", type=" + type + ", " + speeches.size() + " Speeches=" + speeches + "]";
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
		Discussion other = (Discussion) obj;
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
