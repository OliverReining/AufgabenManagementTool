package v2;

import v1.DatabaseConnectionOld;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TaskManager {

    /*
     * TODO: dueDate, priority, isCompleted(boolean) hinzufügen Datenbank
     * TODO: Tasks als klasse und dann in DB einfügen
     *  -> weniger sql anfragen
     *  -> eine anfrage um alle daten der tabelle in Klasse zu speichern
     *  -> eine anfrage um alle updates am ende der sitzung hochzuladen
     *  -> zwischenspeicherung in Task-Klasse
     */
    private ArrayList<Task> tasks;

    public TaskManager() {
        new SQL();
    }

    // createTask für Klasse
    public void createTask(String title, String description, int projectId, boolean isCompleted) {
        Task task = new Task();

        task.setTitle(title);
        task.setDescription(description);
        task.setProjectId(projectId);
        task.setCompleted(isCompleted);
        addTask(task);
    }

    public void addTask(Task task){
        tasks.add(task);
    }

    // Liste aller Aufgaben holen
    public ArrayList<Task> getTasks() {
        return tasks;
    }

}
