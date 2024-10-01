package v1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionOld {

	// Statische Werte für die Verbindung (standardmäßig)
	private static final String BASE_URL = "jdbc:mysql://localhost/";
	private static String dbName = "ams"; // Standardmäßig "ams"
	private static String username = "root"; // Standardbenutzer
	private static String password = ""; // Standardpasswort

	// Standard-Methode für die Manager-Klassen (unverändert)
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(BASE_URL + dbName, username, password);
	}

	// Login-Eingabe mit dynamische Werten
	public static Connection getConnection(String dynamicDbName, String dynamicUsername, String dynamicPassword)
			throws SQLException {
		String dynamicUrl = BASE_URL + dynamicDbName; // URL wird dynamisch erstellt
		return DriverManager.getConnection(dynamicUrl, dynamicUsername, dynamicPassword);
	}

	// Anmeldeinformationen aktualisieren
	public static void setCredentials(String dynamicDbName, String dynamicUsername, String dynamicPassword) {
		dbName = dynamicDbName; // Standard-DB-Name wird geändert
		username = dynamicUsername; // Standard-Benutzername wird geändert
		password = dynamicPassword; // Standard-Passwort wird geändert
	}
}
