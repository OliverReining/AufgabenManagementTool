package v2;

import java.util.*;
import javax.swing.table.AbstractTableModel;

public class ProjectTableModel extends AbstractTableModel {

	private final String[] columnNames = { "Project-ID", "Titel", "Beschreibung", "Abgabe", "Fertig", "Projektleiter" };
	private final ArrayList<Project> projects;

	public ProjectTableModel(ArrayList<Project> projects) {
		this.projects = projects;
	}

	@Override
	public int getRowCount() {
		return projects.size();
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
		Project project = projects.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return project.getProjectId();
		case 1:
			return project.getTitle();
		case 2:
			return project.getDescription();
		case 3:
			return project.getDueDate();
		case 4:
			return project.isCompleted() ? "Ja" : "Nein";
		case 5:
			return project.getProjectLead().getUserInfo();
		default:
			return null;
		}

	}
}
