package at.jku.tk.hiesmair.gv.parliament.etl.period.transformer.session.discussion;

import java.util.List;

import org.jsoup.nodes.Document;

import at.jku.tk.hiesmair.gv.parliament.entities.discussion.Discussion;
import at.jku.tk.hiesmair.gv.parliament.entities.session.Session;

public interface DiscussionTransformer {

	public abstract List<Discussion> getDiscussions(Document index, Document protocol, Session session)
			throws Exception;

}