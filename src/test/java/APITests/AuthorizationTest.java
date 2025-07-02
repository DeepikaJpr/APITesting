package APITests;
import static io.restassured.RestAssured.given;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.testng.Assert;

import io.restassured.path.json.JsonPath;
import pojo.Api;
import pojo.GetCourses;
import pojo.WebAutomation;

public class AuthorizationTest {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		Properties prop = new Properties();
		prop.load(new FileInputStream("config.properties"));
		
		String clientSecret = prop.getProperty("client_secret");
		
		String[] webAutomationcoursesTitleExpected= {"Selenium Webdriver Java","Cypress","Protractor"};
		
		//Post OAuth params and get access_token in response to access the Locked API
		String response=
			given().log().all()
				.param("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
				.param("client_secret",clientSecret )
				.param("grant_type", "client_credentials")
				.param("scope", "trust")
				
			.when().post("https://rahulshettyacademy.com/oauthapi/oauth2/resourceOwner/token").asString();
		
		System.out.println(response);
		
		JsonPath jspath = new JsonPath(response);
		String token= jspath.getString("access_token");
		
		
		//Get getCourseDetails using OAuth Token - access_token
		GetCourses response1=
		given().log().all()
			.queryParam("access_token", token)
		.when()
			.get("https://rahulshettyacademy.com/oauthapi/getCourseDetails").as(GetCourses.class);
		
		
		
		//Print Course - `SoapUI Webservices testing` price
		List<Api> apiCourses= response1.getCourses().getApi();
		
		for(int i=0;i<apiCourses.size();i++)
		{
			if(apiCourses.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing"))
			{
				System.out.println("--------"+apiCourses.get(i).getPrice()+"--------------------");
			}
		}
		
		
		
		//Print all Web Automation Courses name
		List<WebAutomation> webAutomationsCourses = response1.getCourses().getWebAutomation();
		
		ArrayList<String> wATA=new ArrayList<String>();
		
		for(int j=0;j<webAutomationsCourses.size();j++)
		{
			wATA.add(webAutomationsCourses.get(j).getCourseTitle());
			System.out.println(webAutomationsCourses.get(j).getCourseTitle());
			
		}
		
		List<String> wATE= Arrays.asList(webAutomationcoursesTitleExpected);
		Assert.assertTrue(wATA.equals(wATE));
		
		
		
	}

}
