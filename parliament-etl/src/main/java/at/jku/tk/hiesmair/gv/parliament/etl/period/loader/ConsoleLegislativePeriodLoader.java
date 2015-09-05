package at.jku.tk.hiesmair.gv.parliament.etl.period.loader;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import at.jku.tk.hiesmair.gv.parliament.entities.LegislativePeriod;
import at.jku.tk.hiesmair.gv.parliament.entities.discussion.Discussion;
import at.jku.tk.hiesmair.gv.parliament.entities.politician.Politician;
import at.jku.tk.hiesmair.gv.parliament.entities.session.Session;

public class ConsoleLegislativePeriodLoader implements LegislativePeriodLoader {

	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

	@Override
	public void loadLegislativePeriods(List<LegislativePeriod> periods) {
		for (LegislativePeriod period : periods) {
			printPeriod(period);
			printSessions(period.getSessions());
		}
	}

	protected void printSessions(List<Session> sessions) {
		System.out.println("Sessions");
		System.out.println("-------------------------------------------");
		for (Session session : sessions) {
			printSession(session);
		}
	}

	protected void printSession(Session session) {
		System.out.println("Session " + session.getSessionNr());
		if (session.getStartDate() != null) {
			System.out.print(dateFormat.format(session.getStartDate()));
			if (session.getEndDate() != null) {
				System.out.print(" - " + dateFormat.format(session.getEndDate()));
			}
			System.out.println();
		}
		System.out.println("-------------------------------------------");

		System.out.print("Absent Politicians (" + session.getAbsentNationalCouncilMembers().size() + "): ");
		printPoliticians(session.getAbsentNationalCouncilMembers().stream().map(ncm -> ncm.getPolitician())
				.collect(Collectors.toList()));

		// System.out.println("Discussions: ");
		// printDiscussions(session.getDiscussions());

		System.out.println("-------------------------------------------");
	}

	private void printDiscussions(List<Discussion> discussions) {
		for (Discussion discussion : discussions) {
			printDiscussion(discussion);
		}

		System.out.println();
	}

	private void printDiscussion(Discussion discussion) {
		System.out.println("Discussion " + discussion.getTopic());
		System.out.println(discussion.getType());
		System.out.println(discussion.getSpeeches());
		System.out.println();
	}

	protected void printPoliticians(List<Politician> politicians) {
		for (Politician p : politicians) {
			System.out.print(p.getFullName());
			System.out.print(", ");
		}
		System.out.println();
		System.out.println();
	}

	protected void printPeriod(LegislativePeriod period) {
		System.out.println("Period " + period);
		System.out.println("-------------------------------------------");
		System.out.println();
	}

}
