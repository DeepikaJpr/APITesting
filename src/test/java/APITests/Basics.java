package APITests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.Assert;
import org.testng.annotations.Test;

import files.ReUsableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class Basics {

	ReUsableMethods reUsableMethods = new ReUsableMethods();

	static{RestAssured.baseURI="https://rahulshettyacademy.com";}
	String placeId,newAddress;

	// Add Place and get Json response
	@Test
	public void addPlace()throws IOException {
		String AddPlaceResponse = given().log().all().queryParam("key", "qaclick123")
				.header("Content-Type", "application/json")
				.body(new String(Files.readAllBytes(Paths.get(
						"C:\\Users\\HP\\eclipse-workspace\\APIFramework\\src\\test\\resources\\staticPlayloads\\addPlace.json"))))
				.when().post("/maps/api/place/add/json").then().log().all().assertThat().statusCode(200)
				.body("scope", equalTo("APP")).header("Server", "Apache/2.4.52 (Ubuntu)").extract().response()
				.asString();

		// JsonPath js = new JsonPath(AddPlaceResponse);
		JsonPath js = reUsableMethods.rawToJson(AddPlaceResponse);
		placeId = js.getString("place_id");
		System.out.println(placeId);
	}

	// Update Place
	@Test(dependsOnMethods = "addPlace")
	public void updatePlace() {
		newAddress = "71 Summer walk, USA";
		given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
				.body("{\r\n" + "\"place_id\":\"" + placeId + "\",\r\n" + "\"address\":\"" + newAddress + "\",\r\n"
						+ "\"key\":\"qaclick123\"\r\n" + "}")
				.when().put("/maps/api/place/update/json").then().log().all().assertThat().statusCode(200)
				.body("msg", equalTo("Address successfully updated"));
	}

	// get Place
	@Test(dependsOnMethods = "updatePlace")
		public void getPlace() {
		String getPlaceResponse = given().queryParam("key", "qaclick123").queryParam("place_id", placeId).when()
				.get("/maps/api/place/get/json").then().log().all().assertThat().statusCode(200).extract().response()
				.asString();

		JsonPath js1 = reUsableMethods.rawToJson(getPlaceResponse);
		String actualAddress = js1.getString("address");
		System.out.println(actualAddress);
		Assert.assertEquals(actualAddress, newAddress);
	}

		// delete Place
	@Test(dependsOnMethods = "getPlace")
	public void deletePlace() {

		given().queryParam("key", "qaclick123")
		.body("{\n" +
			      "\"place_id\":\"" + placeId + "\"\n" +
			      "}")
				.when().delete("/maps/api/place/delete/json").then().log().all().assertThat().statusCode(200);

	}

}
