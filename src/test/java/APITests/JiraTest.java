package APITests;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import io.restassured.RestAssured;

public class JiraTest {
	
	public static void main(String args[]) throws FileNotFoundException, IOException {
		
		
		
		Properties prop = new Properties();
		prop.load(new FileInputStream("config.properties"));
		
		String base_uri=prop.getProperty("base_uri");
		String email = prop.getProperty("auth.email");
		String token = prop.getProperty("auth.token");
		File file = new File(JiraTest.class.getClassLoader().getResource("Screenshot_1.png").getFile());

		
		//Get Jira Issue details
		RestAssured.baseURI=base_uri;
		
		given()
			.log().all()
			.auth()
			.preemptive()
			.basic(email, token)
			.header("Accept","application/json")
		.when()
			.get("/rest/api/3/issue/10033")
		.then()
			.log().all()
			.assertThat().statusCode(200);
		
		//Add Attachment on Jira
		given()
			.log().all()
			.auth()
			.preemptive()
			.basic(email, token)
			.multiPart("file", file)
			.header("X-Atlassian-Token","no-check")
		.when()
			.post("rest/api/3/issue/10033/attachments")
		.then()
			.log().all()
			.assertThat().statusCode(200);
			
		
		
	}

}
