package at.jku.tk.hiesmair.gv.parlament.entities.discussion;

public enum SpeechType {

	PRO, CONTRA, ACTUAL_CORRECTION, GOVERNMENT_BENCH, SIGNER, DISCUSSION_CUR_TOPIC, NORMAL_SPEECH,
	REASONING, URGENT_REQUEST, SHORT_DISCUSSION, OTHER;
	
	public static SpeechType getSpeechType(String abbreviation){
		switch (abbreviation.toLowerCase()) {
		case "p":
			return PRO;
		case "c":
			return CONTRA;
		case "tb":
			return ACTUAL_CORRECTION;
		case "rb":
			return GOVERNMENT_BENCH;
		case "un":
			return SIGNER;
		case "as":
			return DISCUSSION_CUR_TOPIC;
		case "wm":
			return NORMAL_SPEECH;
		case "bg":
			return REASONING;
		case "da":
			return URGENT_REQUEST;
		case "kd":
			return SHORT_DISCUSSION;
		default:
			return OTHER;
		}
	}
	
}
