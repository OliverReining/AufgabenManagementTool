package v2;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

@SuppressWarnings("serial")
public class GUIFullScreen extends JFrame {

//	private DatabaseConnection dbConnect;
	private UserManager uMan;
	private ProjectManager pMan;
	private TaskManager tMan;
	private WorkManager wMan;
	private LogManager log;
	private JTable userTable;
	private JTable projectTable;
	private JTable taskTable;
	private JTextArea displayArea;
	private JTextArea userDisplayArea;
	private JTextArea projectDisplayArea;
	private JTextArea taskDisplayArea;
	private ArrayList<User> users;
	private ArrayList<Project> projects;
	private ArrayList<Task> tasks;

	public GUIFullScreen(DatabaseConnection dbConnect, LogManager log) {
		setTitle("Projekt- & Aufgabenmanagement Tool"); // Fenstertitel
		setMinimumSize(new Dimension(1000, 650)); // Minimale größe des Fensters, danach nicht mehr verkleinerbar
		setLayout(new BorderLayout());
		setLocationRelativeTo(null); // Fenster in der Mitte des Bildschirms
		setExtendedState(JFrame.MAXIMIZED_BOTH); // Fenster maximiert öffnen
//		setDefaultCloseOperation(EXIT_ON_CLOSE); // Standart schließen bei x
		this.log = log;

		// WindowListener, sobald Fenster geschlossen wird wird saveLogs ausgeführt
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dbConnect.closeConnection();
				log.saveLogs();
				System.exit(0);
			}
		});

		// Manager-Klassen werden nur erstellt, wenn eine Datenbankverbindung vorhanden
		// ist
		// danach TabbedPane mit den jeweiligen Panels da die Panels zugriff auf Manager
		// brauchen
		// Manager brauchen DB-Connection
		if (dbConnect.getConnection() != null) {
			// neue Managerklassen mit der gleichen Connection um eine Verbindung zu
			// benutzen
			// und LogManager um einen globalen Log zu schreiben
			uMan = new UserManager(dbConnect, log);
			pMan = new ProjectManager(dbConnect, log, uMan);
			tMan = new TaskManager(dbConnect, log);
			// WorkManager mit den anderen ManagerKlassen initialisieren,
			// Muss auf die gleichen Daten zugreifen können
			wMan = new WorkManager(dbConnect, log, uMan, pMan, tMan);

			log.log("Programm wird gestartet...", Log.LogType.INFO, Log.Manager.GUI);

			users = uMan.getUsers();
			log.log("Benutzer geladen.", Log.LogType.SUCCESS, Log.Manager.GUI);

			projects = pMan.getProjects();
			log.log("Projekte geladen.", Log.LogType.SUCCESS, Log.Manager.GUI);

			tasks = tMan.getTasks();
			log.log("Aufgaben geladen.", Log.LogType.SUCCESS, Log.Manager.GUI);

			/*
			 * ungefähres Layout der seite x - Buttons / Eingabefelder (TextArea) Links
			 * Tabelle (ca 2/3), unten rechts DisplayArea für log und messages
			 * 
			 * |__0_|__1_|__2_|__3_|__4_|__5_|__6_|__7_|__8_|__9_|____
			 * |_______________________________________|__x_|__x_| _0_
			 * |_______________________________________|__x_|__x_| _1_
			 * |_______________________________________|__x_|__x_| _2_
			 * |_________________Tabelle_______________|____|____| _3_
			 * |_______________________________________|____|____| _4_
			 * |_______________________________________|____|____| _5_
			 * |_______________________________________|Display- | _6_
			 * |________________tablePanel ____________|____Area_| _7_
			 * 
			 */

			JTabbedPane tabPane = new JTabbedPane(); // neues TabbedPane an die alle Tabs angehängt werden
			tabPane.addChangeListener(e -> {
				int selected = tabPane.getSelectedIndex();
				switch (selected) {
				case 0:
					log.setDisplayArea(userDisplayArea);
					break;
				case 1:
					log.setDisplayArea(projectDisplayArea);
					break;
				case 2:
					log.setDisplayArea(taskDisplayArea);
					break;
				}
			});

			JPanel userPanel = getMainPanel("userPanel");
			tabPane.addTab("Benutzerübersicht", userPanel);

			JPanel projectPanel = getMainPanel("projectPanel");
			tabPane.addTab("Projektübersicht", projectPanel);

			JPanel taskPanel = getMainPanel("taskPanel");
			tabPane.addTab("Aufgabenübersicht", taskPanel);

			add(tabPane, BorderLayout.CENTER);

			log.log("Willkommen! '-HIER KOMMT NOCH DER RICHTIGE NUTZER-'", Log.LogType.INFO, Log.Manager.GUI);

			// Wenn soweit dass ich User übergebe
//		logManager.log("Anmeldung von: " + currentUser.getName() + ", " currentUser.getVorname + "\n", Log.LogType.INFO);

		} else {
			log.log("Keine Verbindung zur Datenbank verfügbar", Log.LogType.ERROR, Log.Manager.GUI);
		}
	}

	// --------------------------------------------------
	// --- ÜbersichtsPanels erstellen für TabbedPane ---
	// --------------------------------------------------

	// Hauptpanel mit SplitPane
	private JPanel getMainPanel(String panelName) {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		// mit SplitPane
//		JSplitPane splitPane = getSplitPane(panelName);
//		panel.add(splitPane, BorderLayout.CENTER);

		// mit BorderLayout
		panel.add(getTablePanel(panelName), BorderLayout.CENTER);
		panel.add(getRightPanel(panelName), BorderLayout.EAST);
		return panel;
	}

	// Hauptpanel mit GBC-Layout
//	private JPanel getUserPanelGBC(String panelName) {
//		JPanel panel = new JPanel();
//		panel.setLayout(new GridBagLayout());
//		panel.add(getTablePanel(panelName), getTablePanelLayout());
//		panel.add(getRightPanel(panelName), getRightPanelLayout());
//		return panel;
//	}

	// UNUSED: machs jetzt übers BorderLayout, damit die rechte Seite immer gleich
	// groß bleibt
//	// SplitPane für die MainPanel erstellen
//	private JSplitPane getSplitPane(String panelName) {
//		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, getTablePanel(panelName),
//				getRightPanel(panelName));
//		splitPane.setResizeWeight(1);
//		splitPane.setDividerSize(3);
//		splitPane.setEnabled(false);
//		return splitPane;
//	}

	// Je nach Panel wird anderes TableModel geladen und gibt Panel zurück
	private JScrollPane getTablePanel(String panelName) {
		JScrollPane scrollPane;

		switch (panelName) {
		case "userPanel":
			// Neues TableModel mit Usern erstellen
			UserTableModel userTableModel = new UserTableModel(users); // holt die UserData
			userTable = new JTable(userTableModel); // Neue Tabelle mit dem TableModel
			// TableRowSorter erstellen
			TableRowSorter<UserTableModel> userSorter = new TableRowSorter<>(userTableModel);
			userTable.setRowSorter(userSorter);

			// 7 Spalten : "User-ID", "Name", "Vorname", "E-Mail", "Passwort", "Rolle",
			// "Projektleiter"
			// Voreingestellte Spaltenbreiten übergeben
//			int[] userColumnWidhts = { 20, 150, 500, 50, 30, 70, 10 }; // breiten der einzelnen Spalten einstellen
//			setColumnWidths(userTable, userColumnWidhts); // voreingestellte Breiten übergeben und Spaltenbreiten ändern

			// Dynamische Spaltenbreiten benutzen
			setDynamicColumnWidths(userTable);

			scrollPane = new JScrollPane(userTable); // ScrollPane, wenn die Tabellen länger werden
			return scrollPane;
		case "projectPanel":
			ProjectTableModel projectTableModel = new ProjectTableModel(projects);
			projectTable = new JTable(projectTableModel);
			setDynamicColumnWidths(projectTable);
			scrollPane = new JScrollPane(projectTable);
			return scrollPane;
		case "taskPanel":
			TaskTableModel taskTableModel = new TaskTableModel(tasks);
			taskTable = new JTable(taskTableModel);
			setDynamicColumnWidths(taskTable);
			scrollPane = new JScrollPane(taskTable);
			return scrollPane;
		default:
			return null;

		}
	}

	// erstellt das rechte Panel basierend auf dem Panel Namen
	// eigenes SplitPane oben Buttons, Labels, TextFields;
	// unten DiplayArea, für eventuellen log, oder anderes
	private JPanel getRightPanel(String panelName) {
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());

		JPanel newPanel = null; // initialisieren eines leeren Panel um ButtonPanels zu übergeben
		// je nach Übergabewert neues JPanel aus Panel-Klasse laden
		switch (panelName) {
		case "userPanel":
			// neue Klasse für das Panel erstellen
			// Panel aus der PanelUserActions laden
			newPanel = new PanelUserAction(users, userTable, uMan, log, wMan); // setz Panel auf das aus der Klasse

//			newPanel = getUserActionsPanel(); // setzt das zu übergebene Panel auf userPanel
			break;
		case "projectPanel":
//			newPanel = new PanelProjectAction(projects, projectTable, pMan, log);
			newPanel = new PanelUserAction(users, userTable, uMan, log, wMan);
			break;
		case "taskPanel":
			newPanel = new PanelTaskAction(tasks, taskTable, tMan, log);
			break;
		}
		rightPanel.add(newPanel, BorderLayout.NORTH);

		// je nach Case wird andere Funktion zum erstellen des ButtonPanels aufgerufen
		// DisplayPanel ändert sich nicht, TablePanel wird in späterer Methode
		// hinzugefügt

		JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, newPanel, getDisplayPanel(panelName));

		// 75% für Buttons, 25% für DisplayArea (eigentlich)
		// nach bissl probieren ist .50 gut für die Größe des DisplayPanel
		rightSplitPane.setResizeWeight(0.50);
		rightSplitPane.setEnabled(true); // SplitPane verschiebbar, um DisplayArea bei Bedarf nach oben zu vergößern
		rightPanel.add(rightSplitPane, BorderLayout.CENTER);

		return rightPanel;

	}

	// DisplayAreaPanel, unten Rechts vom Bildschirm
	private JPanel getDisplayPanel(String panelName) {
		JPanel displayPanel = new JPanel();
		displayPanel.setLayout(new BorderLayout());
		displayArea = new JTextArea();
		displayArea.setEditable(false);
//		displayArea.setMaximumSize(new Dimension(400, 800));
//		displayArea.setMaximumSize(null);
		JScrollPane scrollPane = new JScrollPane(displayArea);
		displayPanel.add(scrollPane, BorderLayout.CENTER);

		switch (panelName) {
		case "userPanel":
			userDisplayArea = displayArea;
			break;
		case "projectPanel":
			projectDisplayArea = displayArea;
			break;
		case "taskPanel":
			taskDisplayArea = displayArea;
			break;
		}

		return displayPanel;
	}

	// --------------
	// --- UNUSED ---
	// --------------

//	// GBC-Standart-Layout für die Tabelle (linke Seite von jedem Panel)
//	private GridBagConstraints getTablePanelLayout() {
//		GridBagConstraints gbc = new GridBagConstraints();
//		// Tabelle (linker Bereich)
//		gbc.gridx = 0; // Start Spalte
//		gbc.gridy = 0; // Start Zeile
//		gbc.gridwidth = 80; // Spalten breit
//		gbc.gridheight = 40; // Zeilen hoch
//		gbc.weightx = 0.80; // % der Breite für die Tabelle
//		gbc.weighty = 1.0; // % der Höhe
//		gbc.fill = GridBagConstraints.BOTH;
//		return gbc;
//	}
//
//	// GBC-Standart-Layout für die rechte Seite des Bildschirms
//	// gehört zu GBC vom TablePanelLayout, muss im gleichen Panel angewendet werden
//	// -> um Funktionalität zu gewährleisten
//	private GridBagConstraints getRightPanelLayout() {
//		GridBagConstraints gbc = new GridBagConstraints();
//		// Rechter Bereich
//		gbc.gridx = 80; // Start in der Spalte
//		gbc.gridy = 0; // Start Zeile
//		gbc.gridwidth = 20; // Spalten breit
//		gbc.gridheight = 25; // Zeilen hoch
//		gbc.weightx = 0.20; // % der Breite für den rechten Bereich
//		gbc.weighty = 0.60; // % der Höhe
//		gbc.fill = GridBagConstraints.BOTH;
//		return gbc;
//	}

	// Tabelle aktualisierren
	private void refreshTable(String choice) {
		switch (choice) {
		case "user":
			UserTableModel userModel = new UserTableModel(uMan.getUsers());
			userTable.setModel(userModel);
			break;
		case "task":
			TaskTableModel taskModel = new TaskTableModel(tMan.getTasks());
			taskTable.setModel(taskModel);
			break;
		case "project":
			ProjectTableModel projectModel = new ProjectTableModel(pMan.getProjects());
			projectTable.setModel(projectModel);
			break;
		default:
			return;
		}
	}

	// Spaltenbreiten mit voreingestellten Werten setzen
//	private void setColumnWidths(JTable table, int[] columnWidhts) {
//		TableColumn column;
//		for (int i = 0; i < columnWidhts.length; i++) {
//			column = table.getColumnModel().getColumn(i);
//			column.setPreferredWidth(columnWidhts[i]);
//		}
//	}

	// Spaltenbreiten dynamisch auf der Länge des Inhaltes setzen
	private void setDynamicColumnWidths(JTable table) {
		for (int coumn = 0; coumn < table.getColumnCount(); coumn++) {
			TableColumn column = table.getColumnModel().getColumn(coumn);
			int maxWidth = 50; // Mindestbreite

			// Durch die Zeilen iterieren und die maximale Breite ermitteln
			for (int row = 0; row < table.getRowCount(); row++) {
				TableCellRenderer renderer = table.getCellRenderer(row, coumn);
				Component comp = table.prepareRenderer(renderer, row, coumn);
				maxWidth = Math.max(comp.getPreferredSize().width + 10, maxWidth); // +10 für Puffer
			}

			// Breite für die Spalte festlegen
			column.setPreferredWidth(maxWidth);
		}
	}

}
