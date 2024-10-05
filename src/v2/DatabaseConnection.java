package v2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {

	// Statische Werte für die Verbindung (standardmäßig)
	private final String BASE_URL = "jdbc:mysql://localhost/";
	private String dbName = "ams";
	private String username = "root";
	private String password = "";
	private LogManager log;
	private Connection conn; // Connection
	private DatabaseMetaData metaData; // MetaData der Datenbank
	private String catalog; // Datenbankname
	private ResultSet rs;

	public DatabaseConnection() {
		// Leerer Konstruktor
	}

	public DatabaseConnection(LogManager log) {
		// Leerer Konstruktor mit LogManager
		this.log = log;
	}

	// Verbindung mit Standartwerten herstellen
	public void connect() throws SQLException {
		String dynamicUrl = BASE_URL + dbName;
		conn = DriverManager.getConnection(dynamicUrl, username, password);
		metaData = conn.getMetaData();
		catalog = conn.getCatalog();
	}

	// Verbindung mit dynamischen Werten herstellen
	public void connect(String dynamicDbName, String dynamicUsername, String dynamicPassword) throws SQLException {
		this.dbName = dynamicDbName;
		this.username = dynamicUsername;
		this.password = dynamicPassword;

		String dynamicUrl = BASE_URL + dbName;
		conn = DriverManager.getConnection(dynamicUrl, username, password);
		metaData = conn.getMetaData();
		catalog = conn.getCatalog();
	}

	// Verbindung mit dynamischen Werten herstellen, log
	public void connect(String dynamicDbName, String dynamicUsername, String dynamicPassword, LogManager log)
			throws SQLException {
		this.dbName = dynamicDbName;
		log.log("databasename set", Log.LogType.SUCCESS);
		this.username = dynamicUsername;
		log.log("username set", Log.LogType.SUCCESS);
		this.password = dynamicPassword;
		log.log("passwort set", Log.LogType.SUCCESS);

		String dynamicUrl = BASE_URL + dbName;
		conn = DriverManager.getConnection(dynamicUrl, username, password);
		log.log("connected", Log.LogType.SUCCESS);
		metaData = conn.getMetaData();
		log.log("metadata set", Log.LogType.SUCCESS);
		catalog = conn.getCatalog();
		log.log("catalog set", Log.LogType.SUCCESS);
	}

	// Methode zum Abrufen der aktuellen Verbindung
	public Connection getConnection() {
		return conn;
	}

	// Methode zum Schließen der Verbindung
	public void closeConnection() {
		if (conn != null) {
			try {
				log.log("Verbindung getrennt", Log.LogType.SUCCESS);
				conn.close();
			} catch (SQLException e) {
				log.log("Verbindung konnte nicht getrennt werden,\n" + e.getMessage(), Log.LogType.SUCCESS);
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

	public ResultSet getResultSet(String sql) {
		try {
			return rs = getConnection().prepareStatement(sql).executeQuery();
		} catch (SQLException e) {
			log.sqlExceptionLog(e, sql);
			return null;
		}
	}
	
    // Tabellennamen dynamisch finden (Case-Insensitive)
    public String findTableName(String desiredTableName) throws SQLException {
        ResultSet rs = metaData.getTables(null, null, "%", new String[]{"TABLE"});
        while (rs.next()) {
            String tableName = rs.getString("TABLE_NAME");
            if (tableName.equalsIgnoreCase(desiredTableName)) {
                return tableName;
            }
        }
        return null;
    }

	// Lister der Spaltennamen für eine ausgewählte Tabelle holen
    public List<String> getColumnNames(String tableName) throws SQLException {
        ResultSet rs = metaData.getColumns(null, null, tableName, null);
        List<String> columnNames = new ArrayList<>();
        while (rs.next()) {
            columnNames.add(rs.getString("COLUMN_NAME"));
        }
        return columnNames;
    }

	// Typ der Spalte für eine ausgewählte Tabelle und Spalte holen
	public String getColumnType(String selectedTable, String selectedColumn) {
		try (ResultSet rs = metaData.getColumns(catalog, null, selectedTable, selectedColumn)) {
			String columnType = null;
			if (rs.next()) {
				columnType = rs.getString("TYPE_NAME");
			}
			return columnType;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	// Anmeldeinformationen (DB-Name, Benutzername, Passwort) aktualisieren
	public void setCredentials(String dynamicDbName, String dynamicUsername, String dynamicPassword) {
		this.dbName = dynamicDbName;
		this.username = dynamicUsername;
		this.password = dynamicPassword;
	}
}
