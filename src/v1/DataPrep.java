package v1;

import java.util.List;
import java.util.Map;

public class DataPrep {

	public static Object[][] listMapInObject(List<Map<String, Object>> List, String[] keys) {

		Object[][] data = new Object[List.size()][3];
		for (int i = 0; i < List.size(); i++) {
			Map<String, Object> list = List.get(i);
			for (int j = 0; j < keys.length; j++) {
				data[i][j] = list.get(keys[j]);
			}
		}
		return data;
	}

}
