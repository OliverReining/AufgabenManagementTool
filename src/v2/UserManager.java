package v2;

import java.sql.*;
import java.util.*;

//Manager-Klasse für Benutzer:
//Verwaltet Benutzer über eine ArrayList<User> und ermöglicht Interaktionen mit der Datenbank
//Diese Klasse ist verantwortlich für das Hinzufügen, Entfernen, Aktualisieren und Abrufen von Benutzerdaten
//sowie die Interaktion mit anderen Managern, wenn komplexere Anfragen benötigt werden (z.B. Worktime und Tasks)

public class UserManager {

	// Lokale Variablen
	private ArrayList<User> users; // Liste aller Benutzer, die in der Anwendung verwendet werden
	private DatabaseConnection dbConnect; // Verbindung zur Datenbank
	private LogManager log; // LogManager zum Protokollieren von Informationen und Fehlern
	private String desiredTable = "benutzer";

	// Konstruktoren
	public UserManager() {
		users = new ArrayList<>(); // Initialisiert die User-Liste ohne Datenbankverbindung
	}

	public UserManager(DatabaseConnection dbConnect) {
		this.dbConnect = dbConnect; // Setzt die Datenbankverbindung
		getAllUsers(); // Lädt Benutzer aus der Datenbank
	}

	public UserManager(DatabaseConnection dbConnect, LogManager log) {
		this.log = log; // Setzt LogManager
		this.dbConnect = dbConnect; // Setzt die Datenbankverbindung
		getAllUsers(); // Lädt Benutzer aus der Datenbank
	}

	// Methode zum Laden der Benutzer aus der Datenbank
	// Liest alle Benutzer aus der Tabelle "benutzer" in der Datenbank und fügt sie
	// der lokalen User-Liste hinzu
	private void getAllUsers() {
		users = new ArrayList<>();
		String sql = "SELECT * FROM benutzer;";
		log.log("Versuche '" + sql + "' auszuführen...", Log.LogType.INFO);

		try (ResultSet rs = dbConnect.getConnection().prepareStatement(sql).executeQuery()) {
			while (rs.next()) {
				// Erstellen eines neuen User-Objekts basierend auf den DB-Daten
				User user = new User(rs.getString("userid"), rs.getString("name"), rs.getString("vorname"),
						rs.getString("email"), rs.getString("tel"), rs.getString("pass"), rs.getString("role"),
						rs.getBoolean("projectlead"), rs.getDouble("hourlyRate"));
				users.add(user); // Benutzer zur lokalen Liste hinzufügen
			}
			setUsers(users); // Liste lokal speichern
		} catch (SQLException e) {
			log.sqlExceptionLog(e, sql); // Fehler protokollieren
			throw new RuntimeException(e);
		}
	}

	// Methode um einen Benutzer anhand seiner ID zu finden
	public User getUserById(String userId) {
		for (User user : users) {
			if (userId.equals(user.getUserId())) {
				return user; // Benutzer gefunden und zurückgegeben
			} else {
				log.log("User-ID nicht gefunden", Log.LogType.ERROR);
			}
		}
		return null; // Kein Benutzer gefunden
	}

	// TODO addUser(User user)
	// - Methode um einen neuen Benutzer in die Datenbank einzufügen
	// + SQL-INSERT-Befehl vorbereiten, um die Benutzerdaten in die Datenbank zu
	// schreiben
	// + Benutzer in die lokale ArrayList hinzufügen
	// - Exception Handling hinzufügen, falls das Einfügen fehlschlägt
	public void addUser(User user) {
		users.add(user); // Benutzer zur lokalen Liste hinzufügen
		// TODO: SQL-INSERT-Anweisung hier implementieren, um den Benutzer in die
		// Datenbank einzufügen
		dynamicSqlInsert(user);
	}

	public void sqlInsert(User user) {
		DatabaseMetaData metaData = dbConnect.getMetaData();

//		StringBuilder sqlsb = new StringBuilder();
		String table = "benutzer";
		String sql = "INSERT INTO " + table
				+ " (userid, name, vorname, email, tel, pass, role, isProjectLead, hourlyRate) VALUES ("
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?);";
		try (Connection conn = dbConnect.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, user.getUserId());
			stmt.setString(2, user.getName());
			stmt.setString(3, user.getVorname());
			stmt.setString(4, user.getEmail());
			stmt.setString(5, user.getTel());
			stmt.setString(6, user.getPass()); // später hashedPass
			stmt.setString(7, user.getRole());
			stmt.setBoolean(8, user.isProjectLead());
			stmt.setDouble(9, user.getHourlyRate());
			stmt.executeQuery();
		} catch (SQLException e) {
			log.sqlExceptionLog(e, sql);
		}
	}

	// TODO removeUser(User user)
	// - Methode um einen Benutzer aus der Datenbank zu löschen
	// - SQL-DELETE-Befehl vorbereiten, um den Benutzer anhand seiner ID aus der
	// Datenbank zu entfernen
	// - Benutzer aus der lokalen ArrayList entfernen
	// - Exception Handling hinzufügen, falls das Löschen fehlschlägt
	public void removeUser(User currentUser) {
		users.remove(currentUser); // Benutzer aus der lokalen Liste entfernen
		// TODO: SQL-DELETE-Anweisung hier implementieren, um den Benutzer aus der
		// Datenbank zu löschen

	}

	// TODO updateUser(User user)
	// - Methode um Benutzerdaten in der Datenbank zu aktualisieren
	// - SQL-UPDATE-Befehl vorbereiten, um die geänderten Benutzerdaten in der
	// Datenbank zu speichern
	// - Lokale Benutzerdaten in der ArrayList aktualisieren
	// - Exception Handling hinzufügen, falls das Update fehlschlägt
	public void updateUser(User currentUser) {
		for (User user : users) {
			if (user.getUserId().equals(currentUser.getUserId())) {
				// Lokale Liste aktualisieren
				user.setName(currentUser.getName());
				user.setVorname(currentUser.getVorname());
				user.setEmail(currentUser.getEmail());
				user.setTel(currentUser.getTel());
				user.setPass(currentUser.getPass());
				user.setRole(currentUser.getRole());
				user.setProjectLead(currentUser.isProjectLead());
				user.setHourlyRate(currentUser.getHourlyRate());
				return;
			}
		}
		// TODO: SQL-UPDATE-Anweisung hier implementieren, um Benutzerdaten in der
		// Datenbank zu aktualisieren
	}

	// Methode zum Abrufen aller Projektleiter über eine Liste von Projekten
	public ArrayList<User> getAllTimeProjectLeads(ArrayList<Project> projects) {
		ArrayList<User> projectLeads = new ArrayList<>();
		for (Project project : projects) {
			for (User user : users) {
				if (user.getUserId().equals(project.getProjectLead())) {
					projectLeads.add(user); // Benutzer als Projektleiter hinzufügen
				}
			}
		}
		return projectLeads;
	}

	// Methode um alle aktuellen Projektleiter abzurufen (nur für laufende Projekte)
	public ArrayList<User> getActualProjectLeads(ArrayList<Project> projects) {
		ArrayList<User> projectLeads = getAllTimeProjectLeads(projects);
		for (User projectLead : getAllTimeProjectLeads(projects)) {
			for (Project project : projects) {
				if (project.isCompleted()) {
					projectLeads.remove(projectLead); // Entfernt Benutzer, wenn das Projekt abgeschlossen ist
				}
			}
		}
		return projectLeads;
	}

	// Setter für Projektleiter
	public void setProjectLeads(ArrayList<User> projectLeads) {
		for (User user : users) {
			for (User leader : projectLeads) {
				if (user.getUserId().equals(leader.getUserId())) {
					user.setProjectLead(true); // Setzt Benutzer als Projektleiter
				}
			}
		}
	}

	// TODO searchUsers(String attribute, String value)
	// - Methode um Benutzer anhand eines Attributs in der Datenbank zu suchen (z.B.
	// Name oder E-Mail)
	// - SQL-SELECT-Befehl mit WHERE-Klausel vorbereiten, um nach dem Attribut zu
	// suchen
	// - Benutzer entsprechend der Suchkriterien aus der Datenbank laden und in die
	// ArrayList einfügen
	// - Exception Handling für mögliche Datenbankfehler

	// TODO validateUserData(User user)
	// - Methode zur Validierung der Benutzerdaten vor dem Einfügen oder
	// Aktualisieren in der Datenbank
	// - Überprüfen, ob alle Pflichtfelder ausgefüllt sind (z.B. Name und E-Mail)
	// - Überprüfen, ob die E-Mail-Adresse ein gültiges Format hat
	// - Falls Validierung fehlschlägt, eine Exception werfen oder eine
	// Fehlermeldung zurückgeben

	// TODO handleDatabaseExceptions()
	// - Methode für Exception Handling bei Datenbankoperationen
	// - SQLExceptions und andere mögliche Fehler abfangen
	// - Benutzer oder Entwickler über Fehler informieren (Logging oder
	// GUI-Benachrichtigung)

	// TODO getWorktimeByUser(User currentUser)
	// Methode, die alle Arbeitszeiten zurückgibt, die von einem Nutzer existieren
	// Dazu gehört:

	// TODO getTasksByUser(User currentUser)
	// Methode, die alle Aufgaben zurückgibt, an denen ein Benutzer arbeitet oder
	// gearbeitet hat
	// Dazu gehört:
	// Arbeitszeiten des Benutzers aus dem WorktimeManager abrufen
	// dazugehörigen Aufgaben aus dem TaskManager suchen

	// TODO getProjectsByUser(User currentUser)
	// Methode, die alle Projekte zurückgibt, an denen ein Benutzer arbeitet oder
	// gearbeitet hat
	// Dazu gehört:
	// Arbeitszeiten des Benutzers aus dem WorktimeManager abrufen
	// dazugehörigen Aufgaben aus dem TaskManager suchen
	// Aus den Aufgaben die Projekte ableiten und zurückgeben

	// Getter und Setter für die UserList
	public ArrayList<User> getUsers() {
		return users; // gibt Liste aller Nutzer zurück
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users; // setzt NutzerListe auf inputListe
	}

	// Dynamischer SQL-Insert
	public void dynamicSqlInsert(User user) {
		try (Connection conn = dbConnect.getConnection()) {

			// Tabellenname dynamisch finden
			String table = null;
			switch (desiredTable) {
			case "benutzer":
			case "users":
			case "nutzer":
				table = dbConnect.findTableName(desiredTable);
				break;
			default:
				throw new SQLException("Tabelle nicht erkannt: " + table);
			}
			if (table == null) {
				throw new RuntimeException("Tabelle " + desiredTable + " nicht gefunden.");
			}

			// Spaltennamen dynamisch ermitteln
			List<String> columnNames = dbConnect.getColumnNames(table);

			// Dynamischen SQL-Query aufbauen
			StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ").append(table).append(" (");
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
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				setUserParameters(stmt, columnNames, user);
				stmt.executeUpdate();
			}
		} catch (SQLException e) {
			log.sqlExceptionLog(e, "Tabelle " + desiredTable + " nicht gefunden.");
		}
	}

	// Dynamisches Setzen der PreparedStatement-Parameter
	private void setUserParameters(PreparedStatement stmt, List<String> columnNames, User user) throws SQLException {
		for (int i = 0; i < columnNames.size(); i++) {
			// .toLowerCase() ignoriert Groß-Kleinschreibung
			// .strip() entfernt alle Leerzeichen vorne und hinten
			// .replaceAll() übeschreibt Whitespace innerhalb mit ""
			// \\s für Whitespace-Zeichen (Leerzeichen, Tabs, etc.)
			// - steht für den Bindestrich.
			// + bedeutet, dass alle aufeinanderfolgenden Vorkommen entfernt werden.
			// wird ersetzt durch "" -> nichts
			String column = columnNames.get(i).toLowerCase().strip().replaceAll("\\s-]+", "");

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
			case "hashedpass":
			case "hashedpassword":
			case "hashedpasswort":
				stmt.setString(i + 1, user.getPass());
				break;
			case "role":
			case "rolle":
				stmt.setString(i + 1, user.getRole());
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

}
