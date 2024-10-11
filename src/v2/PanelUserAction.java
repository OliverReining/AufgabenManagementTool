package v2;

import java.awt.*;
import java.util.*;
import javax.swing.*;

public class PanelUserAction extends JPanel {

	private UserManager uMan;
	private WorkManager wMan;
	private ArrayList<User> users;
	private JTable userTable;
	private JTextField userIdField;
	private JTextField nameField;
	private JTextField vornameField;
	private JTextField emailField;
	private JTextField telField;
	private JTextField roleField;
	private JTextField passField;
	private boolean projectLead = false;
	private JTextField hourlyRateField;

	public PanelUserAction(ArrayList<User> users, JTable userTable, UserManager uMan, LogManager log,
			WorkManager wMan) {
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		Log.Manager manager = Log.Manager.USER_MANAGER;
		this.uMan = uMan;
		this.wMan = wMan;
		this.users = users;
		this.userTable = userTable;

		// Allgemeine Einstellungen für die Buttons
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5, 5, 5, 5); // Abstand zwischen den Componenten
		gbc.weightx = 1.0; // Breite soll sich ausdehnen
		gbc.weighty = 0; // Button nimmt keinen zusätzlichen vertikalen Platz ein

		// -----------------------------------
		// --- Komponenten für Zeile y = 0 ---
		// -----------------------------------

		// Refresh TableModel Button
		JButton refresh = new JButton("aktualisieren");
		refresh.addActionListener(e -> {
			refreshAll();
		});
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(refresh, gbc);

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
					log.log("UserOverviewFrame", Log.LogType.OPEN, Log.Manager.GUI);
					new FrameUserOverview(selectedUser, log, uMan).setVisible(true);
				} else {
					log.log("User nicht gefunden", Log.LogType.ERROR, Log.Manager.GUI);
					JOptionPane.showMessageDialog(null, "Der ausgewählte Benutzer konnte nicht gefunden werden.",
							"Fehler", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				log.log("Es wurde kein User zum Anzeigen ausgewählt", Log.LogType.ERROR, Log.Manager.GUI);
				JOptionPane.showMessageDialog(null, "Bitte vorher einen Benutzer aus der Liste auswählen", "Fehler",
						JOptionPane.ERROR_MESSAGE);
			}
		});

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		add(showUser, gbc);

		// Benutzer erstellen Button
		JButton button = new JButton("button");
		button.addActionListener(e -> {
			// TODO Button für was auch immer
		});
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		add(button, gbc);

		// -----------------------------------
		// --- Komponenten für Zeile y = 1 ---
		// -----------------------------------
		// ----- ??????? - UPDATE - REMOVE ---
		// -----------------------------------

		// Benutzer ändern Button
		// Öffnet neues Fenster mit den Userdaten
		// -> in Tabelle muss ein User ausgewählt sein
		// im neuen Fenster werden direkt alle Daten angezeigt und können geändert
		// werden
		// addUser() und insert in DB
		JButton editUser = new JButton("Benutzer ändern");
		editUser.addActionListener(e -> {
			int selectedRow = userTable.getSelectedRow();
			if (selectedRow != -1) {
				log.log("new EditUserFrame(uMan.getUserById((String) userTable.getValueAt(selectedRow, 0)), uMan, log).setVisible(true)",
						Log.LogType.OPEN, Log.Manager.GUI);
				new FrameUserEdit(uMan.getUserById((String) userTable.getValueAt(selectedRow, 0)), uMan, log)
						.setVisible(true);
			} else {
				log.log("Es wurde kein User zum bearbeiten ausgewählt", Log.LogType.ERROR, Log.Manager.GUI);
				JOptionPane.showMessageDialog(null, "Bitte vorher einen Benutzer aus der Liste wählen", "Fehler",
						JOptionPane.ERROR_MESSAGE);
			}

		});
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		add(editUser, gbc);

		// Benutzer löschen Button
		// -> ActionListener
		// -> in Tabelle muss ein User ausgewählt sein
		JButton deleteUser = new JButton("Benutzer löschen");
		deleteUser.addActionListener(e -> {
			int selectedRow = userTable.getSelectedRow();
			if (selectedRow != -1) {
				String userId = (String) userTable.getValueAt(selectedRow, 0);
				log.log("Löschvorgang wird gestartet...", Log.LogType.INFO, Log.Manager.GUI);
				uMan.removeUser(uMan.getUserById(userId));
				UserTableModel userModel = new UserTableModel(uMan.getUsers());
				userTable.setModel(userModel);
			} else if (selectedRow == -1) {
				log.log("Es wurde kein Benutzer zm löschen ausgewählt", Log.LogType.ERROR, Log.Manager.GUI);
			} else {
				log.log("Benutzer konnte nicht gelöscht werden", Log.LogType.ERROR, Log.Manager.GUI);
				JOptionPane.showMessageDialog(null, "Benutzer konnte nicht gelöscht werden", "Fehler",
						JOptionPane.ERROR_MESSAGE);
			}
		});
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		add(deleteUser, gbc);

		// ---------------------------------------------
		// --- Komponenten für Zeile: y = 2 --- NAME ---
		// ---------------------------------------------

		JLabel nameFieldLabel = new JLabel("Name");
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		add(nameFieldLabel, gbc);

		// Field für Name
		nameField = new JTextField();
		gbc.gridx = 1; // Start Spalte
		gbc.gridy = 2; // Start Zeile
		gbc.gridwidth = 2;
		add(nameField, gbc);

		// ------------------------------------------------
		// --- Komponenten für Zeile: y = 3 --- VORNAME ---
		// ------------------------------------------------

		JLabel vornameFieldLabel = new JLabel("Vorname");
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		add(vornameFieldLabel, gbc);

		// Field für Vorname
		vornameField = new JTextField();
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		add(vornameField, gbc);

		// ------------------------------------------------
		// --- Komponenten für Zeile: y = 4 --- USER-ID ---
		// ------------------------------------------------

		JLabel userIdLabel = new JLabel("User-ID");
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		add(userIdLabel, gbc);

		// UserID Field -> generateUserIDButton erzeugt userID
		userIdField = new JTextField();
		userIdField.setEditable(false); // verhindert selbstständiges schreiben in der textbox
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.weightx = 0.7;
		add(userIdField, gbc);

		// Create User-ID Button
		JButton userIdButton = new JButton("User-ID generieren");
		userIdButton.addActionListener(e -> {
			if (nameField.getText().isEmpty() || vornameField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Name und Vorname düfen nicht leer sein.", "Fehler",
						JOptionPane.ERROR_MESSAGE);
			} else {
				userIdField.setText(generateUserId());
				log.successLog("User-ID generiert: " + userIdField.getText(), manager);
			}
		});
		gbc.gridx = 2;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.weightx = 0.3;
		add(userIdButton, gbc);

		// -----------------------------------------------
		// --- Komponenten für Zeile: y = 5 --- E-MAIL ---
		// -----------------------------------------------

		// E-Mail-Label
		JLabel emailLabel = new JLabel("E-Mail");
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		add(emailLabel, gbc);

		// E-Mail Textfeld -> generateEmailButton erzeugt email
		emailField = new JTextField();
		emailField.setEditable(false);
		gbc.gridx = 1;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		gbc.weightx = 0.7;
		add(emailField, gbc);

		// Create E-Mail Button
		JButton emailButton = new JButton("E-Mail generieren");
		emailButton.addActionListener(e -> {
			if (nameField.getText().isEmpty() || vornameField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Name und Vorname düfen nicht leer sein.", "Fehler",
						JOptionPane.ERROR_MESSAGE);
			} else {
				emailField.setText(generateEmail());
				log.successLog("E-Mail Adresse generieren: " + emailField.getText(), manager);
			}
		});
		gbc.gridx = 2;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		gbc.weightx = 0.3;
		add(emailButton, gbc);

		// -----------------------------------------------------------
		// --- Komponenten für Zeile: y = 6 --- TELEFONNUMMER - PW ---
		// -----------------------------------------------------------

		JLabel telLabel = new JLabel("Telefonnummer");
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.gridwidth = 1;
		gbc.weightx = 0.2;
		add(telLabel, gbc);

		// TelefonnummerField -> generateUserIDButton erzeugt userID
		telField = new JTextField();
		gbc.gridx = 1;
		gbc.gridy = 6;
		gbc.gridwidth = 1;
		gbc.weightx = 0.4;
		add(telField, gbc);

		// PasswordField - Standartmäßig steht "Passwort" drin
		passField = new JTextField("Passwort");
		gbc.gridx = 2;
		gbc.gridy = 6;
		gbc.gridwidth = 1;
		gbc.weightx = 0.4;
		add(passField, gbc);

		// ----------------------------------------------
		// --- Komponenten für Zeile: y = 7 --- Rolle ---
		// ----------------------------------------------

		JLabel roleLabel = new JLabel("Position");
		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.gridwidth = 1;
		add(roleLabel, gbc);

		// TODO Dropdown-Liste um Voreingestellte Rollen auszuwählen
		roleField = new JTextField("Hier soll ne Dropdown");
		roleField.setEditable(false);
		gbc.gridx = 1;
		gbc.gridy = 7;
		gbc.gridwidth = 1;
		add(roleField, gbc);

		// TODO Button Zeigt Liste aller Projekte -> Auswahl des Projektes ->
		// Such-/Filteroptionen
		// setzt ProjektLead des ausgewählten Projektes auf die generierte ID
		// ID darf nicht leer sein
		JButton setProjectLead = new JButton("Projektleiter setzen");
		setProjectLead.addActionListener(e -> {
			wMan.getCurrentProjectLeads();
		});
		gbc.gridx = 2;
		gbc.gridy = 7;
		gbc.gridwidth = 1;
		add(setProjectLead, gbc);

		// ------------------------------------
		// --- Komponenten für Zeile: y = 8 ---
		// ------------------------------------

		JLabel hourlyRateLabel = new JLabel("Stundenlohn - €");
		gbc.gridx = 0;
		gbc.gridy = 8;
		gbc.gridwidth = 1;
		add(hourlyRateLabel, gbc);

		// TODO Dropdown-Liste um Voreingestellte Rollen auszuwählen
		hourlyRateField = new JTextField();
		roleField.setEditable(false);
		gbc.gridx = 1;
		gbc.gridy = 8;
		gbc.gridwidth = 1;
		add(hourlyRateField, gbc);

		// ------------------------------------
		// --- Komponenten für Zeile: y = 9 ---
		// ------------------------------------

		// Benutzer erstellen Button
		JButton addUser = new JButton("Benutzer erstellen");
		addUser.addActionListener(e -> {
			// Überprüfe, ob alle erforderlichen Felder ausgefüllt sind
			if (userIdField.getText().isEmpty() || nameField.getText().isEmpty() || vornameField.getText().isEmpty()
					|| emailField.getText().isEmpty() || passField.getText().isEmpty()) {
				// Zeige eine Fehlermeldung, wenn eines der Pflichtfelder leer ist
				log.errorLog("Pflichtfelder (User-ID, Name, Vorname, E-Mail, Passwort) müssen ausgefüllt sein",
						manager);
				JOptionPane.showMessageDialog(null,
						"Alle Pflichtfelder (User-ID, Name, Vorname, E-Mail, Passwort) müssen ausgefüllt sein.",
						"Fehler", JOptionPane.ERROR_MESSAGE);
				return;
			}
			// Versuche, einen neuen Benutzer zu erstellen, nur wenn alle Felder ausgefüllt
			// sind
			try {
				// neues User-Objekt und setze die Werte aus den Eingabefeldern
				User user = new User();
				user.setUserId(userIdField.getText());
				user.setName(nameField.getText());
				user.setVorname(vornameField.getText());
				user.setEmail(emailField.getText());
				user.setTel(telField.getText()); // Tel-Feld darf leer sein, wird gesetzt, wenn ausgefüllt
				user.setProjectLead(projectLead); // Projektleiter-Flag
				user.setPass(passField.getText());

				// Versuche, den Stundensatz zu setzen. Falls das Feld leer ist, wird der
				// Standardwert 0.0 gesetzt.
				String hourlyRateText = hourlyRateField.getText();
				if (!hourlyRateText.isEmpty()) {
					user.setHourlyRate(Double.parseDouble(hourlyRateText));
				} else {
					user.setHourlyRate(0.0); // Standardwert
				}
				String role = roleField.getText();
				if (role.equals("Hier soll ne Dropdown")) {
					user.setRole(User.Role.User);
				} else {
					user.setRole(user.toRole(roleField.getText()));
				}
				// Benutzer in die Liste und Datenbank hinzufügen
				uMan.addUser(user);
				log.successLog("Benutzer hinzugefügt.", manager);
				// Aktualisiere die UI, um den neuen Benutzer anzuzeigen oder Felder zu leeren
				refreshAll();
			} catch (NumberFormatException ex) {
				// Falls der Stundenlohn nicht korrekt eingegeben wurde (kein Double), eine
				// Fehlermeldung anzeigen
				JOptionPane.showMessageDialog(null, "Stundenlohn muss eine Zahl sein.", "Fehler",
						JOptionPane.ERROR_MESSAGE);
			}

		});
		gbc.gridx = 1;
		gbc.gridy = 9;
		gbc.gridwidth = 2;
		add(addUser, gbc);

		// Eingabefelder leeren
		JButton clearFields = new JButton("Felder löschen");
		clearFields.addActionListener(e -> {
			clearFields();
		});
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		add(clearFields, gbc);

		// neuer testlog und DisplayArea, aktuaisierten
		JButton showLogs = new JButton("test log");
		showLogs.addActionListener(e -> {
			log.log("Knopf wurde gedrückt.", Log.LogType.INFO, Log.Manager.GUI);

		});
		gbc.gridx = 2;
		gbc.gridy = 15;
		gbc.gridwidth = 1;
		add(showLogs, gbc);

		JButton saveLogs = new JButton("Log Speichern");
		saveLogs.addActionListener(e -> {
			log.saveLogs();
		});
		gbc.gridx = 1;
		gbc.gridy = 15;
		gbc.gridwidth = 1;
		add(saveLogs, gbc);

		// Leerraum-Panel, das den restlichen Platz zwischen Log-Buttons und oberen Teil
		// ausfüllt
		gbc.gridx = 0;
		gbc.gridy = 10;
		gbc.gridwidth = 3; // Es soll die gesamte Breite einnehmen
		gbc.weighty = 1.0; // Füllt den restlichen vertikalen Platz aus
		gbc.fill = GridBagConstraints.BOTH; // Das Panel soll sich ausdehnen
		JPanel emptySpace = new JPanel(); // Leeres Panel
		add(emptySpace, gbc);
	}

	private void refreshAll() {
		clearFields();
		UserTableModel userModel = new UserTableModel(uMan.getUsers());
		userTable.setModel(userModel);
	}

	private void clearFields() {
		nameField.setText("");
		vornameField.setText("");
		userIdField.setText("");
		emailField.setText("");
		telField.setText("");
		passField.setText("Passwort");
		roleField.setText("Hier soll ne Dropdown"); // TODO Dropdown mit allen Rollen aus User
		projectLead = false;
		hourlyRateField.setText("");
	}

	// Methode zur Generierung einer eindeutigen User-ID
	private String generateUserId() {
		// Initialisiere den Zähler bei 1
		int counter = 1;
		// Basis-ID erstellen, indem die ersten drei Buchstaben des Vornamens und
		// Nachnamens genommen werden
		// .toLowerCase() sorgt dafür, dass alles kleingeschrieben wird
		String baseId = vornameField.getText().substring(0, Math.min(3, vornameField.getText().length())).toLowerCase()
				+ nameField.getText().substring(0, Math.min(3, nameField.getText().length())).toLowerCase();
		String genId = baseId + String.format("%02d", counter);

		boolean exists = true; // Setze eine Flag-Variable, um zu überprüfen, ob die generierte ID bereits
								// existiert
		// Schleife, um sicherzustellen, dass eine eindeutige User-ID erstellt wird
		while (exists) {
			exists = false; // Setze exist-Flag auf false, um anzunehmen, dass die ID einzigartig ist

			// Durchlaufe alle bestehenden Benutzer in der Liste
			for (User user : users) {
				// Falls die generierte ID bereits einem existierenden User zugewiesen ist
				if (user.getUserId().equals(genId)) {
					counter++; // Erhöhe den Zähler, um eine neue ID zu generieren
					// Erstelle die neue ID mit dem aktualisierten Zähler
					genId = baseId + String.format("%02d", counter);
					// Setze das exist-Flag auf true, um anzuzeigen, dass ein Konflikt existiert und
					// die Schleife weiterläuft
					exists = true;
					// Breche die Schleife ab, da eine Kollision erkannt wurde und eine neue ID
					// generiert werden muss
					break;
				}
			}
		}
		// Wenn die Schleife abgeschlossen ist, haben wir eine eindeutige ID und geben
		// sie zurück
		return genId;
	}

	// Methode zur Generierung einer eindeutigen E-Mail-Adresse
	private String generateEmail() {
		// Hole den Nachnamen aus dem Textfeld und wandle ihn in Kleinbuchstaben um
		String lastName = nameField.getText().toLowerCase();
		// Hole den Vornamen aus dem Textfeld
		String firstName = vornameField.getText().toLowerCase();
		int counter = 1; // Startzähler für den Buchstabenindex des Vornamens
		String email = firstName.substring(0, counter) + lastName + "@email.com";
		// Flag zum Überprüfen, ob die generierte E-Mail bereits existiert
		boolean exists = true;
		// Schleife, um eine eindeutige E-Mail-Adresse zu generieren
		while (exists) {
			exists = false;
			// Durchlaufe alle vorhandenen Benutzer, um Konflikte zu finden
			for (User user : users) {
				// Wenn die E-Mail bereits existiert, setze das exists-Flag auf true
				if (user.getEmail().equals(email)) {
					// Erhöhe den Zähler, um den nächsten Buchstaben des Vornamens zu verwenden
					counter++;

					// Vermeide IndexOutOfBoundsException: Falls der Vorname kürzer ist, wird nur
					// bis zur Länge des Vornamens gegangen
					if (counter <= firstName.length()) {
						email = firstName.substring(0, counter) + lastName + "@email.com";
					} else {
						// Falls der Vorname vollständig aufgebraucht ist, kann alternativ ein
						// numerischer Wert angehängt werden
						email = firstName + lastName + counter + "@email.com";
					}
					// Markiere, dass es noch einen Konflikt gibt
					exists = true;

					// Breche die Schleife ab, damit die neue E-Mail überprüft werden kann
					break;
				}
			}
		}

		// Gebe die eindeutige E-Mail-Adresse zurück
		return email;
	}

}
