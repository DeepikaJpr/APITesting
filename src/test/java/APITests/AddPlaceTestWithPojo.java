package APITests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.util.ArrayList;

import org.testng.annotations.Test;

import files.ReUsableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import pojo_AddPlace.AddPlacePojo;
import pojo_AddPlace.Location;

public class AddPlaceTestWithPojo {

	ReUsableMethods reUsableMethods = new ReUsableMethods();

	static {
		RestAssured.baseURI = "https://rahulshettyacademy.com";
	}
	String placeId, newAddress;

	// Add Place and get Json response
	@Test
	public void addPlace() throws IOException {
		
		
		AddPlacePojo app = new AddPlacePojo();
		app.setAccuracy(50);
		app.setAddress("29, side layout, cohen 09");
		app.setLanguage("French-IN");
		app.setName("Frontline house");
		app.setPhone_number("(+91) 983 893 3937");
		app.setWebsite("http://google.com");
		
		
		Location lo = new Location();
		lo.setLat(-38.383494);
		lo.setLng(33.427362);
		
		app.setLocation(lo);
		
		ArrayList<String> set_Types = new ArrayList<String>();
		set_Types.add("shoe park");
		set_Types.add("shop");
		
		app.setTypes(set_Types);
		
		
		
		
		String AddPlaceResponse = given().log().all().queryParam("key", "qaclick123")
				.header("Content-Type", "application/json")
				.body(app)
				.when().post("/maps/api/place/add/json").then().log().all().assertThat().statusCode(200)
				.body("scope", equalTo("APP")).header("Server", "Apache/2.4.52 (Ubuntu)").extract().response()
				.asString();

		// JsonPath js = new JsonPath(AddPlaceResponse);
		JsonPath js = reUsableMethods.rawToJson(AddPlaceResponse);
		placeId = js.getString("place_id");
		System.out.println(placeId);
	}

}
