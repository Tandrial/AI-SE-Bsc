package uni.dc.util;

import org.json.JSONArray;
import org.json.JSONObject;

import uni.dc.model.Traffic;

public class NetworkParser {

	public Traffic loadFromFile() {
		return null;
	}

	public void saveToFile(Traffic traffic) {

	}

	public static void main(String[] args) {
		String str = "{ \"name\": \"Alice\", \"age\": 20 }";
		JSONObject obj = new JSONObject(str);
		String n = obj.getString("name");
		int a = obj.getInt("age");
		System.out.println(n + " " + a); 

		str = "{ \"number\": [3, 4, 5, 6] }";
		obj = new JSONObject(str);
		JSONArray arr = obj.getJSONArray("number");
		for (int i = 0; i < arr.length(); i++)
			System.out.println(arr.getInt(i));
	}

}
