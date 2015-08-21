package at.jku.tk.hiesmair.gv.parlament.entities.discussion;

import java.util.List;

public class Discussion {

	private String topic;
	private String type;
	private List<DiscussionSpeech> speeches;

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

}
