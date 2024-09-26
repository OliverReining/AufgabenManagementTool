package v1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Analytics {

	// Benutzerdaten anzeigen
	public static Object[] showUserData(int userId) {
		Object[] userData = new Object[6];
		String sql = "SELECT * FROM benutzer WHERE userid = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, userId); // Setze die UserID als Parameter
			try (ResultSet rs = stmt.executeQuery()) {

				if (rs.next()) {
					// Die abgerufenen Werte in das Array einfügen
					userData[0] = rs.getInt("userid");
					userData[1] = rs.getString("name");
					userData[2] = rs.getString("vorname");
					userData[3] = rs.getString("email");
					userData[4] = rs.getString("pass");
					userData[5] = rs.getString("role");
				} else {
					// Falls der Benutzer nicht existiert
					System.out.println("Benutzer nicht gefunden.");
					return null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userData;
	}

	// Gesamtanzahl der Aufgaben
	public static int getTaskCount(int userId) {
		int taskCount = 0;
		String sql = "SELECT COUNT(taskid) AS 'AnzahlAufgaben' FROM task_user WHERE userid = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				taskCount = rs.getInt("AnzahlAufgaben");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return taskCount;
	}

	// Gesamtanzahl der Projekte
	public static int getProjectCount(int userId) {
		int projectCount = 0;
		String sql = "SELECT COUNT(DISTINCT projectid) AS AnzahlProjekte " + "FROM task_user tu "
				+ "JOIN task USING(taskid) " + "WHERE tu.userid = ?";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, userId); // Setze die UserID als Parameter
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				projectCount = rs.getInt("AnzahlProjekte");
			} else {
				// Falls der Benutzer nicht existiert
				System.out.println("Benutzer nicht gefunden.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return projectCount;
	}

	// Namen der Projekte und Anzahl der enthaltenen Aufgaben
	public static List<Map<String, Object>> getProjectNamesAndTaskCount(int userId) {
		String sql = "SELECT project.title AS Projekte, COUNT(task.taskid) AS Aufgaben, CONCAT(projektleiter.name, ' ', projektleiter.vorname) AS Projektleiter "
				+ "FROM project JOIN task USING(projectid) " + "JOIN task_user USING(taskid) "
				+ "JOIN benutzer projektleiter ON project.projectlead = projektleiter.userid "
				+ "WHERE task_user.userid = ? "
				+ "GROUP BY project.title, CONCAT(projektleiter.vorname, ' ', projektleiter.name);";
		List<Map<String, Object>> projectList = new ArrayList<>();

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Map<String, Object> projectData = new HashMap<>();
				projectData.put("Projekte", rs.getString("Projekte"));
				projectData.put("Aufgaben", rs.getInt("Aufgaben"));
				projectData.put("Projektleiter", rs.getString("Projektleiter"));

				projectList.add(projectData);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return projectList;
	}

	// TODO

}
