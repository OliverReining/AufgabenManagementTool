package v2;

import java.sql.*;
import java.util.*;
import v2.Log.*;

//Manager-Klasse für Benutzer:
//Verwaltet Benutzer über eine ArrayList<User> und ermöglicht Interaktionen mit der Datenbank
//Diese Klasse ist verantwortlich für das Hinzufügen, Entfernen, Aktualisieren und Abrufen von Benutzerdaten
//sowie die Interaktion mit anderen Managern, wenn komplexere Anfragen benötigt werden (z.B. Worktime und Tasks)

public class UserManager {

	// Lokale Variablen
	private ArrayList<User> users; // Liste aller Benutzer, die in der Anwendung verwendet werden
	private DatabaseConnection dbConnect; // Verbindung zur Datenbank
	private LogManager log; // LogManager zum Protokollieren von Informationen und Fehlern
	// Manager Enum des Logs initialisieren um nicht jedes mal den Text zu schreiben
	private Manager managerType = Log.Manager.USER_MANAGER;

	// desiredTable Lokal auf "benutzer" gesetzt um bei änderung in DB nur 1
	// Variable zu ändern
	private String desiredTable = "benutzer";
	// Lokale Liste mit den Spaltennamen, werden basierend auf desiredTable geladen
	private List<String> columnNames; //

	public UserManager() {
		users = new ArrayList<>();
	}

	// Konstruktor wenn Datenbank vorhanden - nimmt vorhandene Verbindung
	// erstellt die lokale User-List basierend auf der DB
	// schreibt zusätzlich logMessages für den LogManager
	// Standart für Nachverofolgung mit log
	public UserManager(DatabaseConnection dbConnect, LogManager log) {
		this.log = log; // Setzt LogManager
		this.dbConnect = dbConnect;
		try {
			// versuche Spaltennamen dynamisch zu ermitteln und in lokale Liste zu speichern
			// damit alle Abfragen mit den gleichen Spaltennamen funktionieren
			columnNames = dbConnect.getColumnNames(dbConnect.findTableName(desiredTable));
		} catch (SQLException e) {
			log.sqlExceptionLog(e, managerType);
			throw new RuntimeException(e);
		}
		getAllUsers();
	}

	// Methode zum Laden der Benutzer aus der Datenbank
	// SQL-Query liest alle Benutzer aus der Tabelle "desiredTable" in der Datenbank
	// fügt sie der lokalen User-Liste hinzu
	private void getAllUsers() {

		String sql = "SELECT * FROM " + desiredTable;
		users = new ArrayList<>();
		log.log("Versuche '" + sql + "' auszuführen...", Log.LogType.INFO, managerType);

		try (ResultSet rs = dbConnect.getResultSet(sql)) {
			while (rs.next()) {
				// Erstellen eines neuen User-Objekts basierend auf den DB-Daten
				User user = new User();
				user.setUserId(rs.getString("userid"));
				user.setName(rs.getString("name"));
				user.setVorname(rs.getString("vorname"));
				user.setEmail(rs.getString("email"));
				user.setTel(rs.getString("tel"));
				user.setPass(rs.getString("pass"));
				user.setRole(user.toRole(rs.getString("role")));
				user.setProjectLead(rs.getBoolean("isprojectlead"));
				user.setHourlyRate(rs.getDouble("hourlyRate"));
				users.add(user); // Benutzer zur lokalen Liste hinzufügen
			}
			log.log(users.size() + " Nutzer gefunden.", Log.LogType.SUCCESS, managerType);
			setUsers(users); // Liste lokal speichern
		} catch (SQLException e) {
			log.sqlExceptionLog(e, managerType); // Fehler protokollieren
			throw new RuntimeException(e);
		}
	}

	// Methode um einen Benutzer anhand seiner ID zu finden
	public User getUserById(String userId) {
		// Schleife durch alle User in der UserList
		for (User user : users) {
			// wenn UserID übereinstimmt wird sie zurückgegeben
			if (userId.equals(user.getUserId())) {
				return user; // Benutzer gefunden und zurückgegeben
			}
		}
		log.log("User-ID konnte nicht gefunden werden", Log.LogType.ERROR, managerType);
		return null; // Kein Benutzer gefunden
	}

	// addUser(User user)
	// + Methode um einen neuen Benutzer hinzuzufügen
	// + Benutzer in die lokale ArrayList hinzufügen
	// + SQL-INSERT-Befehl vorbereiten, um die Benutzerdaten in die Datenbank zu
	// schreiben
	// + Exception Handling hinzufügen, falls das Einfügen fehlschlägt
	public void addUser(User user) {
//		users.add(user); 
		// wenn Benutzer nicht zur Liste hinzugefügt werden konnte ERROR-Log erstellen
		// und SQL-Insert abbrechen
		if (!users.add(user)) { // Benutzer zur lokalen Liste hinzufügen
			log.errorLog("Benutzer konnte nicht zur lokalen Liste hinzugefügt werden. SQL-Query abgebrochen!",
					managerType);
			return;
		}
		log.successLog("Benutzer zur lokalen Liste hinzugefügt.", managerType);
		try {
			sqlInsert(user, true);
		} catch (SQLException e) {
			log.sqlExceptionLog(e, managerType);
		}
	}

	// SQL-Query vorbereiten und an die Datenbank senden
	private void sqlInsert(User user, boolean isDynamic) throws SQLException {
		// wenn Dynamischer Insert false ist, wird der statische Befehl benutzt
		// kann zu Problemen führen wenn die Spaltennamen in der DB verändert wurden
		if (!isDynamic) {
			log.log("Versuche statischen SQL-Insert", Log.LogType.INFO, managerType);
			// SQL-Query "INSERT INTO" mit dem private String desiredTable = "benutzer"
			String sql = "INSERT INTO " + desiredTable
					+ " (userid, name, vorname, email, tel, pass, role, isProjectLead, hourlyRate) VALUES ("
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?);";
			try (PreparedStatement stmt = dbConnect.getConnection().prepareStatement(sql)) {
				stmt.setString(1, user.getUserId());
				stmt.setString(2, user.getName());
				stmt.setString(3, user.getVorname());
				stmt.setString(4, user.getEmail());
				stmt.setString(5, user.getTel());
				stmt.setString(6, user.getPass()); // später hashedPass
				stmt.setString(7, user.toRoleString(user.getRole()));
				stmt.setBoolean(8, user.isProjectLead());
				stmt.setDouble(9, user.getHourlyRate());
				stmt.executeQuery();
			} catch (SQLException e) {
				throw new SQLException(sql + " konnte nicht ausgeführt werden.", e);
			}
		}
		log.log("Versuche dynamischen SQL-Insert", Log.LogType.INFO, managerType);
		// Dynamischen SQL-Query aufbauen
		StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ").append(desiredTable).append(" (");
		StringBuilder valuesBuilder = new StringBuilder(" VALUES (");

		for (int i = 0; i < columnNames.size(); i++) {
			sqlBuilder.append(columnNames.get(i));
			valuesBuilder.append("?");

			if (i < columnNames.size() - 1) {
				sqlBuilder.append(", ");
				valuesBuilder.append(", ");
			}
		}
		sqlBuilder.append(")").append(valuesBuilder).append(");");

		String sql = sqlBuilder.toString();

		// PreparedStatement vorbereiten
		try (PreparedStatement stmt = dbConnect.getConnection().prepareStatement(sql)) {
			setUserParameters(stmt, columnNames, user);
			stmt.executeUpdate();
			log.successLog(sql, managerType);
		} catch (SQLException e) {
			throw new SQLException(sql + " konnte nicht ausgeführt werden.");
		}
	}

	// TODO updateUser(User user)
	// + Methode um Benutzerdaten in der Datenbank zu aktualisieren
	// - SQL-UPDATE-Befehl vorbereiten, um die geänderten Benutzerdaten in der
	// Datenbank zu speichern
	// + Lokale Benutzerdaten in der ArrayList aktualisieren
	// + Exception Handling hinzufügen, falls das Update fehlschlägt
	public void updateUser(User currentUser) {
		for (User user : users) {
			if (user.getUserId().equals(currentUser.getUserId())) {
				log.successLog(currentUser.toString() + "in Liste gefunden.", managerType);
				// Lokale Liste aktualisieren
				user.setUserId(currentUser.getUserId());
				user.setName(currentUser.getName());
				user.setVorname(currentUser.getVorname());
				user.setEmail(currentUser.getEmail());
				user.setTel(currentUser.getTel());
				user.setPass(currentUser.getPass());
				user.setRole(currentUser.getRole());
				user.setProjectLead(currentUser.isProjectLead());
				user.setHourlyRate(currentUser.getHourlyRate());
				log.log("User List updated: " + user.toString(), Log.LogType.INFO, managerType);
				try {
					log.log("try sqlUpdate:", Log.LogType.INFO, managerType);
					sqlUpdate(currentUser, true);
				} catch (SQLException e) {
					log.sqlExceptionLog(e, managerType);
				}
			}
		}

	}

	// Benutzerdaten in der Datenbank zu aktualisieren
	public void sqlUpdate(User user, boolean isDynamic) throws SQLException {
		// wenn Dynamischer Insert false ist, wird der vorgefertigte Befehl benutzt
		// kann zu Problemen führen wenn die Spaltennamen in der DB verändert wurden
		if (!isDynamic) {
			log.log("Versuche statische SQL-Query auszuführen.", Log.LogType.INFO, managerType);
			String sql = "UPDATE " + desiredTable
					+ " SET name = ?, vorname = ?, email = ?, tel = ?, pass = ?, role = ?, isProjectLead = ?, hourlyRate = ? WHERE userid = ?;";
			try (PreparedStatement stmt = dbConnect.getConnection().prepareStatement(sql)) {
				stmt.setString(1, user.getName());
				stmt.setString(2, user.getVorname());
				stmt.setString(3, user.getEmail());
				stmt.setString(4, user.getTel());
				stmt.setString(5, user.getPass()); // später hashedPass
				stmt.setString(6, user.toRoleString(user.getRole()));
				stmt.setBoolean(7, user.isProjectLead());
				stmt.setDouble(8, user.getHourlyRate());
				stmt.setString(9, user.getUserId());
				stmt.executeUpdate();
				log.successLog(sql, managerType);
			} catch (SQLException e) {
				throw new SQLException(sql + " konnte nicht ausgeführt werden.", e);
			}
		} else {
			log.log("Versuche dynamische SQL-Query auszuführen.", Log.LogType.INFO, managerType);
			// Dynamischen SQL-Query aufbauen
			StringBuilder sqlBuilder = new StringBuilder("UPDATE ").append(desiredTable).append(" SET ");
			for (int i = 0; i < columnNames.size(); i++) {
				String column = columnNames.get(i).toLowerCase().strip().replaceAll("[\\s-]+", "");

				// Überspringe die "userid"-Spalte
				if (column.equals("userid")) {
					continue; // Überspringen
				}
				sqlBuilder.append(columnNames.get(i)).append(" = ?");
				if (i < columnNames.size() - 1) {
					sqlBuilder.append(", ");
				}
			}

			// Entfernt das letzte Komma, falls es existiert
			if (sqlBuilder.charAt(sqlBuilder.length() - 2) == ',') {
				sqlBuilder.setLength(sqlBuilder.length() - 2); // entfernt das letzte ", "
			}

			// WHERE-Bedingung hinzufügen (angenommen columnNames.get(0) ist der
			// Primärschlüssel, z.B. 'userid')
			sqlBuilder.append(" WHERE ").append(columnNames.get(0)).append(" = ?;");

			String sql = sqlBuilder.toString();

			log.log(sql, Log.LogType.SUCCESS, managerType);
			// PreparedStatement vorbereiten
			try (PreparedStatement stmt = dbConnect.getConnection().prepareStatement(sql)) {
				setUserParameters(stmt, columnNames, user);
				stmt.executeUpdate();
				log.successLog(sql, managerType);
			} catch (SQLException e) {
				throw new SQLException(sql + " konnte nicht ausgeführt werden.", e);
			}

		}

	}

	// TODO removeUser(User user)
	// - Methode um einen Benutzer aus der Datenbank zu löschen
	// - SQL-DELETE-Befehl vorbereiten, um den Benutzer anhand seiner ID aus der
	// Datenbank zu entfernen
	// - Benutzer aus der lokalen ArrayList entfernen
	// - Exception Handling hinzufügen, falls das Löschen fehlschlägt
	public void removeUser(User currentUser) {
		if (!users.remove(currentUser)) {
			log.errorLog("Benutzer konnte nicht entfernt werden. SQL-Query abgebrochen!", managerType);
			return;
		}
		log.successLog("Benutzer aus lokaler Liste gelöscht.", managerType);
		try {
			sqlDelete(currentUser, true);
		} catch (SQLException e) {
			log.sqlExceptionLog(e, managerType);
		}
	}

	// Methode um User aus Datenbank zu löschen
	private void sqlDelete(User currentUser, boolean isDynamic) throws SQLException {
		// TODO: SQL-DELETE-Anweisung hier implementieren, um den Benutzer aus der
		String sql = "DELETE FROM " + desiredTable + " WHERE userid = ?";

		try (PreparedStatement stmt = dbConnect.getConnection().prepareStatement(sql)) {
			stmt.setString(1, currentUser.getUserId());
			stmt.executeUpdate();
			log.successLog(sql, managerType);
		}
		throw new SQLException(sql + " konnte nicht ausgeführt werden.");

	}

	// Setter für Projektleiter
	public void setProjectLeads(ArrayList<Project> projects) {
		ArrayList<User> currentLeads = getCurrentProjectLeads(projects);
		for (User user : users) {
			boolean wasLead = user.isProjectLead(); // Prüfe, ob der Benutzer vorher ein Projektleiter war
			boolean isLead = false; // Standardmäßig ist er kein Projektleiter
			// Überprüfe, ob der Benutzer in der Liste der aktuellen Projektleiter ist
			for (User leader : currentLeads) {
				if (user.getUserId().equals(leader.getUserId())) {
					isLead = true;
					break; // Schleife abbrechen, wenn wir den Benutzer gefunden haben
				}
			}
			user.setProjectLead(isLead); // Setze den neuen Status
			// Update nur, wenn sich der Status geändert hat
			if (wasLead != isLead) {
				updateUser(user);
			}
		}
	}

	// Methode um alle aktuellen Projektleiter abzurufen (nur für laufende Projekte)
	public ArrayList<User> getCurrentProjectLeads(ArrayList<Project> projects) {
		ArrayList<User> currentLeads = new ArrayList<>(getAllTimeProjectLeads(projects));
		for (User projectLead : currentLeads) {
			for (Project project : projects) {
				if (project.isCompleted()) {
					currentLeads.remove(projectLead); // Entfernt Benutzer, wenn das Projekt abgeschlossen ist
				}
			}
		}
		return currentLeads;
	}

	// Methode zum Abrufen der All-Time Projektleiter über eine Liste von Projekten
	public ArrayList<User> getAllTimeProjectLeads(ArrayList<Project> projects) {
		ArrayList<User> allTeamLeads = new ArrayList<>();
		for (Project project : projects) {
			for (User user : users) {
				if (user.equals(project.getProjectLead())) {
					allTeamLeads.add(user); // Benutzer als Projektleiter hinzufügen
				}
			}
		}
		return allTeamLeads;
	}

	// TODO searchUser(String attribute, String value)
	// - Methode um Benutzer anhand eines Attributs in der Liste zu suchen (z.B.
	// Name oder E-Mail)

	// TODO handleDatabaseExceptions()
	// - Methode für Exception Handling bei Datenbankoperationen
	// - SQLExceptions und andere mögliche Fehler abfangen
	// - Benutzer oder Entwickler über Fehler informieren (Logging oder
	// GUI-Benachrichtigung)

	// Dynamisches Setzen der PreparedStatement-Parameter für die SQL-Query (INSERT
	// INTO, UPDATE)
	private void setUserParameters(PreparedStatement stmt, List<String> columnNames, User user) throws SQLException {
		log.log("setUserParameters() gestartet", Log.LogType.SUCCESS, managerType);
		for (int i = 0; i < columnNames.size(); i++) {
			// .toLowerCase() ignoriert Groß-Kleinschreibung
			// .strip() entfernt alle Leerzeichen vorne und hinten
			// .replaceAll() übeschreibt Whitespace innerhalb mit ""
			// \\s für Whitespace-Zeichen (Leerzeichen, Tabs, etc.)
			// - steht für den Bindestrich.
			// + bedeutet, dass alle aufeinanderfolgenden Vorkommen entfernt werden.
			// wird ersetzt durch "" -> nichts
			String column = columnNames.get(i).toLowerCase().strip().replaceAll("[\\s-]+", "");

			// Je nach Spaltenname wird der Wert des entsprechenden Feldes geholt und
			// an PreparedStaement übergeben
			switch (column) {
			case "userid": // -> auch UserID, userID, userId, User-ID...
				stmt.setString(i + 1, user.getUserId());
				break;
			case "name":
			case "lastname":
			case "nachname":
				stmt.setString(i + 1, user.getName());
				break;
			case "vorname":
			case "prename":
				stmt.setString(i + 1, user.getVorname());
				break;
			case "email":
				stmt.setString(i + 1, user.getEmail());
				break;
			case "tel":
			case "phone":
			case "telephone":
			case "phonenumber":
			case "telephonenumber":
			case "telnr":
			case "phonenr":
				stmt.setString(i + 1, user.getTel());
				break;
			case "pass":
			case "password":
			case "passwort":
			case "hashpass":
			case "hashpassword":
			case "hashedpass":
			case "hashedpassword":
			case "hashedpasswort":
				stmt.setString(i + 1, user.getPass());
				break;
			case "role":
			case "rolle":
			case "position":
				stmt.setString(i + 1, user.toRoleString(user.getRole()));
				break;
			case "isprojectlead":
			case "isprojectleader":
			case "projectlead":
			case "projectleader":
			case "projektleiter":
			case "projektleitung":
				stmt.setBoolean(i + 1, user.isProjectLead());
				break;
			case "hourlyrate":
			case "stundensatz":
				stmt.setDouble(i + 1, user.getHourlyRate());
				break;
			default:
				throw new SQLException("Unbekannte Spalte: " + column);
			}
		}
	}

	// Getter und Setter für die UserList
	public ArrayList<User> getUsers() {
		return users; // gibt Liste aller Nutzer zurück
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users; // setzt NutzerListe auf inputListe
	}

}
