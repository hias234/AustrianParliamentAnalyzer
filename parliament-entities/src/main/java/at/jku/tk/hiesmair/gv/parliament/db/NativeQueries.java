package at.jku.tk.hiesmair.gv.parliament.db;

public class NativeQueries {

	// --------
	// count session absences - all time
	protected final static String COUNT_SESSION_ABSENCES_OF_POLITICIAN = "select count(*) from "
			+ DBConstants.JOIN_TAB_NAME_SESSION_ABSENT_NCM + " acm inner join " + DBConstants.TAB_NAME_MANDATE
			+ " m on (m.id = acm.absent_ncm_id)";

	protected final static String COUNT_SESSION_PRESENCES_OF_POLITICIAN = "select count(*) from "
			+ DBConstants.JOIN_TAB_NAME_SESSION_PRESENT_NCM + " pcm inner join " + DBConstants.TAB_NAME_MANDATE
			+ " m on (m.id = pcm.present_ncm_id)";

	protected final static String COUNT_SA_PER_POLITICIAN_WHERE_CLAUSE = " where m.politician_id = p.id";
	protected final static String COUNT_SA_PER_POLITICIAN_QUERY = COUNT_SESSION_ABSENCES_OF_POLITICIAN
			+ COUNT_SA_PER_POLITICIAN_WHERE_CLAUSE;
	protected final static String COUNT_SP_PER_POLITICIAN_QUERY = COUNT_SESSION_PRESENCES_OF_POLITICIAN
			+ COUNT_SA_PER_POLITICIAN_WHERE_CLAUSE;

	public final static String COUNT_SESSION_ABSENCES_PER_POLITICIAN_QUERY = "SELECT p.id as id, ("
			+ COUNT_SA_PER_POLITICIAN_QUERY + ") as absence_count, (" + COUNT_SP_PER_POLITICIAN_QUERY
			+ ") as presence_count from " + DBConstants.TAB_NAME_POLITICIAN + " p where ("
			+ COUNT_SP_PER_POLITICIAN_QUERY + ") > 0 order by cast((" + COUNT_SA_PER_POLITICIAN_QUERY
			+ ") as double precision) / (" + COUNT_SP_PER_POLITICIAN_QUERY + ") desc";

	protected final static String COUNT_SA_PER_CLUB_WHERE_CLAUSE = " where m.club_shortname = c.short_name";
	protected final static String COUNT_SA_PER_CLUB_QUERY = COUNT_SESSION_ABSENCES_OF_POLITICIAN
			+ COUNT_SA_PER_CLUB_WHERE_CLAUSE;
	protected final static String COUNT_SP_PER_CLUB_QUERY = COUNT_SESSION_PRESENCES_OF_POLITICIAN
			+ COUNT_SA_PER_CLUB_WHERE_CLAUSE;

	public final static String COUNT_SESSION_ABSENCES_PER_CLUB_QUERY = "SELECT c.short_name as id, ("
			+ COUNT_SA_PER_CLUB_QUERY + ") as absence_count, (" + COUNT_SP_PER_CLUB_QUERY + ") as presence_count from "
			+ DBConstants.TAB_NAME_PARLIAMENT_CLUB + " c where (" + COUNT_SP_PER_CLUB_QUERY + ") > 0 order by cast(("
			+ COUNT_SA_PER_CLUB_QUERY + ") as double precision) / (" + COUNT_SP_PER_CLUB_QUERY + ") desc";

	// ----
	// count session absences by period
	protected final static String COUNT_SESSION_ABSENCES_OF_POLITICIAN_BY_PERIOD = "select count(*) from "
			+ DBConstants.JOIN_TAB_NAME_SESSION_ABSENT_NCM + " acm inner join " + DBConstants.TAB_NAME_MANDATE
			+ " m on (m.id = acm.absent_ncm_id) inner join " + DBConstants.JOIN_TAB_NAME_NCM_PERIOD
			+ " per on (m.id = per.ncm_id)";

	protected final static String COUNT_SESSION_PRESENCES_OF_POLITICIAN_BY_PERIOD = "select count(*) from "
			+ DBConstants.JOIN_TAB_NAME_SESSION_PRESENT_NCM + " pcm inner join " + DBConstants.TAB_NAME_MANDATE
			+ " m on (m.id = pcm.present_ncm_id) inner join " + DBConstants.JOIN_TAB_NAME_NCM_PERIOD
			+ " per on (m.id = per.ncm_id)";

	protected final static String COUNT_SA_PER_POLITICIAN_BY_PERIOD_WHERE_CLAUSE = " where m.politician_id = p.id and period between :periodFrom and :periodTo";
	protected final static String COUNT_SA_PER_POLITICIAN_BY_PERIOD_QUERY = COUNT_SESSION_ABSENCES_OF_POLITICIAN_BY_PERIOD
			+ COUNT_SA_PER_POLITICIAN_BY_PERIOD_WHERE_CLAUSE;
	protected final static String COUNT_SP_PER_POLITICIAN_BY_PERIOD_QUERY = COUNT_SESSION_PRESENCES_OF_POLITICIAN_BY_PERIOD
			+ COUNT_SA_PER_POLITICIAN_BY_PERIOD_WHERE_CLAUSE;

	public final static String COUNT_SESSION_ABSENCES_PER_POLITICIAN_BY_PERIOD_QUERY = "SELECT p.id as id, ("
			+ COUNT_SA_PER_POLITICIAN_BY_PERIOD_QUERY + ") as absence_count, ("
			+ COUNT_SP_PER_POLITICIAN_BY_PERIOD_QUERY + ") as presence_count from " + DBConstants.TAB_NAME_POLITICIAN
			+ " p where (" + COUNT_SP_PER_POLITICIAN_BY_PERIOD_QUERY + ") > 0 order by cast(("
			+ COUNT_SA_PER_POLITICIAN_BY_PERIOD_QUERY + ") as double precision) / ("
			+ COUNT_SP_PER_POLITICIAN_BY_PERIOD_QUERY + ") desc";

	protected final static String COUNT_SA_PER_CLUB_BY_PERIOD_WHERE_CLAUSE = " where m.club_shortname = c.short_name and period between :periodFrom and :periodTo";
	protected final static String COUNT_SA_PER_CLUB_BY_PERIOD_QUERY = COUNT_SESSION_ABSENCES_OF_POLITICIAN_BY_PERIOD
			+ COUNT_SA_PER_CLUB_BY_PERIOD_WHERE_CLAUSE;
	protected final static String COUNT_SP_PER_CLUB_BY_PERIOD_QUERY = COUNT_SESSION_PRESENCES_OF_POLITICIAN_BY_PERIOD
			+ COUNT_SA_PER_CLUB_BY_PERIOD_WHERE_CLAUSE;

	public final static String COUNT_SESSION_ABSENCES_PER_CLUB_BY_PERIOD_QUERY = "SELECT c.short_name as id, ("
			+ COUNT_SA_PER_CLUB_BY_PERIOD_QUERY + ") as absence_count, (" + COUNT_SP_PER_CLUB_BY_PERIOD_QUERY + ") as presence_count from "
			+ DBConstants.TAB_NAME_PARLIAMENT_CLUB + " c where (" + COUNT_SP_PER_CLUB_BY_PERIOD_QUERY + ") > 0 order by cast(("
			+ COUNT_SA_PER_CLUB_BY_PERIOD_QUERY + ") as double precision) / (" + COUNT_SP_PER_CLUB_BY_PERIOD_QUERY + ") desc";

}
