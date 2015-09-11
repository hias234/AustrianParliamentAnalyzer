package at.jku.tk.hiesmair.gv.parliament.etl.period.transformer.session;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import at.jku.tk.hiesmair.gv.parliament.etl.period.transformer.session.discussion.DiscussionTransformer21;
import at.jku.tk.hiesmair.gv.parliament.etl.politician.transformer.PoliticianTransformer;

@Component
public class SessionTransformer21 extends AbstractSessionTransformer {

	private static final Logger logger = Logger.getLogger(SessionTransformer21.class.getSimpleName());

	@Inject
	public SessionTransformer21(PoliticianTransformer politicianTransformer, DiscussionTransformer21 discussionTransformer) {
		super(politicianTransformer, discussionTransformer);
	}

	@Override
	public Integer getSessionTranformerFromPeriod() {
		return 21;
	}

	@Override
	public Integer getSessionTranformerToPeriod() {
		return 21;
	}
}
