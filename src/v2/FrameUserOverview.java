package v2;

import java.awt.BorderLayout;

import javax.swing.*;

//--- Übersicht eines einzelnen Benutzers - Daten (Name, Vorname, Email) ---
//--- abgeschlossene Aufaben - offene Aufgaben - bei wie vielen Projekten dabei ---
//--- durchschnittliche Bearbeitungszeiten - weiterer analytics ---
//--- Aufgaben an denen Nutzer beteiligt ist - Namen der einzelnen Aufgaben - zu welchem Projekt? - weitere Mitarbeiter ---
//--- Projekte an denen Nutzer beteiligt ist - andere Mitarbeiter der Projekte - Projektleiter ---
public class FrameUserOverview extends JFrame {

	private UserManager uMan;
	private LogManager log;
	private User user;

	public FrameUserOverview(User user, LogManager logManager, UserManager uMan) {
		this.log = logManager;
		this.uMan = uMan;
		this.user = user;

		setTitle("Benutzer im Detail");
		setTitle("Nutzerübersicht");
		setSize(650, 400);
		setLocationRelativeTo(null);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		UserTableModel model = new UserTableModel(uMan.getUsers());
		JTable table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);

		panel.add(scrollPane);
		add(panel);

	}

}
