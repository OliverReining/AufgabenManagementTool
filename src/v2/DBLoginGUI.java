package v2;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

@SuppressWarnings("serial")
public class DBLoginGUI extends JFrame {

	private JTextField dbNameField;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private DatabaseConnection dbConnect;
	private LogManager log;

	public DBLoginGUI(DatabaseConnection dbConnect, LogManager log) {
		this.dbConnect = dbConnect;
		this.log = log;

		setTitle("Datenbankverbindung");
		setSize(250, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridLayout(4, 2));
		setLocationRelativeTo(null);
		setResizable(false);

		JLabel dbNameLabel = new JLabel("Datenbankname:");
		dbNameField = new JTextField("ams");
		add(dbNameLabel);
		add(dbNameField);

		JLabel usernameLabel = new JLabel("Benutzername:");
		usernameField = new JTextField("root");
		add(usernameLabel);
		add(usernameField);

		JLabel passwordLabel = new JLabel("Passwort:");
		passwordField = new JPasswordField("");
		add(passwordLabel);
		add(passwordField);

		JButton connectButton = new JButton("Verbinden");
		connectButton.addActionListener(e -> {
			connectToDatabase();
		});
		add(connectButton);

		JButton cancelButton = new JButton("Abbrechen");
		cancelButton.addActionListener(e -> System.exit(0));
		add(cancelButton);
	}

	private void connectToDatabase() {
		String databaseName = dbNameField.getText();
		String username = usernameField.getText();
		String password = new String(passwordField.getPassword());

		// Überprüfen, ob die erforderlichen Felder ausgefüllt sind
		if (databaseName.isEmpty() || username.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Datenbankname und Benutzername dürfen nicht leer sein.", "Fehler",
					JOptionPane.ERROR_MESSAGE); // "this" zeigt Fehlermeldung zentriert in GUI an
			return;
		}

		try {
			// Verbindungsversuch mit den Benutzereingaben
			dbConnect.connect(databaseName, username, password, log);

			// Erfolgsmeldung anzeigen
			log.log("Verbindung zur Datenbank wurde hergestellt. -" + username, Log.LogType.SUCCESS);

			// Schließen des Login-Fensters
			dispose();
			new FullScreenGUI(dbConnect, log).setVisible(true);

		} catch (SQLException e) {
			// Fehlermeldung anzeigen, wenn die Verbindung fehlschlägt
			log.log("Verbindung zur Datenbank fehlgeschlagen.", Log.LogType.ERROR);
			log.sqlExceptionLog(e,"");
			log.saveLogs();
		}
	}
}
