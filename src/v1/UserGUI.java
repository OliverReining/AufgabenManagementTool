package v1;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

@SuppressWarnings("serial")
public class UserGUI extends JFrame {

	private int userId;
	private Object[] userData;
	private boolean isProjectLead = false;

	public UserGUI(int userId) {
		this.userId = userId;
		this.userData = Analytics.showUserData(userId);
		this.isProjectLead = Analytics.isProjectLead(userId);

		setTitle("Benutzerübersicht");
		setSize(530, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);
		setResizable(false);

		JTabbedPane tabbedPane = new JTabbedPane();

		// Benutzer Übersicht (name, vorname, email, userid, passwort ändern...)
		JPanel userOverview = userOverviewPanel();
		tabbedPane.addTab("Übersicht", userOverview);

		// Übersicht über die Projekte an denen der User mitarbeitet
		JPanel projectOverwiev = projectOverwievPanel();
		tabbedPane.addTab("Projekte", projectOverwiev);

		// Übersicht der einzelnen Aufgaben
		JPanel TasksOverview = tasksOverviewPanel();
		tabbedPane.addTab("Aufgaben", TasksOverview);

		// falls ProjectLead, wird zusätzlicher Tab angezeigt
		if (isProjectLead) {
			JPanel projectLeadPanel = projectLeadPanel();
			tabbedPane.add("Projektleitung", projectLeadPanel);
		}

		add(tabbedPane, BorderLayout.CENTER);

	}

	private JPanel userOverviewPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(null);

		JLabel nameLabel = new JLabel("Name:");
		nameLabel.setBounds(20, 20, 150, 25);
		panel.add(nameLabel);

		JLabel nameLabel2 = new JLabel(userData[1].toString());
		nameLabel2.setBounds(150, 20, 150, 25);
		panel.add(nameLabel2);

		JLabel vornameLabel = new JLabel("Vorname:");
		vornameLabel.setBounds(20, 40, 150, 25);
		panel.add(vornameLabel);

		JLabel vornameLabel2 = new JLabel(userData[2].toString());
		vornameLabel2.setBounds(150, 40, 150, 25);
		panel.add(vornameLabel2);

		JLabel emailLabel = new JLabel("E-Mail:");
		emailLabel.setBounds(20, 60, 150, 25);
		panel.add(emailLabel);

		JLabel emailLabel2 = new JLabel(userData[3].toString());
		emailLabel2.setBounds(150, 60, 150, 25);
		panel.add(emailLabel2);

		JLabel projectsLabel = new JLabel("Aktuelle Projekte:");
		projectsLabel.setBounds(20, 80, 150, 25);
		panel.add(projectsLabel);

		JLabel projectsLabel2 = new JLabel(String.valueOf(Analytics.getProjectCount(userId)));
		projectsLabel2.setBounds(150, 80, 150, 25);
		panel.add(projectsLabel2);

		JLabel taskLabel = new JLabel("Aktuelle Aufgaben:");
		taskLabel.setBounds(20, 100, 150, 25);
		panel.add(taskLabel);

		JLabel taskLabel2 = new JLabel(String.valueOf(Analytics.getTaskCount(userId)));
		taskLabel2.setBounds(150, 100, 150, 25);
		panel.add(taskLabel2);

		return panel;
	}

	private JPanel projectOverwievPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		// Spalten für die JTable erstellen und Daten hinzufügen
		String[] columnNames = { "Projekt", "Anzahl Aufgaben", "Projektleiter" };
		DefaultTableModel model = new DefaultTableModel(Analytics.getProjectNamesAndTaskCount(userId), columnNames);
		JTable table = new JTable(model);

		// ScrollPane für die Tabelle hinzufügen
		JScrollPane scrollPane = new JScrollPane(table);
		panel.add(scrollPane, BorderLayout.CENTER);

		return panel;
	}

	private JPanel tasksOverviewPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		String[] columnNames = { "Titel", "Beschreibung", "Projektleiter" };
		DefaultTableModel model = new DefaultTableModel(Analytics.getTaskInfo(userId), columnNames);
		JTable table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);
		panel.add(scrollPane, BorderLayout.CENTER);

		return panel;
	}

	private JPanel projectLeadPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		JTabbedPane tabbedPane = new JTabbedPane();
		panel.add(tabbedPane);

		Object[][] data = Analytics.getProjectNamesAndTaskCount(userId);

		for (int i = 0; i < data.length; i++) {
			String projectName = (String) data[i][0];
			JPanel projectPanel = projectTaskPanel();
			tabbedPane.addTab(projectName, projectPanel);
			
		}

		/*
		 * - extra tabbed pane in der user gui, nur sichtbar wenn userid mit einer
		 * projectleadid übereinstimmt - zeigt nur eigene projekte an - kann alle
		 * aufgaben seiner projekte sehen - kann aufgaben für das projekt erstellen und
		 * löschen - kann benutzer den aufgaben hinzufügen und entfernen - übersicht der
		 * arbeitszeiten - tabbed pane innerhalb des tabbed pane?? falls projectlead für
		 * mehrere projekte
		 */

		return panel;

	}

	private JPanel projectTaskPanel() {
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		String[] columnNames = { "Projekt", "Aufgabe", "Mitarbeiter"};
		DefaultTableModel model = new DefaultTableModel(Analytics.getProjectTasks(userId), columnNames);
		JTable table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);
		panel.add(scrollPane, BorderLayout.CENTER);
		
		return panel;
	}

}
