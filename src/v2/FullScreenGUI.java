package v2;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

@SuppressWarnings("serial")
public class FullScreenGUI extends JFrame {

//	private DatabaseConnection dbConnect;
	private UserManager uMan;
	private ProjectManager pMan;
	private TaskManager tMan;
	private WorkManager wMan;
	private LogManager log;
	private JTextField nameField;
	private JTextField vornameField;
	private JTextField userIdField;
	private JTextField emailField;
	private JTable userTable;
	private JTable taskTable;
	private JTable projectTable;
	private JTextArea userDisplayArea;
	private JTextArea projectDisplayArea;
	private JTextArea taskDisplayArea;
	private ArrayList<User> users;
	private ArrayList<Task> tasks;
	private ArrayList<Project> projects;

	public FullScreenGUI(DatabaseConnection dbConnect, LogManager log) {
		setTitle("Projekt- & Aufgabenmanagement Tool"); // Fenstertitel
		setDefaultCloseOperation(EXIT_ON_CLOSE); // Standart schließen bei x
		setMinimumSize(new Dimension(1000, 627)); // Minimale größe des Fensters, danach nicht mehr verkleinerbar
		setLayout(new BorderLayout());
		setLocationRelativeTo(null); // Fenster in der Mitte des Bildschirms
		setExtendedState(JFrame.MAXIMIZED_BOTH); // Fenster maximiert öffnen
		this.log = log;

		// Manager-Klassen werden nur erstellt, wenn eine Datenbankverbindung vorhanden
		// ist
		// danach TabbedPane mit den jeweiligen Panels da die Panels zugriff auf Manager
		// brauchen
		// Manager brauchen DB-Connection
		if (dbConnect.getConnection() != null) {
			uMan = new UserManager(dbConnect, log);
			pMan = new ProjectManager(dbConnect, log);
			tMan = new TaskManager(dbConnect, log);
			wMan = new WorkManager(dbConnect, log, uMan, pMan, tMan);

			log.log("Programm wird gestartet...\n", Log.LogType.INFO);

			users = uMan.getUsers();
			log.log("Benutzer geladen.", Log.LogType.SUCCESS);

			projects = pMan.getProjects();
			log.log("Projekte geladen.", Log.LogType.SUCCESS);

			tasks = tMan.getTasks();
			log.log("Aufgaben geladen.\n", Log.LogType.SUCCESS);

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

			JPanel userPanel = getPanelBL("userPanel");
			tabPane.addTab("Benutzerübersicht", userPanel);

			JPanel projectPanel = getPanelBL("projectPanel");
			tabPane.addTab("Projektübersicht", projectPanel);

			JPanel taskPanel = getPanelBL("taskPanel");
			tabPane.addTab("Aufgabenübersicht", taskPanel);

			add(tabPane, BorderLayout.CENTER);

			log.log("Willkommen! '-HIER KOMMT NOCH DER RICHTIGE NUTZER-'\n", Log.LogType.INFO);

			// Wenn soweit dass ich User übergebe
//		logManager.log("Anmeldung von: " + currentUser.getName() + ", " currentUser.getVorname + "\n", Log.LogType.INFO);

		} else {
			log.log("Keine Verbindung zur Datenbank verfügbar", Log.LogType.ERROR);
			log.saveLogs();
		}
	}

	// --------------------------------------------------
	// --- ÜbersichtsPanels erstellen für TabbedPane ---
	// --------------------------------------------------

	// Hauptpanel mit SplitPane
	private JPanel getPanelBL(String panelName) {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JSplitPane splitPane = getSplitPane(panelName);
		panel.add(splitPane, BorderLayout.CENTER);
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

	// SplitPane für die Hauptpanels erstellen
	private JSplitPane getSplitPane(String panelName) {
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, getTablePanel(panelName),
				getRightPanel(panelName));
		splitPane.setResizeWeight(1);
		splitPane.setDividerSize(3);
		splitPane.setEnabled(false);
		return splitPane;
	}

	// Je nach Panel wird anderes TableModel geladen und gibt Panel zurück
	private JScrollPane getTablePanel(String panelName) {
		JScrollPane scrollPane;

		switch (panelName) {
		case "userPanel":
			// Neues TableModel mit Usern erstellen
			UserTableModel userTableModel = new UserTableModel(users); // holt die UserData
			userTable = new JTable(userTableModel); // Neue Tabelle mit dem TableModel
			// 7 Spalten : "User-ID", "Name", "Vorname", "E-Mail", "Passwort", "Rolle",
			// "Projektleiter"
			int[] userColumnWidhts = { 20, 150, 500, 50, 30, 70, 10 }; // breiten der einzelnen Spalten einstellen
			setColumnWidths(userTable, userColumnWidhts); // voreingestellte Breiten übergeben und Spaltenbreiten ändern
			scrollPane = new JScrollPane(userTable); // ScrollPane, wenn die Tabellen länger werden
			return scrollPane;
		case "projectPanel":
			ProjectTableModel projectModel = new ProjectTableModel(projects);
			projectTable = new JTable(projectModel);
			// 6 Spalten "Project-ID", "Titel", "Beschreibung", "Abgabe", "Fertig",
			// "Projektleiter"
			int[] projectColumnWidhts = { 20, 150, 510, 50, 30, 70 }; // breiten der einzelnen Spalten einstellen
			setColumnWidths(projectTable, projectColumnWidhts); // voreingestellte Breiten übergeben und Spaltenbreiten
																// ändern
			scrollPane = new JScrollPane(projectTable);
			return scrollPane;
		case "taskPanel":
			TaskTableModel taskModel = new TaskTableModel(tasks);
			taskTable = new JTable(taskModel);
			// 7 Spalten TaskID, Titel, Beschreibung, ProjektID, Abgabe, Priorität, Fertig
			int[] taskColumnWidhts = { 20, 150, 500, 50, 30, 70, 10 }; // breiten der einzelnen Spalten einstellen
			setColumnWidths(taskTable, taskColumnWidhts); // voreingestellte Breiten übergeben und Spaltenbreiten ändern
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
		switch (panelName) {
		case "userPanel":
			newPanel = getUserActionsPanel(); // setzt das zu übergebene Panel auf userPanel
			break;
		case "projectPanel":
			newPanel = getProjectActionsPanel();
			break;
		case "taskPanel":
			newPanel = getTaskActionsPanel();
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
		JTextArea displayArea = new JTextArea();
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

	// ----------------------------------------------------------
	// --- Userübersicht --- Panel für die rechte obere Seite ---
	// ----------------------------------------------------------
	// ---------------- Buttons und Eingabefelder ---------------
	// ----------------------------------------------------------
	private JPanel getUserActionsPanel() {
		JPanel actionsPanel = new JPanel();
		actionsPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		// Allgemeine Einstellungen für die Buttons
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5); // Abstand zwischen den Componenten
		gbc.weightx = 1.0; // Breite soll sich ausdehnen
		gbc.weighty = 0; // Button nimmt keinen zusätzlichen vertikalen Platz ein

		// -----------------------------------
		// --- Komponenten für Zeile y = 0 ---
		// -----------------------------------

		// Refresh TableModel Button
		JButton refresh = new JButton("aktualisieren");
		refresh.addActionListener(e -> {

		});
		gbc.gridx = 0;
		gbc.gridy = 0;
		actionsPanel.add(refresh, gbc);

		// Benutzer anzeigen Button
		// -> ActionListener
		// -> in Tabelle muss ein User ausgewählt sein
		// -> öffnet neues Fenster mit detaillierter Userübersicht
		JButton showUser = new JButton("Benutzer anzeigen");
		showUser.addActionListener(e -> {
			int selectedRow = userTable.getSelectedRow();
			if (selectedRow != -1) {
				String userId = (String) userTable.getValueAt(selectedRow, 0);
				User selectedUser = null; // Zu Beginn kein User ausgewählt

				// Suche den passenden User in der Liste
				for (User user : users) {
					if (userId.equals(user.getUserId())) { // Vergleiche Strings mit equals
						selectedUser = user;
						break; // Sobald der User gefunden wurde, beende die Schleife
					}
				}

				if (selectedUser != null) {
					log.log("UserOverviewFrame", Log.LogType.OPEN);
					new UserOverviewFrame(selectedUser, log).setVisible(true);
				} else {
					log.log("User nicht gefunden", Log.LogType.ERROR);
					JOptionPane.showMessageDialog(null, "Der ausgewählte Benutzer konnte nicht gefunden werden.",
							"Fehler", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				log.log("Es wurde kein User zum Anzeigen ausgewählt", Log.LogType.ERROR);
				JOptionPane.showMessageDialog(null, "Bitte vorher einen Benutzer aus der Liste auswählen", "Fehler",
						JOptionPane.ERROR_MESSAGE);
			}
		});

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		actionsPanel.add(showUser, gbc);

		// Benutzer erstellen Button
		JButton addUser = new JButton("Benutzer erstellen");
		addUser.addActionListener(e -> {
			// TODO:
		});
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		actionsPanel.add(addUser, gbc);

		// -----------------------------------
		// --- Komponenten für Zeile y = 1 ---
		// -----------------------------------

		// Benutzer ändern Button
		// -> ActionListener
		// -> in Tabelle muss ein User ausgewählt sein
		JButton editUser = new JButton("Benutzer ändern");
		editUser.addActionListener(e -> {
			int selectedRow = userTable.getSelectedRow();
			if (selectedRow != -1) {
				String userId = (String) userTable.getValueAt(selectedRow, 0);
				log.log("EditUserFrame", Log.LogType.OPEN);
				while (true) {
					break;
				}
				User user = new User();
				new EditUserFrame(user, log).setVisible(true);
			} else {
				log.log("Es wurde kein User zum bearbeiten ausgewählt", Log.LogType.ERROR);
				JOptionPane.showMessageDialog(null, "Bitte vorher einen Benutzer aus der Liste wählen", "Fehler",
						JOptionPane.ERROR_MESSAGE);
			}

		});
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		actionsPanel.add(editUser, gbc);

		// Benutzer löschen Button
		// -> ActionListener
		// -> in Tabelle muss ein User ausgewählt sein
		JButton deleteUser = new JButton("Benutzer löschen");
		deleteUser.addActionListener(e -> {
			int selectedRow = userTable.getSelectedRow();
			if (selectedRow != -1) {
				String userId = (String) userTable.getValueAt(selectedRow, 0);
				log.log("Löschvorgang wird gestartet...", Log.LogType.INFO);
				uMan.removeUser(uMan.getUserById(userId));
				refreshTable("user");
			} else if (selectedRow == -1) {
				log.log("Es wurde kein Benutzer zm löschen ausgewählt", Log.LogType.ERROR);
			} else {
				log.log("Benutzer konnte nicht gelöscht werden", Log.LogType.ERROR);
				JOptionPane.showMessageDialog(null, "Benutzer konnte nicht gelöscht werden", "Fehler",
						JOptionPane.ERROR_MESSAGE);
			}
		});
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		actionsPanel.add(deleteUser, gbc);

		// ------------------------------------
		// --- Komponenten für Zeile: y = 2 ---
		// ------------------------------------

		JLabel nameFieldLabel = new JLabel("Name");
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		actionsPanel.add(nameFieldLabel, gbc);

		// Field für Name
		nameField = new JTextField();
		gbc.gridx = 1; // Start Spalte
		gbc.gridy = 2; // Start Zeile
		gbc.gridwidth = 2;
		actionsPanel.add(nameField, gbc);

		// ------------------------------------
		// --- Komponenten für Zeile: y = 3 ---
		// ------------------------------------

		JLabel vornameFieldLabel = new JLabel("Vorname");
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		actionsPanel.add(vornameFieldLabel, gbc);

		// Field für Vorname
		vornameField = new JTextField();
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		actionsPanel.add(vornameField, gbc);

		// ------------------------------------
		// --- Komponenten für Zeile: y = 4 ---
		// ------------------------------------

		JLabel userIdLabel = new JLabel("User-ID");
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		actionsPanel.add(userIdLabel, gbc);

		// UserID Field -> generateUserIDButton erzeugt userID
		userIdField = new JTextField();
		userIdField.setEditable(false); // verhindert selbstständiges schreiben in der textbox
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.gridwidth = 2;
		actionsPanel.add(userIdField, gbc);

		// Create User-ID Button
		JButton userIdButton = new JButton("User-ID generieren");
		userIdButton.addActionListener(e -> {
			if (nameField.getText().isEmpty() || vornameField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Name und Vorname düfen nicht leer sein.", "Fehler",
						JOptionPane.ERROR_MESSAGE);
			} else {
				userIdField.setText(generateUserId());
			}
		});
		gbc.gridx = 1;
		gbc.gridy = 6;
		gbc.gridwidth = 1;
		actionsPanel.add(userIdButton, gbc);

		// ------------------------------------
		// --- Komponenten für Zeile: y = 5 ---
		// ------------------------------------

		JLabel emailLabel = new JLabel("E-Mail");
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		actionsPanel.add(emailLabel, gbc);

		// E-Mail Textfeld -> generateEmailButton erzeugt email
		emailField = new JTextField();
		emailField.setEditable(false);
		gbc.gridx = 1;
		gbc.gridy = 5;
		gbc.gridwidth = 2;
		actionsPanel.add(emailField, gbc);

		// Create E-Mail Button
		JButton emailButton = new JButton("E-Mail generieren");
		emailButton.addActionListener(e -> {
			if (nameField.getText().isEmpty() || vornameField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Name und Vorname düfen nicht leer sein.", "Fehler",
						JOptionPane.ERROR_MESSAGE);
			} else {
				emailField.setText(generateEmail());
			}
		});
		gbc.gridx = 2;
		gbc.gridy = 6;
		gbc.gridwidth = 1;
		actionsPanel.add(emailButton, gbc);

		// Eingabefelder leeren
		JButton clearFields = new JButton("Felder löschen");
		clearFields.addActionListener(e -> {
			clearUserFields();
		});
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		actionsPanel.add(clearFields, gbc);

		//
		JButton showLogs = new JButton("test log");
		showLogs.addActionListener(e -> {
			log.log("Knopf wurde gedrückt.", Log.LogType.INFO);

		});
		gbc.gridx = 2;
		gbc.gridy = 15;
		gbc.gridwidth = 1;
		actionsPanel.add(showLogs, gbc);

		JButton saveLogs = new JButton("Log Speichern");
		saveLogs.addActionListener(e -> {
			log.saveLogs();
		});
		gbc.gridx = 1;
		gbc.gridy = 15;
		gbc.gridwidth = 1;
		actionsPanel.add(saveLogs, gbc);

		// Leerraum-Panel, das den restlichen Platz ausfüllt
		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.gridwidth = 4; // Es soll die gesamte Breite einnehmen
		gbc.weighty = 1.0; // Füllt den restlichen vertikalen Platz aus
		gbc.fill = GridBagConstraints.BOTH; // Das Panel soll sich ausdehnen
		JPanel emptySpace = new JPanel(); // Leeres Panel
		actionsPanel.add(emptySpace, gbc);

		return actionsPanel;
	}

	// -------------------------------------------------------------
	// --- Projektübersicht --- Panel für die rechte obere Seite ---
	// -------------------------------------------------------------
	// ---------------- Buttons und Eingabefelder ------------------
	// -------------------------------------------------------------
	private JPanel getProjectActionsPanel() {
		JPanel actionsPanel = new JPanel();
		actionsPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		// Allgemeine Einstellungen für die Buttons
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5); // Abstand zwischen den Componenten
		gbc.weightx = 1.0; // Breite soll sich ausdehnen
		gbc.weighty = 0; // Button nimmt keinen zusätzlichen vertikalen Platz ein

		// -----------------------------------
		// --- Komponenten für Zeile y = 0 ---
		// -----------------------------------

		// Refresh TableModel Button
		JButton refresh = new JButton("aktualisieren");
		refresh.addActionListener(e -> {

		});
		gbc.gridx = 0;
		gbc.gridy = 0;
		actionsPanel.add(refresh, gbc);

		// Benutzer anzeigen Button
		// -> ActionListener
		// -> in Tabelle muss ein User ausgewählt sein
		// -> öffnet neues Fenster mit detaillierter Userübersicht
		JButton showUser = new JButton("Benutzer anzeigen");
		showUser.addActionListener(e -> {
			int selectedRow = userTable.getSelectedRow();
			if (selectedRow != -1) {
				String userId = (String) userTable.getValueAt(selectedRow, 0);
				User selectedUser = null; // Zu Beginn kein User ausgewählt

				// Suche den passenden User in der Liste
				for (User user : users) {
					if (userId.equals(user.getUserId())) { // Vergleiche Strings mit equals
						selectedUser = user;
						break; // Sobald der User gefunden wurde, beende die Schleife
					}
				}

				if (selectedUser != null) {
					log.log("UserOverviewFrame", Log.LogType.OPEN);
					new UserOverviewFrame(selectedUser, log).setVisible(true);
				} else {
					log.log("User nicht gefunden", Log.LogType.ERROR);
					JOptionPane.showMessageDialog(null, "Der ausgewählte Benutzer konnte nicht gefunden werden.",
							"Fehler", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				log.log("Es wurde kein User zum Anzeigen ausgewählt", Log.LogType.ERROR);
				JOptionPane.showMessageDialog(null, "Bitte vorher einen Benutzer aus der Liste auswählen", "Fehler",
						JOptionPane.ERROR_MESSAGE);
			}
		});

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		actionsPanel.add(showUser, gbc);

		// Benutzer erstellen Button
		JButton addUser = new JButton("Projekt erstellen");
		addUser.addActionListener(e -> {
			
		});
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		actionsPanel.add(addUser, gbc);

		// -----------------------------------
		// --- Komponenten für Zeile y = 1 ---
		// -----------------------------------

		// Benutzer ändern Button
		// -> ActionListener
		// -> in Tabelle muss ein User ausgewählt sein
		JButton editUser = new JButton("Benutzer ändern");
		editUser.addActionListener(e -> {
			int selectedRow = userTable.getSelectedRow();
			if (selectedRow != -1) {
				String userId = (String) userTable.getValueAt(selectedRow, 0);
				log.log("EditUserFrame", Log.LogType.OPEN);
				while (true) {
					break;
				}
				User user = new User();
				new EditUserFrame(user, log).setVisible(true);
			} else {
				log.log("Es wurde kein User zum bearbeiten ausgewählt", Log.LogType.ERROR);
				JOptionPane.showMessageDialog(null, "Bitte vorher einen Benutzer aus der Liste wählen", "Fehler",
						JOptionPane.ERROR_MESSAGE);
			}

		});
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		actionsPanel.add(editUser, gbc);

		// Benutzer löschen Button
		// -> ActionListener
		// -> in Tabelle muss ein User ausgewählt sein
		JButton deleteUser = new JButton("Benutzer löschen");
		deleteUser.addActionListener(e -> {

		});
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		actionsPanel.add(deleteUser, gbc);

		// ------------------------------------
		// --- Komponenten für Zeile: y = 2 ---
		// ------------------------------------

		JLabel nameFieldLabel = new JLabel("Name");
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		actionsPanel.add(nameFieldLabel, gbc);

		// Field für Name
		nameField = new JTextField();
		gbc.gridx = 1; // Start Spalte
		gbc.gridy = 2; // Start Zeile
		gbc.gridwidth = 2;
		actionsPanel.add(nameField, gbc);

		// ------------------------------------
		// --- Komponenten für Zeile: y = 3 ---
		// ------------------------------------

		JLabel vornameFieldLabel = new JLabel("Vorname");
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		actionsPanel.add(vornameFieldLabel, gbc);

		// Field für Vorname
		vornameField = new JTextField();
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		actionsPanel.add(vornameField, gbc);

		// ------------------------------------
		// --- Komponenten für Zeile: y = 4 ---
		// ------------------------------------

		JLabel userIdLabel = new JLabel("User-ID");
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		actionsPanel.add(userIdLabel, gbc);

		// UserID Field -> generateUserIDButton erzeugt userID
		userIdField = new JTextField();
		userIdField.setEditable(false); // verhindert selbstständiges schreiben in der textbox
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.gridwidth = 2;
		actionsPanel.add(userIdField, gbc);

		// Create User-ID Button
		JButton userIdButton = new JButton("User-ID generieren");
		userIdButton.addActionListener(e -> {
			if (nameField.getText().isEmpty() || vornameField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Name und Vorname düfen nicht leer sein.", "Fehler",
						JOptionPane.ERROR_MESSAGE);
			} else {
				userIdField.setText(generateUserId());
			}
		});
		gbc.gridx = 1;
		gbc.gridy = 6;
		gbc.gridwidth = 1;
		actionsPanel.add(userIdButton, gbc);

		// ------------------------------------
		// --- Komponenten für Zeile: y = 5 ---
		// ------------------------------------

		JLabel emailLabel = new JLabel("E-Mail");
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		actionsPanel.add(emailLabel, gbc);

		// E-Mail Textfeld -> generateEmailButton erzeugt email
		emailField = new JTextField();
		emailField.setEditable(false);
		gbc.gridx = 1;
		gbc.gridy = 5;
		gbc.gridwidth = 2;
		actionsPanel.add(emailField, gbc);

		// Create E-Mail Button
		JButton emailButton = new JButton("E-Mail generieren");
		emailButton.addActionListener(e -> {
			if (nameField.getText().isEmpty() || vornameField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Name und Vorname düfen nicht leer sein.", "Fehler",
						JOptionPane.ERROR_MESSAGE);
			} else {
				emailField.setText(generateEmail());
			}
		});
		gbc.gridx = 2;
		gbc.gridy = 6;
		gbc.gridwidth = 1;
		actionsPanel.add(emailButton, gbc);

		// Eingabefelder leeren
		JButton clearFields = new JButton("Felder löschen");
		clearFields.addActionListener(e -> {
			clearUserFields();
		});
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		actionsPanel.add(clearFields, gbc);

		//
		JButton showLogs = new JButton("test log");
		showLogs.addActionListener(e -> {
			log.log("Knopf wurde gedrückt.", Log.LogType.INFO);

		});
		gbc.gridx = 2;
		gbc.gridy = 15;
		gbc.gridwidth = 1;
		actionsPanel.add(showLogs, gbc);

		JButton saveLogs = new JButton("Log Speichern");
		saveLogs.addActionListener(e -> {
			log.saveLogs();
		});
		gbc.gridx = 1;
		gbc.gridy = 15;
		gbc.gridwidth = 1;
		actionsPanel.add(saveLogs, gbc);

		// Leerraum-Panel, das den restlichen Platz ausfüllt
		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.gridwidth = 4; // Es soll die gesamte Breite einnehmen
		gbc.weighty = 1.0; // Füllt den restlichen vertikalen Platz aus
		gbc.fill = GridBagConstraints.BOTH; // Das Panel soll sich ausdehnen
		JPanel emptySpace = new JPanel(); // Leeres Panel
		actionsPanel.add(emptySpace, gbc);

		return actionsPanel;
	}

	// --------------------------------------------------------------
	// --- Aufgabenübersicht --- Panel für die rechte obere Seite ---
	// --------------------------------------------------------------
	// ---------------- Buttons und Eingabefelder -------------------
	// --------------------------------------------------------------
	private JPanel getTaskActionsPanel() {
		JPanel actionsPanel = new JPanel();
		actionsPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		// Allgemeine Einstellungen für die Buttons
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5); // Abstand zwischen den Componenten
		gbc.weightx = 1.0; // Breite soll sich ausdehnen
		gbc.weighty = 0; // Button nimmt keinen zusätzlichen vertikalen Platz ein

		// -----------------------------------
		// --- Komponenten für Zeile y = 0 ---
		// -----------------------------------

		// Refresh TableModel Button
		JButton refresh = new JButton("aktualisieren");
		refresh.addActionListener(e -> {

		});
		gbc.gridx = 0;
		gbc.gridy = 0;
		actionsPanel.add(refresh, gbc);

		// Benutzer anzeigen Button
		// -> ActionListener
		// -> in Tabelle muss ein User ausgewählt sein
		// -> öffnet neues Fenster mit detaillierter Userübersicht
		JButton showUser = new JButton("Benutzer anzeigen");
		showUser.addActionListener(e -> {
			int selectedRow = userTable.getSelectedRow();
			if (selectedRow != -1) {
				String userId = (String) userTable.getValueAt(selectedRow, 0);
				User selectedUser = null; // Zu Beginn kein User ausgewählt

				// Suche den passenden User in der Liste
				for (User user : users) {
					if (userId.equals(user.getUserId())) { // Vergleiche Strings mit equals
						selectedUser = user;
						break; // Sobald der User gefunden wurde, beende die Schleife
					}
				}

				if (selectedUser != null) {
					log.log("UserOverviewFrame", Log.LogType.OPEN);
					new UserOverviewFrame(selectedUser, log).setVisible(true);
				} else {
					log.log("User nicht gefunden", Log.LogType.ERROR);
					JOptionPane.showMessageDialog(null, "Der ausgewählte Benutzer konnte nicht gefunden werden.",
							"Fehler", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				log.log("Es wurde kein User zum Anzeigen ausgewählt", Log.LogType.ERROR);
				JOptionPane.showMessageDialog(null, "Bitte vorher einen Benutzer aus der Liste auswählen", "Fehler",
						JOptionPane.ERROR_MESSAGE);
			}
		});

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		actionsPanel.add(showUser, gbc);

		// Benutzer erstellen Button
		JButton addUser = new JButton("Benutzer erstellen");
		addUser.addActionListener(e -> {
			// TODO:
		});
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		actionsPanel.add(addUser, gbc);

		// -----------------------------------
		// --- Komponenten für Zeile y = 1 ---
		// -----------------------------------

		// Benutzer ändern Button
		// -> ActionListener
		// -> in Tabelle muss ein User ausgewählt sein
		JButton editUser = new JButton("Benutzer ändern");
		editUser.addActionListener(e -> {
			int selectedRow = userTable.getSelectedRow();
			if (selectedRow != -1) {
				String userId = (String) userTable.getValueAt(selectedRow, 0);
				log.log("EditUserFrame", Log.LogType.OPEN);
				while (true) {
					break;
				}
				User user = new User();
				new EditUserFrame(user, log).setVisible(true);
			} else {
				log.log("Es wurde kein User zum bearbeiten ausgewählt", Log.LogType.ERROR);
				JOptionPane.showMessageDialog(null, "Bitte vorher einen Benutzer aus der Liste wählen", "Fehler",
						JOptionPane.ERROR_MESSAGE);
			}

		});
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		actionsPanel.add(editUser, gbc);

		// Benutzer löschen Button
		// -> ActionListener
		// -> in Tabelle muss ein User ausgewählt sein
		JButton deleteUser = new JButton("Benutzer löschen");
		deleteUser.addActionListener(e -> {

		});
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		actionsPanel.add(deleteUser, gbc);

		// ------------------------------------
		// --- Komponenten für Zeile: y = 2 ---
		// ------------------------------------

		JLabel nameFieldLabel = new JLabel("Name");
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		actionsPanel.add(nameFieldLabel, gbc);

		// Field für Name
		nameField = new JTextField();
		gbc.gridx = 1; // Start Spalte
		gbc.gridy = 2; // Start Zeile
		gbc.gridwidth = 2;
		actionsPanel.add(nameField, gbc);

		// ------------------------------------
		// --- Komponenten für Zeile: y = 3 ---
		// ------------------------------------

		JLabel vornameFieldLabel = new JLabel("Vorname");
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		actionsPanel.add(vornameFieldLabel, gbc);

		// Field für Vorname
		vornameField = new JTextField();
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		actionsPanel.add(vornameField, gbc);

		// ------------------------------------
		// --- Komponenten für Zeile: y = 4 ---
		// ------------------------------------

		JLabel userIdLabel = new JLabel("User-ID");
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		actionsPanel.add(userIdLabel, gbc);

		// UserID Field -> generateUserIDButton erzeugt userID
		userIdField = new JTextField();
		userIdField.setEditable(false); // verhindert selbstständiges schreiben in der textbox
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.gridwidth = 2;
		actionsPanel.add(userIdField, gbc);

		// Create User-ID Button
		JButton userIdButton = new JButton("User-ID generieren");
		userIdButton.addActionListener(e -> {
			if (nameField.getText().isEmpty() || vornameField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Name und Vorname düfen nicht leer sein.", "Fehler",
						JOptionPane.ERROR_MESSAGE);
			} else {
				userIdField.setText(generateUserId());
			}
		});
		gbc.gridx = 1;
		gbc.gridy = 6;
		gbc.gridwidth = 1;
		actionsPanel.add(userIdButton, gbc);

		// ------------------------------------
		// --- Komponenten für Zeile: y = 5 ---
		// ------------------------------------

		JLabel emailLabel = new JLabel("E-Mail");
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		actionsPanel.add(emailLabel, gbc);

		// E-Mail Textfeld -> generateEmailButton erzeugt email
		emailField = new JTextField();
		emailField.setEditable(false);
		gbc.gridx = 1;
		gbc.gridy = 5;
		gbc.gridwidth = 2;
		actionsPanel.add(emailField, gbc);

		// Create E-Mail Button
		JButton emailButton = new JButton("E-Mail generieren");
		emailButton.addActionListener(e -> {
			if (nameField.getText().isEmpty() || vornameField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Name und Vorname düfen nicht leer sein.", "Fehler",
						JOptionPane.ERROR_MESSAGE);
			} else {
				emailField.setText(generateEmail());
			}
		});
		gbc.gridx = 2;
		gbc.gridy = 6;
		gbc.gridwidth = 1;
		actionsPanel.add(emailButton, gbc);

		// Eingabefelder leeren
		JButton clearFields = new JButton("Felder löschen");
		clearFields.addActionListener(e -> {
			clearUserFields();
		});
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		actionsPanel.add(clearFields, gbc);

		//
		JButton showLogs = new JButton("test log");
		showLogs.addActionListener(e -> {
			log.log("Knopf wurde gedrückt.", Log.LogType.INFO);

		});
		gbc.gridx = 2;
		gbc.gridy = 15;
		gbc.gridwidth = 1;
		actionsPanel.add(showLogs, gbc);

		JButton saveLogs = new JButton("Log Speichern");
		saveLogs.addActionListener(e -> {
			log.saveLogs();
		});
		gbc.gridx = 1;
		gbc.gridy = 15;
		gbc.gridwidth = 1;
		actionsPanel.add(saveLogs, gbc);

		// Leerraum-Panel, das den restlichen Platz ausfüllt
		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.gridwidth = 4; // Es soll die gesamte Breite einnehmen
		gbc.weighty = 1.0; // Füllt den restlichen vertikalen Platz aus
		gbc.fill = GridBagConstraints.BOTH; // Das Panel soll sich ausdehnen
		JPanel emptySpace = new JPanel(); // Leeres Panel
		actionsPanel.add(emptySpace, gbc);

		return actionsPanel;
	}

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

	private void clearUserFields() {
		nameField.setText("");
		vornameField.setText("");
		userIdField.setText("");
		emailField.setText("");
	}

	private void setColumnWidths(JTable table, int[] columnWidhts) {
		TableColumn column;
		for (int i = 0; i < columnWidhts.length; i++) {
			column = table.getColumnModel().getColumn(i);
			column.setPreferredWidth(columnWidhts[i]);
		}
	}

	public static void setDynamicColumnWidths(JTable table) {
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

	private String generateUserId() {
		return vornameField.getText().substring(0, Math.min(3, vornameField.getText().length())).toLowerCase()
				+ nameField.getText().substring(0, Math.min(3, nameField.getText().length())).toLowerCase();
	}

	private String generateEmail() {
		return vornameField.getText().substring(0, 1).toLowerCase() + nameField.getText().toLowerCase() + "@email.com";
	}

}
