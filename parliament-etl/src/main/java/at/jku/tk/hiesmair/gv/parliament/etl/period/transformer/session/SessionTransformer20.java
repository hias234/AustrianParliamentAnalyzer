package at.jku.tk.hiesmair.gv.parliament.etl.period.transformer.session;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import at.jku.tk.hiesmair.gv.parliament.etl.period.transformer.session.discussion.DiscussionTransformer20;
import at.jku.tk.hiesmair.gv.parliament.etl.politician.transformer.PoliticianTransformer;

@Component
public class SessionTransformer20 extends AbstractSessionTransformer {

	@Inject
	public SessionTransformer20(PoliticianTransformer politicianTransformer, DiscussionTransformer20 discussionTransformer) {
		super(politicianTransformer, discussionTransformer);
	}

	@Override
	public Integer getSessionTranformerFromPeriod() {
		return 20;
	}

	@Override
	public Integer getSessionTranformerToPeriod() {
		return 20;
	}
	
}
