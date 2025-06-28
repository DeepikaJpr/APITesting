package APITests;
import static org.testng.Assert.assertEquals;

import files.Payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {

	public static void main(String[] args) {

		JsonPath js = new JsonPath(Payload.coursePrice());

		// Print no. of courses returned by API
		int countCourses = js.getInt("courses.size()");
		System.out.println(countCourses);

		// Print purchase amount
		int purchaseAmount = js.getInt("dashboard.purchaseAmount");
		System.out.println(purchaseAmount);

		// Print title of the first course
		String firstCourse = js.getString("courses[0].title");
		System.out.println(firstCourse);

		// Print all courses title and their respective prices
		for (int i = 0; i < countCourses; i++) {
			System.out.println(js.getString("courses[" + i + "].title"));
			System.out.println(js.getInt("courses[" + i + "].price"));
		}

		// Print number of copies sold by 'RPA' course
		for (int j = 0; j < countCourses; j++) {
			if (js.getString("courses[" + j + "].title").equalsIgnoreCase("RPA")) {
				System.out.println(js.getInt("courses[" + j + "].copies"));
				break;
			}
		}

		// Verify sum of all courses prices matches with Purchase amount
		int totalPriceCount =0;
		int coursePrice=0;

		for (int k = 0; k < countCourses; k++) {
			coursePrice=(js.getInt("courses["+k+"].price"))*(js.getInt("courses["+k+"].copies"));
			totalPriceCount= totalPriceCount+ coursePrice;
		}
		assertEquals(purchaseAmount, totalPriceCount);

	}

}
