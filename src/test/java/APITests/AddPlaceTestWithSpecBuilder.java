package APITests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.util.ArrayList;

import org.testng.annotations.Test;

import files.ReUsableMethods;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo_AddPlace.AddPlacePojo;
import pojo_AddPlace.Location;

public class AddPlaceTestWithSpecBuilder {

	ReUsableMethods reUsableMethods = new ReUsableMethods();

	static {
		RestAssured.baseURI = "https://rahulshettyacademy.com";
	}
	String placeId, newAddress;

	// Add Place and get Json response
	@Test
	public void addPlace() throws IOException {
		
		//Serializing the request body with simple set  - String / Int
		AddPlacePojo app = new AddPlacePojo();
		app.setAccuracy(50);
		app.setAddress("29, side layout, cohen 09");
		app.setLanguage("French-IN");
		app.setName("Frontline house");
		app.setPhone_number("(+91) 983 893 3937");
		app.setWebsite("http://google.com");
		
		//Serializing the request body with with set - double
		Location lo = new Location();
		lo.setLat(-38.383494);
		lo.setLng(33.427362);
		
		//loading the nested json with object of the class where request body is already set
		app.setLocation(lo);
		
		//setting request body with - List<String>
		ArrayList<String> set_Types = new ArrayList<String>();
		set_Types.add("shoe park");
		set_Types.add("shop");
		
		app.setTypes(set_Types);
		
		
		//building common steps in request to reutilize in all tests
		RequestSpecification reqSpecs = new RequestSpecBuilder()
											.setContentType(ContentType.JSON)
											.addQueryParam("key", "qaclick123")
											.build();
		
		//building common steps in response to reutilize in all tests
		ResponseSpecification resSpecs = new ResponseSpecBuilder()
												.expectHeader("Server", "Apache/2.4.52 (Ubuntu)")
												.expectStatusCode(200)
												.build();
		
		String AddPlaceResponse = 
								given().log().all().spec(reqSpecs)
								.body(app)
								
								.when().post("/maps/api/place/add/json")
								
								.then().log().all().spec(resSpecs)
								.body("scope", equalTo("APP")).extract().response()
								.asString();

		// JsonPath js = new JsonPath(AddPlaceResponse);
		JsonPath js = reUsableMethods.rawToJson(AddPlaceResponse);
		placeId = js.getString("place_id");
		System.out.println("=============="+placeId+"======================");
	}

}
