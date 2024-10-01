package v2;

import v1.DatabaseConnectionOld;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
TODO: metaData benutzen um spalten abzufragen sql und values builder implementieren

 */


public class SQL {

    DatabaseConnection dbConnect;

    public SQL(){
        getTasks();
    }

    // Aufgaben aus Datenbank holen und Tasks speichern
    public void getTasks() {
        String sql = "SELECT * FROM tasks";

        try {
            assert dbConnect != null;
            try (Connection conn = dbConnect.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql);
                     ResultSet rs = stmt.executeQuery()
            ) {
                while (rs.next()) {
                    int taskId = rs.getInt("taskid");
                    String title = rs.getString("title");
                    String description = rs.getString("description");
                    int projectId = rs.getInt("projectid");
                    boolean isCompleted = rs.getBoolean("isCompleted");
                    Task task = new Task(taskId, title, description, projectId, isCompleted);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public String showTasks() {
        StringBuilder result = new StringBuilder();
        String sql = "SELECT * FROM task";
        try (Connection conn = DatabaseConnectionOld.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                result.append("ID: ").append(rs.getInt("taskid")).append(", Titel: ").append(rs.getString("title"))
                        .append(", Beschreibung: ").append(rs.getString("description")).append(", Projekt-ID: ")
                        .append(rs.getInt("projectid")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    // Aufgabe erstellen
    public void createTask(String title, String description, int projectId) {
        String sql = "INSERT INTO task (title, description, projectid) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnectionOld.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setInt(3, projectId); // projectid ist foreign key
            stmt.executeUpdate();
            System.out.println("Aufgabe erstellt!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Benutzer zur Aufgabe hinzufügen (Mehrere Benutzer pro Aufgabe)
    public void addUserToTask(int taskId, int userId) {
        String sql = "INSERT INTO task_user (taskid, userid) VALUES (?, ?)";
        try (Connection conn = DatabaseConnectionOld.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, taskId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
            System.out.println("Benutzer zur Aufgabe hinzugefügt!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Benutzer von der Aufgabe entfernen
    public void removeUserFromTask(int taskId, int userId) {
        String sql = "DELETE FROM task_user WHERE taskid = ? AND userid = ?";
        try (Connection conn = DatabaseConnectionOld.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, taskId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
            System.out.println("Benutzer von der Aufgabe entfernt!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Aufgabe löschen
    public void deleteTask(int taskId) {
        String sql = "DELETE FROM task WHERE taskid = ?";
        try (Connection conn = DatabaseConnectionOld.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, taskId);
            stmt.executeUpdate();
            System.out.println("Aufgabe gelöscht!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Aufgabe aktualisieren
    public void updateTask(int taskId, String newTitle, String newDescription, int newProjectId) {
        String sql = "UPDATE task SET title = ?, description = ?, projectid = ? WHERE taskid = ?";
        try (Connection conn = DatabaseConnectionOld.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newTitle);
            stmt.setString(2, newDescription);
            stmt.setInt(3, newProjectId);
            stmt.setInt(4, taskId);
            stmt.executeUpdate();
            System.out.println("Aufgabe aktualisiert!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


