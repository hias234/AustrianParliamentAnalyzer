package at.jku.tk.hiesmair.gv.parliament.etl;


public abstract class AbstractTransformer {

	protected static final String NBSP_STRING = Character.toString((char) 160);
	protected static final String SOFT_HYPHEN_STRING = Character.toString((char) 173);
	protected static final String EN_DASH_STRING = Character.toString((char) 8211);
	protected static final String EM_DASH_STRING = Character.toString((char) 8212);
	
}
