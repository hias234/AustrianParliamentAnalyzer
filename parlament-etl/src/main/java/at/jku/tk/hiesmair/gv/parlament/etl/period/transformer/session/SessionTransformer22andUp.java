package at.jku.tk.hiesmair.gv.parlament.etl.period.transformer.session;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import at.jku.tk.hiesmair.gv.parlament.etl.period.transformer.session.discussion.DiscussionTransformer22andUp;
import at.jku.tk.hiesmair.gv.parlament.etl.politician.transformer.PoliticianTransformer;

/**
 * Transforms the Protocols of a Session into a session object
 * 
 * @author Markus
 *
 */
@Component
public class SessionTransformer22andUp extends AbstractSessionTransformer {

	private static final Logger logger = Logger.getLogger(SessionTransformer22andUp.class.getSimpleName());

	@Inject
	public SessionTransformer22andUp(PoliticianTransformer politicianTransformer, DiscussionTransformer22andUp discussionTransformer) {
		super(politicianTransformer, discussionTransformer);
	}

	
}
