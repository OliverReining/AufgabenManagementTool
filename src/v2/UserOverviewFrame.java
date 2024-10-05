package v2;

import javax.swing.*;

// --- Übersicht eines einzelnen Benutzers - Daten (Name, Vorname, Email) ---
// --- abgeschlossene Aufaben - offene Aufgaben - bei wie vielen Projekten dabei ---
// --- durchschnittliche Bearbeitungszeiten - weiterer analytics ---
// --- Aufgaben an denen Nutzer beteiligt ist - Namen der einzelnen Aufgaben - zu welchem Projekt? - weitere Mitarbeiter ---
// --- Projekte an denen Nutzer beteiligt ist - andere Mitarbeiter der Projekte - Projektleiter ---

@SuppressWarnings("serial")
public class UserOverviewFrame extends JFrame {

	private LogManager log;
	private User user;

	public UserOverviewFrame(User user, LogManager logManager) {
		this.user = user;
		this.log = logManager;

		setTitle("Benutzer im Detail");
		
	}

}
