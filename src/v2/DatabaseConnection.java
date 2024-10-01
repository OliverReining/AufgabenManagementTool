package v2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {

    // Statische Werte für die Verbindung (standardmäßig)
    private final String BASE_URL = "jdbc:mysql://localhost/";
    private String dbName = "ams"; // Standardmäßig "ams"
    private String username = "root"; // Standardbenutzer
    private String password = ""; // Standardpasswort
    private DatabaseMetaData metaData;
    private String catalog;

    public DatabaseConnection() {
        try {
            metaData = getConnection().getMetaData();
            catalog = getConnection().getCatalog();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Standard-Methode für die Manager-Klassen (unverändert)
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(BASE_URL + dbName, username, password);
    }

    // Login-Eingabe mit dynamische Werten
    public Connection getConnection(String dynamicDbName, String dynamicUsername, String dynamicPassword)
            throws SQLException {
        String dynamicUrl = BASE_URL + dynamicDbName; // URL wird dynamisch erstellt
        return DriverManager.getConnection(dynamicUrl, dynamicUsername, dynamicPassword);
    }

    // Anmeldeinformationen aktualisieren
    public void setCredentials(String dynamicDbName, String dynamicUsername, String dynamicPassword) {
        dbName = dynamicDbName; // Standard-DB-Name wird geändert
        username = dynamicUsername; // Standard-Benutzername wird geändert
        password = dynamicPassword; // Standard-Passwort wird geändert
    }

    public DatabaseMetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(DatabaseMetaData metaData) {
        this.metaData = metaData;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    // Lister der Tabellennamen aus DB holen
    public List<String> getTableNames() {
        try {
            ResultSet rs = metaData.getTables(catalog, null, "%", new String[]{"Table"});
            List<String> tableNames = new ArrayList<>();

            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                tableNames.add(tableName);
            }
            return tableNames;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getColumnNames(String selectedTable) {
        try {
            ResultSet rs = metaData.getColumns(catalog, null, selectedTable, null);
            List<String> columns = new ArrayList<>();
            while (rs.next()){
                String columnName = rs.getString("COLUMN_NAME");
                columns.add(columnName);
            }
            return columns;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
