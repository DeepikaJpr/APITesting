package APITests;
import static io.restassured.RestAssured.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import io.restassured.path.json.JsonPath;

public class AuthorizationTest {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		Properties prop = new Properties();
		prop.load(new FileInputStream("config.properties"));
		
		String clientSecret = prop.getProperty("client_secret");
		

		String response=
			given()
				.param("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
				.param("client_secret",clientSecret )
				.param("grant_type", "client_credentials")
				.param("scope", "trust")
				
			.when().post("https://rahulshettyacademy.com/oauthapi/oauth2/resourceOwner/token").asString();
		
		System.out.println(response);
		
		JsonPath jspath = new JsonPath(response);
		String token= jspath.getString("access_token");
		
		String response1=
		given()
			.queryParam("access_token", token)
		.when()
			.get("https://rahulshettyacademy.com/oauthapi/getCourseDetails").asString();
		
		System.out.println("-------------------"+response1+"----------------------");
	}

}
