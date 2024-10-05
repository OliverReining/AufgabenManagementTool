package v2;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class TaskTableModel extends AbstractTableModel {

	private final String[] columnNames = { "Task ID", "Title", "Beschreibung", "Project ID", "Abgabe",
			"Priorität", "Fertig" };
	private final ArrayList<Task> tasks;

	public TaskTableModel(ArrayList<Task> tasks) {
		this.tasks = tasks;
	}

	@Override
	public int getRowCount() {
		return tasks.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Task task = tasks.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return task.getTaskId();
		case 1:
			return task.getTitle();
		case 2:
			return task.getDescription();
		case 3:
			return task.getProjectId();
		case 4:
			return task.getDueDate();
		case 5:
			return task.getPriority();
		case 6:
			return task.isCompleted() ? "Ja" : "Nein";
		default:
			return null;
		}
	}
}
