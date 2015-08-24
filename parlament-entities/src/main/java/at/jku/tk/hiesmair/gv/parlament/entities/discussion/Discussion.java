package at.jku.tk.hiesmair.gv.parlament.entities.discussion;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import at.jku.tk.hiesmair.gv.parlament.entities.Session;

@Entity
public class Discussion {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne(optional = false)
	private Session session;

	@Column(name = "session_order")
	private Integer order;
	private String topic;
	private String type;

	@OneToMany(fetch = FetchType.EAGER)
	private List<DiscussionSpeech> speeches = new ArrayList<DiscussionSpeech>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
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

}
