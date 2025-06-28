package APITests;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import files.Payload;
import files.ReUsableMethods;

import static io.restassured.RestAssured.*;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class DynamicJson {

	ReUsableMethods reuse = new ReUsableMethods();

	// Add Book
	//@Test(dataProvider = "Books Data")
	public String addBook(String isbn, String aisle) {
		RestAssured.baseURI = "https://rahulshettyacademy.com";

		String addBookResponse = given().log().all().header("Content-Type", "text/plain")
				.body(Payload.AddBook(isbn, aisle)).when().post("/Library/Addbook.php").then().log().all()
				.statusCode(200).extract().response().asString();

		JsonPath js = reuse.rawToJson(addBookResponse);
		String actualMsgTest = js.getString("Msg");
		String expectedMsgTest = "successfully added";

		String bookId = js.getString("ID");
		System.out.println(bookId);
		assertEquals(actualMsgTest, expectedMsgTest);
		return bookId;
	}

	@DataProvider(name = "Books Data")
	public Object[][] getData() {
		return new Object[][] { { "dsfwws", "68646" }, { "wsasqe", "658466" }, { "wwwl", "4656416" }, { "ettqwq", "899663" } };
	}

	// Delete Book

	@Test(dataProvider = "Books Data")
	public void deleteBook(String isbn,String aisle) {
		String bookId= addBook(isbn, aisle);
		
		given().header("Content-Type", "text/plain").body(Payload.deleteBook(bookId)).when().delete("/Library/DeleteBook.php")
				.then().log().all().assertThat().statusCode(200);
	}

}
