package v2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import v2.Log.Manager;

public class DatabaseConnection {

	// Statische Werte für die Verbindung (standardmäßig)
	private final String BASE_URL = "jdbc:mysql://localhost/";
	private String dbName;
	private String username;
	private String password;
	private LogManager log;
	private Connection conn; // Connection
	private DatabaseMetaData metaData; // MetaData der Datenbank
	private String catalog; // Datenbankname
	private Manager managerType = Log.Manager.DB_CONNECT;

	// Kontruktor mit LogManager aus der Main übergeben
	public DatabaseConnection(LogManager log) {
		this.log = log;
	}

	// Verbindung mit Standartwerten herstellen
	public void connect() throws SQLException {
		String dynamicUrl = BASE_URL + dbName;
		conn = DriverManager.getConnection(dynamicUrl, username, password);
		metaData = conn.getMetaData();
		catalog = conn.getCatalog();
	}

	// UNUSED: gleiche Methode mit log weiter unten
	// Verbindung mit dynamischen Werten herstellen
//	public void connect(String dynamicDbName, String dynamicUsername, String dynamicPassword) throws SQLException {
//		this.dbName = dynamicDbName;
//		this.username = dynamicUsername;
//		this.password = dynamicPassword;
//
//		String dynamicUrl = BASE_URL + dbName;
//		conn = DriverManager.getConnection(dynamicUrl, username, password);
//		metaData = conn.getMetaData();
//		catalog = conn.getCatalog();
//	}

	// Verbindung mit dynamischen Werten herstellen
	public void connect(String dynamicDbName, String dynamicUsername, String dynamicPassword) throws SQLException {
		this.dbName = dynamicDbName;
		log.log("databasename set", Log.LogType.SUCCESS, managerType);
		this.username = dynamicUsername;
		log.log("username set", Log.LogType.SUCCESS, managerType);
		this.password = dynamicPassword;
		log.log("passwort set", Log.LogType.SUCCESS, managerType);

		String dynamicUrl = BASE_URL + dbName;
		conn = DriverManager.getConnection(dynamicUrl, username, password);
		log.log("connected", Log.LogType.SUCCESS, managerType);
		metaData = conn.getMetaData();
		log.log("metadata set", Log.LogType.SUCCESS, managerType);
		catalog = conn.getCatalog();
		log.log("catalog set", Log.LogType.SUCCESS, managerType);
	}

	// Methode zum Abrufen der aktuellen Verbindung
	public Connection getConnection() {
		return conn;
	}

	// Methode zum Schließen der Verbindung
	public void closeConnection() {
		if (conn != null) {
			try {
				log.log("DB-Verbindung getrennt", Log.LogType.SUCCESS, managerType);
				conn.close();
			} catch (SQLException e) {
				log.sqlExceptionLog(e, managerType);				throw new RuntimeException(e);
			}
		}
	}

	// Getter für DatabaseMetaData
	public DatabaseMetaData getMetaData() {
		return metaData;
	}

	// Getter für den aktuellen Katalog (Datenbankname)
	public String getCatalog() {
		return catalog;
	}

	public ResultSet getResultSet(String sql) throws SQLException {
		ResultSet rs = getConnection().prepareStatement(sql).executeQuery();
		if (rs == null) {
			throw new SQLException("ResultSet konnte nicht geladen werden.");
		}
		return rs;
	}

	// Tabellennamen dynamisch finden (Case-Insensitive)
	public String findTableName(String desiredTable) throws SQLException {
		log.log("findTableName(" + desiredTable + ") gestartet.", Log.LogType.OPEN, managerType);
		ResultSet rs = metaData.getTables(null, null, "%", new String[] { "TABLE" });
		String tableName = null;
		switch (desiredTable) {
		case "benutzer":
		case "users":
		case "nutzer":
		case "project":
		case "projects":
		case "projekt":
			while (rs.next()) {
				tableName = rs.getString("TABLE_NAME");
				if (tableName.equalsIgnoreCase(desiredTable)) {
					log.successLog("gefunden: " + tableName, managerType);
					return tableName;
				}
			}
		default:
			throw new SQLException("Tabelle nicht erkannt: " + tableName);
		}
	}

	// Lister der Spaltennamen für eine ausgewählte Tabelle holen
	public List<String> getColumnNames(String tableName) throws SQLException {
		ResultSet rs = metaData.getColumns(null, null, tableName, null);
		if (rs == null) {
			throw new SQLException(
					"ResultSet konnte nicht geladen werden. Falscher Tabellenname.");
		}
		List<String> columnNames = new ArrayList<>();
		while (rs.next()) {
			columnNames.add(rs.getString("COLUMN_NAME"));
		}
		return columnNames;
	}

	// Typ der Spalte für eine ausgewählte Tabelle und Spalte holen
	public String getColumnType(String selectedTable, String selectedColumn) throws SQLException {
		ResultSet rs = metaData.getColumns(catalog, null, selectedTable, selectedColumn);
		if (rs == null) {
			throw new SQLException(
					"ResultSet konnte nicht geladen werden. Falscher Tabellenname, oder falscher Spaltenname.");
		}
		String columnType = null;
		if (rs.next()) {
			columnType = rs.getString("TYPE_NAME");
		}
		return columnType;

	}

	// Anmeldeinformationen (DB-Name, Benutzername, Passwort) aktualisieren
	public void setCredentials(String dynamicDbName, String dynamicUsername, String dynamicPassword) {
		this.dbName = dynamicDbName;
		this.username = dynamicUsername;
		this.password = dynamicPassword;
	}
}
