package at.jku.tk.hiesmair.gv.parliament.db;

public class NativeQueries {

	protected final static String COUNT_SESSION_ABSENCES_OF_POLITICIAN = "select count(*) from "
			+ DBConstants.JOIN_TAB_NAME_SESSION_ABSENT_NCM + " acm inner join " + DBConstants.TAB_NAME_MANDATE
			+ " m on (m.id = acm.absent_ncm_id)";

	protected final static String COUNT_SESSION_PRESENCES_OF_POLITICIAN = "select count(*) from "
			+ DBConstants.JOIN_TAB_NAME_SESSION_PRESENT_NCM + " pcm inner join " + DBConstants.TAB_NAME_MANDATE
			+ " m on (m.id = pcm.present_ncm_id)";

	public final static String COUNT_SESSION_ABSENCES_QUERY = "SELECT p.id as id, ("
			+ COUNT_SESSION_ABSENCES_OF_POLITICIAN + " where m.politician_id = p.id) as absence_count, ("
			+ COUNT_SESSION_PRESENCES_OF_POLITICIAN + " where m.politician_id = p.id) as presence_count " + "from "
			+ DBConstants.TAB_NAME_POLITICIAN + " p where (" + COUNT_SESSION_PRESENCES_OF_POLITICIAN
			+ " where m.politician_id = p.id) > 0 order by cast((" + COUNT_SESSION_ABSENCES_OF_POLITICIAN
			+ " where m.politician_id = p.id) as double precision) / (" + COUNT_SESSION_PRESENCES_OF_POLITICIAN
			+ " where m.politician_id = p.id) desc";

	public final static String COUNT_SESSION_ABSENCES_PER_CLUB_QUERY = "SELECT c.short_name as id, ("
			+ COUNT_SESSION_ABSENCES_OF_POLITICIAN + " where m.club_shortname = c.short_name) as absence_count, ("
			+ COUNT_SESSION_PRESENCES_OF_POLITICIAN + " where m.club_shortname = c.short_name) as presence_count " + "from "
			+ DBConstants.TAB_NAME_PARLIAMENT_CLUB + " c where (" + COUNT_SESSION_PRESENCES_OF_POLITICIAN
			+ " where m.club_shortname = c.short_name) > 0 order by cast((" + COUNT_SESSION_ABSENCES_OF_POLITICIAN
			+ " where m.club_shortname = c.short_name) as double precision) / (" + COUNT_SESSION_PRESENCES_OF_POLITICIAN
			+ " where m.club_shortname = c.short_name) desc";
}
