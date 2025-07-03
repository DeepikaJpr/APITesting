package APITests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import org.testng.Assert;

import ECommerce_pojo.LoginRequest;
import ECommerce_pojo.LoginResponse;
import ECommerce_pojo.Orders;
import ECommerce_pojo.OrdersRequestMain;
import ECommerce_pojo.OrdersResponse;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;

public class ECommerceAPITest 
{

	public static void main(String[] args) throws FileNotFoundException, IOException 
	{
		
		//LOGIN TESTCASE
		RequestSpecification reqSpecs = new RequestSpecBuilder()
											.setBaseUri("https://rahulshettyacademy.com/")
											.setContentType(ContentType.JSON).build();
		
		//Reading critical details from properties
		Properties prop = new Properties();
		prop.load(new FileInputStream("config.properties"));
		
		
		LoginRequest lreq = new LoginRequest();
		lreq.setUserEmail(prop.getProperty("ecomLogin"));
		lreq.setUserPassword(prop.getProperty("ecomPassword"));
		
		
		LoginResponse lres =
		given()
			.log().all()
			.spec(reqSpecs)
			.body(lreq)
		.when()
			.post("api/ecom/auth/login")
		.then().extract().response()
		.as(LoginResponse.class);
		
		System.out.println("=================="+lres.getToken()+"=========================");
		String token = lres.getToken();
		
		System.out.println("====================="+lres.getUserId()+"=================================");
		String userId=lres.getUserId();
		System.out.println("====================="+lres.getMessage()+"=================================");
		
		Assert.assertTrue(lres.getMessage().equals("Login Successfully"));
		
		//CREATE PRODUCT
		RequestSpecification addProductReq= new RequestSpecBuilder()
												.setBaseUri("https://rahulshettyacademy.com")
												.addHeader("Authorization", token)
												.build();
		
		//File file = new File(	ECommerceAPITest.class.getClassLoader().getResource("Screenshot_1.png").getFile());
		
		File file = new File("src/test/resources/Screenshot_1.png"); // works locally
		System.out.println("====FILE Exists=========="+file.exists()+"==========");

		
		String addProductResponse =
		given()
			.log().all()
			.spec(addProductReq)
			.param("productName", "Test123")
			.param("productAddedBy", userId)
			.param("productCategory", "fashion")
			.param("productSubCategory", "shirts")
			.param("productPrice", "9999")
			.param("productDescription", "Addias Originals")
			.param("productFor","women")
			.multiPart("productImage", file)
		.when()
			.post("api/ecom/product/add-product")
		.then()
			.log().all().extract().response().asString();
		
		JsonPath js = new JsonPath(addProductResponse);
		String productId = js.get("productId");
		
		System.out.println("========================"+js.getString("productId")+"======================================");
		System.out.println("========================"+js.getString("message")+"=========================================");
		
		Assert.assertTrue(js.get("message").equals("Product Added Successfully"));
		
		//CREATE ORDER
		
		
		Orders oreqLvl1 = new Orders();
		oreqLvl1.setCountry("India");
		oreqLvl1.setProductOrderedId(productId);
		
		OrdersRequestMain oreqMain = new OrdersRequestMain();
		oreqMain.setOrders(Arrays.asList(oreqLvl1));
		
		
		OrdersResponse ores =
		given()
			.log().all()
			.spec(addProductReq)
			.header("Content-Type","application/json")
			.body(oreqMain)
		.when()
			.post("api/ecom/order/create-order")
		.then()
			.log().all()
			.extract().response().as(OrdersResponse.class);
		
		System.out.println("========================="+ores.getMessage()+"=========================");
		System.out.println("========================="+ores.getProductOrderId().get(0)+"==================");
		
		System.out.println("========================="+ores.getOrders().get(0)+"==========================");
		String orderId= ores.getOrders().get(0);
		
		Assert.assertTrue(ores.getMessage().equals("Order Placed Successfully"));
		
		//GET ORDER DETAILS
		given()
			.log().all()
			.spec(addProductReq)
			.queryParam("id", orderId)
		.when()
			.get("api/ecom/order/get-orders-details")
		.then()
			.log().all()
			.assertThat().body("message",equalTo("Orders fetched for customer Successfully"));
		
		//DELETE PRODUCT
		
		
		given()
			.log().all()
			.spec(addProductReq)
			.pathParam("productId", productId)
		.when()
			.delete("api/ecom/product/delete-product/{productId}")
		.then()
			.log().all()
			.assertThat()
				.statusCode(200)
				.body("message",equalTo("Product Deleted Successfully"));
		
		

	}

}

