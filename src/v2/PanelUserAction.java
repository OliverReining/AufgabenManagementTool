package v2;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class PanelUserAction extends JPanel {

	private JTextField userIdField;
	private JTextField nameField;
	private JTextField vornameField;
	private JTextField emailField;
	private JTextField telField;
	private JTextField roleField;
	private JTextField passField;
	private boolean projectLead = false;
	private JTextField hourlyRateField;

	public PanelUserAction(ArrayList<User> users, JTable userTable, UserManager uMan, LogManager log) {
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

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
			UserTableModel userModel = new UserTableModel(uMan.getUsers());
			userTable.setModel(userModel);
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
			User newUser = new User();
			// TODO: Felder auslesen -> new User("Werte aus den Feldern")

			uMan.addUser(newUser);
			UserTableModel userModel = new UserTableModel(uMan.getUsers());
			userTable.setModel(userModel);
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
				new FrameEditUser(uMan.getUserById((String) userTable.getValueAt(selectedRow, 0)), uMan, log)
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

		// -----------------------------------------------
		// --- Komponenten für Zeile: y = 4 --- E-MAIL ---
		// -----------------------------------------------

		JLabel emailLabel = new JLabel("E-Mail");
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		add(emailLabel, gbc);

		// E-Mail Textfeld -> generateEmailButton erzeugt email
		emailField = new JTextField();
		emailField.setEditable(false);
		gbc.gridx = 1;
		gbc.gridy = 4;
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
			}
		});
		gbc.gridx = 2;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.weightx = 0.3;
		add(emailButton, gbc);

		// ------------------------------------------------------
		// --- Komponenten für Zeile: y = 5 --- TELEFONNUMMER ---
		// ------------------------------------------------------

		JLabel telLabel = new JLabel("Telefonnummer");
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		add(telLabel, gbc);

		// UserID Field -> generateUserIDButton erzeugt userID
		telField = new JTextField();
		telField.setEditable(true); // verhindert selbstständiges schreiben in der textbox
		gbc.gridx = 1;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		gbc.weightx = 0.7;
		add(telField, gbc);

		// ------------------------------------------------
		// --- Komponenten für Zeile: y = 6 --- USER-ID ---
		// ------------------------------------------------

		JLabel userIdLabel = new JLabel("User-ID");
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.gridwidth = 1;
		add(userIdLabel, gbc);

		// UserID Field -> generateUserIDButton erzeugt userID
		userIdField = new JTextField();
		userIdField.setEditable(false); // verhindert selbstständiges schreiben in der textbox
		gbc.gridx = 1;
		gbc.gridy = 6;
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
			}
		});
		gbc.gridx = 2;
		gbc.gridy = 6;
		gbc.gridwidth = 1;
		gbc.weightx = 0.3;
		add(userIdButton, gbc);

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

		// Button setzt ProjektLeiter auf true
		JButton setProjectLead = new JButton("Projektleiter setzen");
		setProjectLead.addActionListener(e -> {
			// TODO setzt Projektleitung des Neuen nutzers auf true
			// erfordert Eingabe des Projektes
			projectLead = true;
		});
		gbc.gridx = 2;
		gbc.gridy = 7;
		gbc.gridwidth = 1;
		add(setProjectLead, gbc);

		// ------------------------------------
		// --- Komponenten für Zeile: y = 8 ---
		// ------------------------------------

		JLabel hourlyRateLabel = new JLabel("Stundenlohn");
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
			// TODO: Felder auslesen -> new User("Werte aus den Feldern")
			User newUser = new User(userIdField.getText(), nameField.getText(), vornameField.getText(),
					emailField.getText(), telField.getText(), roleField.getText(), "pass123", projectLead,
					Double.parseDouble(hourlyRateField.getText()));
			uMan.addUser(newUser);
			UserTableModel userModel = new UserTableModel(uMan.getUsers());
			userTable.setModel(userModel);
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
		gbc.gridwidth = 4; // Es soll die gesamte Breite einnehmen
		gbc.weighty = 1.0; // Füllt den restlichen vertikalen Platz aus
		gbc.fill = GridBagConstraints.BOTH; // Das Panel soll sich ausdehnen
		JPanel emptySpace = new JPanel(); // Leeres Panel
		add(emptySpace, gbc);
	}

	private void clearFields() {
		nameField.setText("");
		vornameField.setText("");
		userIdField.setText("");
		emailField.setText("");
		telField.setText("");
		passField.setText("");
		roleField.setText("");
		projectLead = false;
		hourlyRateField.setText("");
	}

	private String generateUserId() {
		return vornameField.getText().substring(0, Math.min(3, vornameField.getText().length())).toLowerCase()
				+ nameField.getText().substring(0, Math.min(3, nameField.getText().length())).toLowerCase();
	}

	private String generateEmail() {
		return vornameField.getText().substring(0, 1).toLowerCase() + nameField.getText().toLowerCase() + "@email.com";
	}
}
