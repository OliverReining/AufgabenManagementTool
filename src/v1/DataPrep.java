package v1;

import java.util.List;
import java.util.Map;

public class DataPrep {

	public static Object[][] prepareProjectData(int userId) {
		
	    List<Map<String, Object>> projects = Analytics.getProjectNamesAndTaskCount(userId);

	    // Daten für die JTable vorbereiten
	    Object[][] data = new Object[projects.size()][3];
	    for (int i = 0; i < projects.size(); i++) {
	        Map<String, Object> project = projects.get(i);
	        data[i][0] = project.get("Projekte");
	        data[i][1] = project.get("Aufgaben");
	        data[i][2] = project.get("Projektleiter");
	    }
		return data;
	}

}
