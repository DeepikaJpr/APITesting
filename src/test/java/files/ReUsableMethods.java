package files;

import io.restassured.path.json.JsonPath;

public class ReUsableMethods {
	
	public JsonPath rawToJson(String response)
	{
		JsonPath js = new JsonPath(response);
		return js;
		
	}

}
