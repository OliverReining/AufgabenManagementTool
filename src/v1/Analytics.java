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
	public static Object[][] getProjectNamesAndTaskCount(int userId) {
		String sql = "SELECT DISTINCT(project.title) AS Projekte, COUNT(task.taskid) AS Aufgaben, "
				+ "CONCAT(projektleiter.name, ' ', projektleiter.vorname) AS Projektleiter "
				+ "FROM project JOIN task USING(projectid) JOIN task_user USING(taskid) "
				+ "JOIN benutzer projektleiter ON project.projectlead = projektleiter.userid "
				+ "WHERE task_user.userid = ? "
				+ "GROUP BY project.title, CONCAT(projektleiter.vorname, ' ', projektleiter.name);";
		List<Map<String, Object>> projectList = new ArrayList<>();
		String[] keys = { "Projekte", "Aufgaben", "Projektleiter" };

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
		return DataPrep.listMapInObject(projectList, keys);
	}

	// Abfrage ob zusätzliches TabbedPane angezeigt wird
	public static boolean isProjectLead(int userId) {
		boolean isLead = false;
		String sql = "SELECT project.projectid FROM project JOIN benutzer ON benutzer.userid = project.projectlead WHERE userid = ?;";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				isLead = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return isLead;
	}

	// Titel, Beschreibung, Name des Projektleiters von Aufgaben an denen User
	// mitarbeitet
	public static Object[][] getTaskInfo(int userId) {
		// Titel, Beschreibung, Projektleiter
		String sql = "SELECT t.title AS Titel, t.description AS Beschreibung, CONCAT(proj_leader.vorname, ' ', proj_leader.name) AS Projektleiter "
				+ "FROM task t JOIN project p ON t.projectid = p.projectid JOIN benutzer proj_leader ON p.projectlead = proj_leader.userid "
				+ "WHERE t.taskid IN (SELECT taskid FROM task_user WHERE userid = ?) GROUP BY t.taskid;";
		List<Map<String, Object>> taskList = new ArrayList<>();
		String[] keys = { "Titel", "Beschreibung", "Projektleiter" };

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Map<String, Object> taskData = new HashMap<>();
				taskData.put(keys[0], rs.getString(keys[0]));
				taskData.put(keys[1], rs.getString(keys[1]));
				taskData.put(keys[2], rs.getString(keys[2]));

				taskList.add(taskData);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return DataPrep.listMapInObject(taskList, keys);
	}

	public static Object[][] getProjectTasks(int userId) {
		// Alle Aufgaben des Projektes bei denen der User Projectlead ist, und alle
		// Mitarbeiter der Aufgaben
		String sql = "SELECT project.title AS Projekt, task.title AS Aufgabe, "
				+ "CONCAT(benutzer.name, ' ', benutzer.vorname) AS Mitarbeiter "
				+ "FROM project LEFT JOIN task USING(projectid) LEFT JOIN task_user USING(taskid) "
				+ "LEFT JOIN benutzer ON task_user.userid = benutzer.userid WHERE project.projectlead = ? "
				+ "GROUP BY task.title, benutzer.userid ORDER BY task.title;";
		List<Map<String, Object>> projectTasksList = new ArrayList<>();
		String[] keys = { "Projekt", "Aufgabe", "Mitarbeiter" };

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				Map<String, Object> data = new HashMap<>();
				for(String key : keys) {
					data.put(key, rs.getObject(key));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return DataPrep.listMapInObject(projectTasksList, keys);
	}

}
