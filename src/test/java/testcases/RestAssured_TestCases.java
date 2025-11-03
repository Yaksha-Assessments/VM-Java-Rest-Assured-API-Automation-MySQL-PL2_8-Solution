package testcases;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.security.SecureRandom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import rest.ApiUtil;
import rest.CustomResponse;

//import rest.ApiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestAssured_TestCases {
	private static final Logger logger = LoggerFactory.getLogger(RestAssured_TestCases.class);
	private static String baseUrl;
	private static String username;
	private static String password;
	private static String cookieValue = null;
	private ApiUtil apiUtil;
	private int userIdToDelete;
	private int userIdToDelete1;
	private int userId;
	private int idBefore;

	private String apiUtilPath = System.getProperty("user.dir") + "\\src\\main\\java\\rest\\ApiUtil.java";
	private String excelPath = System.getProperty("user.dir") + "\\src\\main\\resources\\TestData.xlsx";

	/**
	 * @BeforeClass method to perform login via Selenium and retrieve session cookie
	 *              for authenticated API calls.
	 * 
	 *              Steps: 1. Setup ChromeDriver using WebDriverManager. 2. Launch
	 *              browser and open the OrangeHRM login page. 3. Perform login with
	 *              provided username and password. 4. Wait for login to complete
	 *              and extract the 'orangehrm' session cookie. 5. Store the cookie
	 *              value to be used in API requests. 6. Quit the browser session.
	 * 
	 *              Throws: - InterruptedException if thread sleep is interrupted. -
	 *              RuntimeException if the required session cookie is not found.
	 */

	@Test(priority = 0, groups = { "PL2" }, description = "Login to OrangeHRM and retrieve session cookie")
	public void loginWithSeleniumAndGetCookie() throws InterruptedException {
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();

		apiUtil = new ApiUtil();
		baseUrl = apiUtil.getBaseUrl();
		username = apiUtil.getUsername();
		password = apiUtil.getPassword();

		driver.get(baseUrl + "/web/index.php/auth/login");
		Thread.sleep(3000); // Wait for page load

		// Login to the app
		driver.findElement(By.name("username")).sendKeys(username);
		driver.findElement(By.name("password")).sendKeys(password);
		driver.findElement(By.cssSelector("button[type='submit']")).click();
		Thread.sleep(6000); // Wait for login

		// Extract cookie named "orangehrm"
		Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();
		for (org.openqa.selenium.Cookie cookie : cookies) {
			if (cookie.getName().equals("orangehrm")) {
				cookieValue = cookie.getValue();
				break;
			}
		}

		driver.quit();

		if (cookieValue == null) {
			throw new RuntimeException("orangehrm cookie not found after login");
		}

		io.restassured.RestAssured.useRelaxedHTTPSValidation();
	}

	@Test(priority = 1, dependsOnMethods = "loginWithSeleniumAndGetCookie", groups = {
			"PL2" }, description = "1. Define the endpoint to fetch holiday data for the year 2025\n"
					+ "2. Send a GET request to '/web/index.php/api/v2/leave/holidays' with a valid cookie\n"
					+ "3. Validate if the implementation uses correct RestAssured steps (given, cookie, get, response)\n"
					+ "4. Print the response status code and body for verification\n"
					+ "5. Assert the status code is 200 and implementation is correct")
	public void GetHolidayData() throws IOException {
		String endpoint = "/web/index.php/api/v2/leave/holidays?fromDate=2025-01-01&toDate=2025-12-31";

		CustomResponse customResponse = apiUtil.GetHolidayData(endpoint, cookieValue, null);

		// Step 1: Validate that method uses proper Rest Assured calls
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "GetHolidayData",
				List.of("given", "cookie", "get", "response"));

		Assert.assertTrue(isImplementationCorrect,
				"GetHolidayData must be implemented using RestAssured methods only!");

		// Step 3: Validate status code
		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200.");

		// Step 4: Validate status field
		Assert.assertEquals(customResponse.getStatus(), "HTTP/1.0 200 OK", "Status should be OK.");

		// Step 5: Validate id, name, and date fields
		List<Object> itemIds = customResponse.getIds(); // id
		List<Object> itemNames = customResponse.getNames(); // name
		List<Object> itemDates = customResponse.getDates(); // date

		Assert.assertFalse(itemIds.isEmpty(), "ID list should not be empty.");
		Assert.assertFalse(itemNames.isEmpty(), "Name list should not be empty.");
		Assert.assertFalse(itemDates.isEmpty(), "Date list should not be empty.");

		for (int i = 0; i < itemIds.size(); i++) {
			Assert.assertNotNull(itemIds.get(i), "ID at index " + i + " should not be null.");
			Assert.assertNotNull(itemNames.get(i), "Name at index " + i + " should not be null.");
			Assert.assertNotNull(itemDates.get(i), "Date at index " + i + " should not be null.");
		}

		// Step 6: Print for debug
		System.out.println("Holiday API Response:");
		customResponse.getResponse().prettyPrint();
	}

	// Test Case 02

	@Test(priority = 2, dependsOnMethods = "loginWithSeleniumAndGetCookie", groups = {
			"PL2" }, description = "1. Define the endpoint to retrieve holiday details for the year 2025\n"
					+ "2. Send a GET request to '/web/index.php/api/v2/leave/holidays' using a valid cookie\n"
					+ "3. Validate whether the method contains RestAssured steps like given, cookie, get, and response\n"
					+ "4. Print and verify the response status code and response body\n"
					+ "5. Assert that the response status code is 200 and implementation is as expected")

	public void GetLeaveData() throws IOException {
		String endpoint = "/web/index.php/api/v2/leave/holidays?fromDate=2025-01-01&toDate=2025-12-31";

		CustomResponse customResponse = apiUtil.GetLeaveData(endpoint, cookieValue, null);

		// Step 1: Validate that method uses proper Rest Assured calls
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "GetLeaveData",
				List.of("given", "cookie", "get", "response"));

		Assert.assertTrue(isImplementationCorrect, "GetLeaveData must be implemented using RestAssured methods only!");

		// Step 2: Validate structure of response
		Assert.assertTrue(TestCodeValidator.validateResponseFields("GetLeaveData", customResponse),
				"Response must contain all required fields (id, name, date)");

		// Step 3: Validate status code
		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200.");

		// Step 4: Validate status field
		Assert.assertEquals(customResponse.getStatus(), "HTTP/1.0 200 OK", "Status should be OK.");

		// Step 5: Validate id, name, and date fields
		List<Object> itemIds = customResponse.getIds(); // id
		List<Object> itemNames = customResponse.getNames(); // name
		List<Object> itemDates = customResponse.getDates(); // date

		Assert.assertFalse(itemIds.isEmpty(), "ID list should not be empty.");
		Assert.assertFalse(itemNames.isEmpty(), "Name list should not be empty.");
		Assert.assertFalse(itemDates.isEmpty(), "Date list should not be empty.");

		for (int i = 0; i < itemIds.size(); i++) {
			Assert.assertNotNull(itemIds.get(i), "ID at index " + i + " should not be null.");
			Assert.assertNotNull(itemNames.get(i), "Name at index " + i + " should not be null.");
			Assert.assertNotNull(itemDates.get(i), "Date at index " + i + " should not be null.");
		}

		// Step 6: Print for debug
		System.out.println("GetLeaveData API Response:");
		customResponse.getResponse().prettyPrint();
	}

	// Test Case 03

	@Test(priority = 3, dependsOnMethods = "loginWithSeleniumAndGetCookie", groups = {
			"PL2" }, description = "1. Define the endpoint to get employee count\n"
					+ "2. Send a GET request to '/web/index.php/api/v2/pim/employees/count' with a valid cookie\n"
					+ "3. Validate presence of RestAssured steps: given, cookie, get, and response\n"
					+ "4. Print and verify the status code and response body\n"
					+ "5. Assert the response status code is 200 and implementation is correct")

	public void GetEmpCount() throws IOException {

		String endpoint = "/web/index.php/api/v2/pim/employees/count";
		CustomResponse customResponse = apiUtil.GetEmpCount(endpoint, cookieValue, null);
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "GetEmpCount",
				List.of("given", "cookie", "get", "response"));

		Assert.assertTrue(isImplementationCorrect, "GetEmpCount must be implemented using RestAssured methods only!");

		Assert.assertTrue(TestCodeValidator.validateResponseFields("GetEmpCount", customResponse),
				"Response must contain all required fields (count)");

		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200.");

		Assert.assertEquals(customResponse.getStatus(), "HTTP/1.0 200 OK", "Status should be OK.");

		int Empcount = customResponse.getEmpCount();
		System.out.println(Empcount);

		Assert.assertNotEquals(endpoint, 0, "The employee count is 0!");

		// for (int i = 0; i < Empcount.size(); i++) {
		Assert.assertNotNull(Empcount, "Empoyee Count should not be null.");
		// }

		System.out.println("GetEmpCount API Response:");
		customResponse.getResponse().prettyPrint();
	}

	@Test(priority = 4, dependsOnMethods = "loginWithSeleniumAndGetCookie", groups = {
			"PL2" }, description = "1. Define the endpoint to retrieve all leave types with no limit\n"
					+ "2. Send a GET request to '/web/index.php/api/v2/leave/leave-types?limit=0' using a valid session cookie\n"
					+ "3. Validate implementation contains: given, cookie, get, and response\n"
					+ "4. Print and verify status code and response body\n"
					+ "5. Assert that the status code is 200 and implementation is correct")

	public void GetLeaveType() throws IOException {
		String endpoint = "/web/index.php/api/v2/leave/leave-types?limit=0";

		CustomResponse customResponse = apiUtil.GetLeaveType(endpoint, cookieValue, null);

		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "GetLeaveType",
				List.of("given", "cookie", "get", "response"));

		Assert.assertTrue(isImplementationCorrect, "GetLeaveType must be implemented using RestAssured methods only!");

		Assert.assertTrue(TestCodeValidator.validateResponseFields("GetLeaveType", customResponse),
				"Response must contain all required fields (id, name, date)");

		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200.");

		// Step 4: Validate status field
		Assert.assertEquals(customResponse.getStatus(), "HTTP/1.0 200 OK", "Status should be OK.");

		List<Object> itemIds = customResponse.getIds();
		List<Object> itemNames = customResponse.getNames();

		List<Object> itemDelete = customResponse.getDeletes();
		List<Object> itemSituationals = customResponse.getSituationals();

		Assert.assertFalse(itemIds.isEmpty(), "ID list should not be empty.");
		Assert.assertFalse(itemNames.isEmpty(), "Name list should not be empty.");
		Assert.assertFalse(itemDelete.isEmpty(), "Date list should not be empty.");
		Assert.assertFalse(itemSituationals.isEmpty(), "Date list should not be empty.");

		for (int i = 0; i < itemIds.size(); i++) {
			Assert.assertNotNull(itemIds.get(i), "ID at index " + i + " should not be null.");
			Assert.assertNotNull(itemNames.get(i), "Name at index " + i + " should not be null.");
			Assert.assertNotNull(itemDelete.get(i), "Name at index " + i + " should not be null.");
			Assert.assertNotNull(itemSituationals.get(i), "Name at index " + i + " should not be null.");
		}

		System.out.println("GetLeaveType API Response:");
		customResponse.getResponse().prettyPrint();
	}

	@Test(priority = 5, dependsOnMethods = "loginWithSeleniumAndGetCookie", groups = {
			"PL2" }, description = "1. Define the endpoint to retrieve all leave types with no limit\n"
					+ "2. Send a GET request to '/web/index.php/api/v2/leave/leave-types?limit=0' using a valid session cookie\n"
					+ "3. Validate implementation contains: given, cookie, get, and response\n"
					+ "4. Print and verify status code and response body\n"
					+ "5. Assert that the status code is 200 and implementation is correct")

	public void GetUsageReport() throws IOException {
		String endpoint = "/web/index.php/api/v2/leave/reports?name=my_leave_entitlements_and_usage";

		CustomResponse customResponse = apiUtil.GetUsageReport(endpoint, cookieValue, null);

		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "GetUsageReport",
				List.of("given", "cookie", "get", "response"));

		Assert.assertTrue(isImplementationCorrect,
				"GetUsageReport must be implemented using RestAssured methods only!");

		Assert.assertTrue(TestCodeValidator.validateResponseFields("GetUsageReport", customResponse),
				"Response must contain all required fields (name, prop, size, pin, cellProperties)");

		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200.");
		Assert.assertEquals(customResponse.getStatus(), "HTTP/1.0 200 OK", "Status should be OK.");

		List<Object> names = customResponse.getNames();
		List<Object> props = customResponse.getProps();
		List<Object> sizes = customResponse.getSizes();
		List<Object> pins = customResponse.getPins();
		List<Object> cellProperties = customResponse.getCellProperties();

		// Assert essential lists are not empty
		Assert.assertFalse(names.isEmpty(), "Name list should not be empty.");
		Assert.assertFalse(props.isEmpty(), "Prop list should not be empty.");
		Assert.assertFalse(sizes.isEmpty(), "Size list should not be empty.");
		Assert.assertFalse(cellProperties.isEmpty(), "CellProperties list should not be empty.");

		for (int i = 0; i < names.size(); i++) {
			Assert.assertNotNull(names.get(i), "Name at index " + i + " should not be null.");
			Assert.assertNotNull(props.get(i), "Prop at index " + i + " should not be null.");
			Assert.assertNotNull(sizes.get(i), "Size at index " + i + " should not be null.");
		}

		// ✅ Print all pin values
		System.out.println("Pin Values:");
		for (int i = 0; i < pins.size(); i++) {
			System.out.println("Pin at index " + i + ": " + pins.get(i));
		}

		System.out.println("✅ GetUsageReport API Response:");
		customResponse.getResponse().prettyPrint();
	}

	@Test(priority = 6, dependsOnMethods = "loginWithSeleniumAndGetCookie", groups = {
			"PL2" }, description = "1. Define the endpoint to retrieve all Vacancies"
					+ "2. Send a GET request to '/web/index.php/api/v2/recruitment/vacancies?limit=50&offset=0&sortField=vacancy.name&sortOrder=ASC&model=detailed' using a valid session cookie\n"
					+ "3. Validate implementation contains: given, cookie, get, and response\n"
					+ "4. Print and verify status code and response body\n"
					+ "5. Assert that the status code is 200 and implementation is correct")
	public void GetVacancies() throws IOException {
		String endpoint = "/web/index.php/api/v2/recruitment/vacancies?limit=50&offset=0&sortField=vacancy.name&sortOrder=ASC&model=detailed";

		CustomResponse customResponse = apiUtil.GetVacancies(endpoint, cookieValue, null);

		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "GetVacancies",
				List.of("given", "cookie", "get", "response"));

		Assert.assertTrue(isImplementationCorrect, "GetVacancies must be implemented using RestAssured methods only!");

		Assert.assertTrue(TestCodeValidator.validateResponseFields("GetVacancies", customResponse),
				"Response must contain all required fields (name, id, status, jobtitle)");

		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200.");
		Assert.assertEquals(customResponse.getStatusLine(), "HTTP/1.0 200 OK", "Status should be OK.");

		List<Object> itemIds = customResponse.getIds();
		List<Object> itemNames = customResponse.getNames();

		Assert.assertFalse(itemIds.isEmpty(), "ID list should not be empty.");
		Assert.assertFalse(itemNames.isEmpty(), "Name list should not be empty.");

		for (int i = 0; i < itemIds.size(); i++) {
			Assert.assertNotNull(itemIds.get(i), "ID at index " + i + " should not be null.");
			Assert.assertNotNull(itemNames.get(i), "Name at index " + i + " should not be null.");
		}

	}

	@Test(priority = 7, dependsOnMethods = "loginWithSeleniumAndGetCookie", groups = {
			"PL2" }, description = "1. Define the endpoint to retrieve all Job Titles\n"
					+ "2. Send a GET request to '/web/index.php/api/v2/admin/job-titles?limit=0' using a valid session cookie\n"
					+ "3. Validate implementation contains: given, cookie, get, and response\n"
					+ "4. Print and verify status code and response body\n"
					+ "5. Assert that the status code is 200 and implementation is correct")
	public void GetJobTitles() throws IOException {
		String endpoint = "/web/index.php/api/v2/admin/job-titles?limit=0";

		CustomResponse customResponse = apiUtil.GetJobTitles(endpoint, cookieValue, null);

		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "GetJobTitles",
				List.of("given", "cookie", "get", "response"));
//		Assert.assertTrue(TestCodeValidator.validateResponseFields("GetJobTitles", customResponse),
//				"Response must contain all required fields (name, id, status, jobtitle)");

		Assert.assertTrue(isImplementationCorrect, "GetJobTitles must be implemented using RestAssured methods only!");

		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200.");
		Assert.assertEquals(customResponse.getStatusLine(), "HTTP/1.0 200 OK", "Status should be OK.");

		List<Object> itemIds = (List<Object>) customResponse.getId();
		List<Object> itemTitles = customResponse.getNames();

		Assert.assertNotNull(itemTitles, "Title list should not be null.");

		for (int i = 0; i < itemIds.size(); i++) {
			Assert.assertNotNull(itemIds.get(i), "ID at index " + i + " should not be null.");
			Assert.assertNotNull(itemTitles.get(i), "Title at index " + i + " should not be null.");
		}
	}

	@Test(priority = 8, dependsOnMethods = "loginWithSeleniumAndGetCookie", groups = {
			"PL2" }, description = "1. Fetch the cookie from Selenium method and empNumber\n"
					+ "2. Send a GET request to '/web/index.php/api/v2/pim/employees/{empNumber}/personal-details' with a valid cookie\n"
					+ "3. Validate implementation contains: given, cookie, get, and response\n"
					+ "4. Print and verify status code and response body\n"
					+ "5. Assert that the status code is 200 and implementation is correct\n"
					+ "6. Assert that important fields are not null or empty")
	public void GetEmpPersonalDetails() throws IOException {
		JsonPath json = RestAssured.given().cookie("orangehrm", cookieValue) // 🔑 Add valid cookie
				.get("https://yakshahrm.makemylabs.in/orangehrm-5.7/web/index.php/api/v2/pim/employees").jsonPath();

		Integer empNumber = json.getInt("data[0].empNumber");
		String endpoint = "/web/index.php/api/v2/pim/employees/" + empNumber + "/personal-details";

		CustomResponse customResponse = apiUtil.GetEmpPersonalDetails(endpoint, cookieValue, null);

		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath,
				"GetEmpPersonalDetails", List.of("given", "cookie", "get", "response"));
		Assert.assertTrue(isImplementationCorrect,
				"GetEmpPersonalDetails must be implemented using RestAssured methods only!");

		// ✅ Validate HTTP response
		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200.");
		Assert.assertEquals(customResponse.getStatusLine(), "HTTP/1.0 200 OK", "Status should be OK.");

		// ✅ Validate extracted fields
		Assert.assertNotNull(customResponse.getEmpNumber(), "empNumber should not be null.");
		Assert.assertTrue(customResponse.getEmpNumber() > 0, "empNumber should be greater than 0.");

		Assert.assertNotNull(customResponse.getFirstName(), "First Name should not be null.");
		Assert.assertFalse(customResponse.getFirstName().trim().isEmpty(), "First Name should not be empty.");

		Assert.assertNotNull(customResponse.getLastName(), "Last Name should not be null.");
		Assert.assertFalse(((String) customResponse.getLastName()).trim().isEmpty(), "Last Name should not be empty.");

		System.out.println("Employee Number: " + customResponse.getEmpNumber());
		System.out.println("Employee Name: " + customResponse.getFirstName() + " " + customResponse.getLastName());

		System.out.println("GetEmpPersonalDetails API Response:");
		customResponse.getResponse().prettyPrint();
	}

	@Test(priority = 9, dependsOnMethods = "loginWithSeleniumAndGetCookie", groups = {
			"PL2" }, description = "1. Define the endpoint to fetch employee leave data\n"
					+ "2. Send a GET request to '/web/index.php/api/v2/pim/employees/7' with a valid cookie\n"
					+ "3. Validate implementation contains: given, cookie, get, and response\n"
					+ "4. Print and verify status code and response body\n"
					+ "5. Assert that the status code is 200 and implementation is correct\n"
					+ "6. Assert that important fields are not null or empty")
	public void GetEmpData() throws IOException {
		JsonPath json = RestAssured.given().cookie("orangehrm", cookieValue) // 🔑 Add valid cookie
				.get("https://yakshahrm.makemylabs.in/orangehrm-5.7/web/index.php/api/v2/pim/employees").jsonPath();

		Integer empNumber = json.getInt("data[0].empNumber");
		String endpoint = "/web/index.php/api/v2/pim/employees/" + empNumber;

		CustomResponse customResponse = apiUtil.GetEmpData(endpoint, cookieValue, null);

		// ✅ Validate test implementation
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "GetEmpData",
				List.of("given", "cookie", "get", "response"));
		Assert.assertTrue(isImplementationCorrect, "GetEmpData must be implemented using RestAssured methods only!");

		// ✅ Validate HTTP response
		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200.");
		Assert.assertEquals(customResponse.getStatusLine(), "HTTP/1.0 200 OK", "Status should be OK.");

		// ✅ Validate extracted fields (fix: check first element in the lists)
		Assert.assertNotNull(customResponse.getEmpNumbers(), "empNumbers list should not be null.");
		Assert.assertFalse(customResponse.getEmpNumbers().isEmpty(), "empNumbers list should not be empty.");

		Assert.assertNotNull(customResponse.getFirstNames(), "First Names list should not be null.");
		Assert.assertFalse(customResponse.getFirstNames().isEmpty(), "First Names list should not be empty.");

		Assert.assertNotNull(customResponse.getLastNames(), "Last Names list should not be null.");
		Assert.assertFalse(customResponse.getLastNames().isEmpty(), "Last Names list should not be empty.");

		// nationality may not exist in all APIs, check only if present
		if (customResponse.getNationalityName() != null) {
			Assert.assertFalse(customResponse.getNationalityName().trim().isEmpty(),
					"Nationality should not be empty.");
		}

		// ✅ Print results
		System.out.println("Employee Number: " + customResponse.getEmpNumbers().get(0));
		System.out.println(
				"Employee Name: " + customResponse.getFirstNames().get(0) + " " + customResponse.getLastNames().get(0));
		System.out.println("Employee Nationality: " + customResponse.getNationalityName());

		System.out.println("GetEmpData API Response:");
		customResponse.getResponse().prettyPrint();
	}

	@Test(priority = 10, dependsOnMethods = "loginWithSeleniumAndGetCookie", groups = {
			"PL2" }, description = "1. Fetch the cookie from selenium method and empNumber\n"
					+ "2. Send a GET request to '/web/index.php/api/v2/leave/workweek?model=indexed' with a valid cookie\n"
					+ "3. Validate implementation contains: given, cookie, get, and response\n"
					+ "4. Print and verify status code and response body\n"
					+ "5. Assert that the status code is 200 and implementation is correct\n"
					+ "6. Assert that important fields are not null or empty")
	public void GetLeaveWorkWeek() throws IOException {
		String endpoint = "/web/index.php/api/v2/leave/workweek?model=indexed";
		CustomResponse customResponse = apiUtil.GetLeaveWorkWeek(endpoint, cookieValue);

		// ✅ Validate implementation correctness
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "GetLeaveWorkWeek",
				List.of("given", "cookie", "get", "response"));
		Assert.assertTrue(isImplementationCorrect,
				"GetLeaveWorkWeek must be implemented using RestAssured methods only!");

		// ✅ Print response details
		System.out.println("Status Code: " + customResponse.getStatusCode());
		System.out.println("Workweek Data: " + customResponse.getData());
		System.out.println("Response Body: " + customResponse.getResponseBody());
		Map<String, Integer> workWeekData = (Map<String, Integer>) customResponse.getData();

		// ✅ Assertions

		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200");

		Assert.assertNotNull(customResponse.getData(), "Workweek data must not be null");

		Assert.assertFalse(workWeekData.isEmpty(), "Workweek data must not be empty");

		// Ensure all days 0-6 are present in the map
		for (int day = 0; day <= 6; day++) {
			Assert.assertTrue(((Map<String, Integer>) customResponse.getData()).containsKey(String.valueOf(day)),
					"Workweek data must contain key for day: " + day);
			Assert.assertNotNull(customResponse.getData(),
					"Workweek value must not be null for day: " + day);
		}

		Assert.assertNotNull(customResponse.getResponseBody(), "Response body must not be null");
		Assert.assertFalse(customResponse.getResponseBody().isEmpty(), "Response body must not be empty");
	}

	@Test(priority = 11, dependsOnMethods = "loginWithSeleniumAndGetCookie", groups = {
			"PL2" }, description = "Precondition: Null\n" + "1. Generate a unique name\n"
					+ "2. Send POST request to '/web/index.php/api/v2/pim/employees'\n"
					+ "3. Make sure the request contains the request body and cookie\n"
					+ "4. Verify response contains the added name")
	public void PostEmployee() throws IOException {
		// Step 1: Generate unique employeeId and name
		String uniqueName = "Employee_" + System.currentTimeMillis();
		String lastName = "Employee_" + System.currentTimeMillis();

		// Step 2: Create request body
		String body = "{\n" + "  \"empPicture\": null,\n" + "  \"firstName\": \"" + uniqueName + "\",\n"
				+ "  \"lastName\": \"" + java.util.UUID.randomUUID().toString().substring(0, 5) + "\",\n"
				+ "  \"middleName\": \"" + lastName + java.util.UUID.randomUUID().toString().substring(0, 5) + "\"\n"
				+ "}";

		String endpoint = "/web/index.php/api/v2/pim/employees";

		// Step 3: Send POST request
		CustomResponse postResponse = apiUtil.PostEmployee(endpoint, cookieValue, body);

		// ✅ Validate implementation correctness
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "PostEmployee",
				List.of("given", "cookie", "post", "response"));
		Assert.assertTrue(isImplementationCorrect, "PostEmployee must be implemented using RestAssured methods only!");

		// Debugging output
		System.out.println("Request Body: " + body);
		System.out.println("Status Code: " + postResponse.getStatusCode());
		System.out.println("Response Body: " + postResponse.getResponse().asString());

		// Step 4: Assertions
		Assert.assertEquals(postResponse.getStatusCode(), 200, "Expected status code 200 after creating employee");
		Assert.assertTrue(postResponse.getResponse().asString().contains(uniqueName),
				"Response should contain the newly added employee name");
		Assert.assertTrue(postResponse.containsText(uniqueName), "Employee name not found in response");
	}

	@Test(priority = 12, dependsOnMethods = "loginWithSeleniumAndGetCookie", groups = {
			"PL1" }, description = "1. Fetch employment status ID using GET '/web/index.php/api/v2/admin/employment-statuses'\n"
					+ "2. Construct endpoint '/web/index.php/api/v2/admin/employment-statuses/{id}'\n"
					+ "3. Send a PUT request with a valid cookie and body containing a new name\n"
					+ "4. Print request/response details\n"
					+ "5. Assert response code is 200 and updated name is reflected in the response")
	public void putEmployeeDeatils() throws IOException {
		JsonPath json = RestAssured.given().cookie("orangehrm", cookieValue) // 🔑 Add valid cookie
				.get("https://yakshahrm.makemylabs.in/orangehrm-5.7/web/index.php/api/v2/pim/employees").jsonPath();

		Integer empNumber = json.getInt("data[0].empNumber");
		String requestBody = "{\n" + "  \"firstName\": \"John123\",\n" + "  \"lastName\": \"abcde\",\n"
				+ "  \"middleName\": \"fghij\"\n" + "}";

		String endpoint = "/web/index.php/api/v2/pim/employees/" + empNumber + "/personal-details";

		CustomResponse customResponse = apiUtil.putEmployeeDetails(endpoint, cookieValue, requestBody);

		// Step 4: Validate implementation (optional, if you're doing method validation)
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath,
				"putEmployeeDetails", List.of("given", "cookie", "body", "put", "response"));
		Assert.assertTrue(isImplementationCorrect, "putEmployeeDetails must use RestAssured methods properly.");

		// Step 5: Print request/response
		System.out.println("Request Body: " + requestBody);
		System.out.println("Status Code: " + customResponse.getStatusCode());
		System.out.println("Response Body: " + customResponse.getResponseBody());

		// Step 6: Assertions
		Assert.assertEquals(customResponse.getStatusCode(), 200, "Expected status code 200");
		Assert.assertNotNull(customResponse.getResponseBody(), "Response body should not be null");
		Assert.assertNotNull(customResponse.getEmpNumbers(), "empNumbers list should not be null");
		Assert.assertNotNull(customResponse.getFirstNames(), "firstNames list should not be null");
		Assert.assertNotNull(customResponse.getLastNames(), "lastNames list should not be null");

	}

	@Test(priority = 13, dependsOnMethods = "loginWithSeleniumAndGetCookie", groups = {
			"PL2" }, description = "Precondition: Null\n" + "1. Generate a unique name\n"
					+ "2. Send POST request to '/web/index.php/api/v2/pim/employees'\n"
					+ "3. Make sure the request contains the request body and cookie\n"
					+ "4. Verify response contains the added name")
	public void PostJobCategoriesTest() throws IOException {

		String endpoint = "/web/index.php/api/v2/pim/reports/defined";
		String requestBody = "{" + "\"name\":\"jav\"," + "\"include\":\"currentAndPast\"," + "\"criteria\":{},"
				+ "\"fieldGroup\":{" + "\"1\":{" + "\"fields\":[9]," + "\"includeHeader\":false" + "}" + "}" + "}";

		// Step 3: Send POST request
		CustomResponse postResponse = apiUtil.PostJobCategoriesTest(endpoint, cookieValue, requestBody);

		// ✅ Validate implementation correctness
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath,
				"PostJobCategoriesTest", List.of("given", "cookie", "post", "response"));
		Assert.assertTrue(isImplementationCorrect, "PostEmployee must be implemented using RestAssured methods only!");

		// Debugging output
		System.out.println("Request Body: " + requestBody);
		System.out.println("Status Code: " + postResponse.getStatusCode());
		System.out.println("Response Body: " + postResponse.getResponse().asString());
		System.out.println("name: " + postResponse.getNationalityName()); // or getName()
		System.out.println("id: " + postResponse.getId());

		// Assertions
		Assert.assertNotNull(postResponse.getId(), "ID should not be null");
		Assert.assertEquals(postResponse.getStatusCode(), 200, "Expected status code 200 after creating employee");
	}

	@Test(priority = 14, dependsOnMethods = "loginWithSeleniumAndGetCookie", groups = {
			"PL1" }, description = "Precondition: Null\n" + "1. Generate unique first name, last name, and email\n"
					+ "2. Send POST request to '/web/index.php/api/v2/recruitment/candidates'\n"
					+ "3. Make sure the request contains the body and cookie\n"
					+ "4. Verify the response contains the added candidate details")
	public void PostCandidate() throws IOException {

// Step 1: Generate unique values
		String firstName = "First_" + System.currentTimeMillis();
		String lastName = "Last_" + System.currentTimeMillis();
		String email = "candidate_" + System.currentTimeMillis() + "@example.com";

// Step 2: Create JSON request body
		String requestBody = "{\n" + "  \"firstName\": \"" + firstName + "\",\n" + "  \"lastName\": \"" + lastName
				+ "\",\n" + "  \"email\": \"" + email + "\"\n" + "}";

		String endpoint = "/web/index.php/api/v2/recruitment/candidates";

// Step 3: Send POST request
		CustomResponse postResponse = apiUtil.PostCandidate(endpoint, cookieValue, requestBody);

// ✅ Validate implementation correctness
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "PostCandidate",
				List.of("given", "cookie", "post", "response"));
		Assert.assertTrue(isImplementationCorrect, "PostCandidate must be implemented using RestAssured methods only!");

// Debugging output
		System.out.println("Request Body: " + requestBody);
		System.out.println("Status Code: " + postResponse.getStatusCode());
		System.out.println("Response Body: " + postResponse.getResponse().asString());

// Step 4: Assertions
		Assert.assertEquals(postResponse.getStatusCode(), 200, "Expected status code 200 after creating candidate");

		String responseBody = postResponse.getResponse().asString();
		Assert.assertTrue(responseBody.contains(firstName), "Response should contain the candidate's first name");
		Assert.assertTrue(responseBody.contains(lastName), "Response should contain the candidate's last name");
		Assert.assertTrue(responseBody.contains(email), "Response should contain the candidate's email");

// Optional: if `containsText()` is your custom helper
		Assert.assertTrue(postResponse.containsText(firstName), "First name not found in response body");
		Assert.assertTrue(postResponse.containsText(lastName), "Last name not found in response body");
		Assert.assertTrue(postResponse.containsText(email), "Email not found in response body");
	}

	/**
	 * Test Case: Validate DELETE /web/index.php/api/v2/recruitment/candidates
	 * endpoint.
	 *
	 * Precondition: - A valid OrangeHRM session cookie obtained via Selenium login.
	 * - At least one employment status exists in the system.
	 *
	 * Test Steps: 1. Create a new employment status to ensure a valid record exists
	 * for deletion. 2. Retrieve the first employment status ID before deletion. 3.
	 * Construct the request body containing the employment status ID. 4. Send a
	 * DELETE request to the employment-statuses endpoint with the valid cookie. 5.
	 * Assert that the API returns HTTP 200 OK, indicating successful deletion. 6.
	 * Fetch the first employment status ID after deletion for verification.
	 *
	 * Expected Results: - The API should return HTTP 200 OK after deletion. - The
	 * first employment status ID after deletion should differ from the deleted ID.
	 * - Response body should not be null after deletion.
	 */

	@Test(priority = 15, dependsOnMethods = "loginWithSeleniumAndGetCookie", groups = {
			"PL1" }, description = "1. Fetch employment status ID using GET '/web/index.php/api/v2/admin/employment-statuses'\n"
					+ "2. Delete that employment status using DELETE '/web/index.php/api/v2/admin/employment-statuses'\n"
					+ "3. Assert response code is 200 and verify that the first employment status ID has changed")
	public void DeleterecruitmentCad() throws Exception {

		Response response = RestAssured.given().cookie("orangehrm", cookieValue)
				.header("Content-Type", "application/json")
				.get("https://yakshahrm.makemylabs.in/orangehrm-5.7/web/index.php/api/v2/recruitment/candidates").then()
				.statusCode(200).extract().response();

		// Fetch first candidate's ID
		Integer firstCandidateId = response.jsonPath().getInt("data[0].id");

		String endpoint = "/web/index.php/api/v2/recruitment/candidates";
		String requestBody = "{\n" + "  \"ids\": [" + firstCandidateId + "]\n" + "}";
// Step 2: Send DELETE request
		CustomResponse deleteResponse = apiUtil.DeleterecruitmentCad(endpoint, cookieValue, requestBody);

// Step 4: Validate implementation (optional, if you're doing method validation)
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath,
				"DeleterecruitmentCad", List.of("given", "cookie", "body", "delete", "response"));
		Assert.assertTrue(isImplementationCorrect, "DeleteJobTitleById must use RestAssured methods properly.");

// Step 3: Assert deletion response status
		Assert.assertEquals(deleteResponse.getStatusCode(), 200, "Expected status code 200 after deletion");
		System.out.println("Here it is" + deleteResponse.getResponseBody());

		System.out.println("First Employment Status ID after deletion: " + firstCandidateId);
		Assert.assertNotNull(deleteResponse.getResponseBody(), "Response body should not be null after deletion");
	}

	// https://opensource-demo.orangehrmlive.com/web/index.php/api/v2/recruitment/candidates

	// -------------------------------helper function------------------------

	public Response getEmpId(String endpoint, String cookieValue, Map<String, String> body) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		// Only add the body if it's not null
		if (body != null) {
			request.body(body);
		}

		return request.get(baseUrl + endpoint).then().extract().response();
	}

	public Response GetId(String endpoint, String cookieValue, Map<String, String> body) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		// Only add the body if it's not null
		if (body != null) {
			request.body(body);
		}

		return request.get(baseUrl + endpoint).then().extract().response();
	}

	public String generateRandomString(int length) {

		String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		SecureRandom RANDOM = new SecureRandom();
		StringBuilder sb = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			int index = RANDOM.nextInt(CHARACTERS.length());
			sb.append(CHARACTERS.charAt(index));
		}

		return sb.toString();
	}

	public void createEmploymentStatus() {
		String endpoint = "/web/index.php/api/v2/admin/employment-statuses";

		// Generate a random string for name
		String uniqueName = "EmpStatus_" + System.currentTimeMillis();

		// Request body
		String requestBody = "{\n" + "  \"name\": \"" + uniqueName + "\"\n" + "}";

		Response response = RestAssured.given().relaxedHTTPSValidation().cookie("orangehrm", cookieValue)
				.header("Content-Type", "application/json").body(requestBody).post(baseUrl + endpoint).then().extract()
				.response();

		System.out.println("Create Employment Status Request Body: " + requestBody);
		System.out.println("Create Employment Status Response Code: " + response.getStatusCode());
		System.out.println("Create Employment Status Response: " + response.asString());

		if (response.getStatusCode() != 200) {
			throw new RuntimeException("Failed to create employment status. Status: " + response.getStatusCode());
		}
	}

	public int getFirstEmploymentStatus() {
		String endpoint = "/web/index.php/api/v2/admin/employment-statuses?limit=50&offset=0";

		Response response = RestAssured.given().cookie("orangehrm", cookieValue).get(baseUrl + endpoint);

		if (response.statusCode() == 200) {
			int firstId = response.jsonPath().getInt("data[0].id");
			System.out.println("First Job Title ID: " + firstId);
			return firstId;
		} else {
			System.out.println("Failed to fetch job titles. Status code: " + response.statusCode());
			return -1;
		}

	}

	public int getFirstJobTitleId() {
		String endpoint = "/web/index.php/api/v2/admin/job-titles?limit=50&offset=0&sortField=jt.jobTitleName&sortOrder=ASC";

		Response response = RestAssured.given().relaxedHTTPSValidation().cookie("orangehrm", cookieValue)
				.get(baseUrl + endpoint);

		System.out.println("Get Job Titles Response: " + response.asString());

		if (response.statusCode() == 200) {
			List<Map<String, Object>> dataList = response.jsonPath().getList("data");
			if (dataList != null && !dataList.isEmpty() && dataList.get(0).get("id") != null) {
				int firstId = ((Number) dataList.get(0).get("id")).intValue();
				System.out.println("First Job Title ID: " + firstId);
				return firstId;
			} else {
				System.out.println("No job titles found in response.");
				return -1;
			}
		} else {
			System.out.println("Failed to fetch job titles. Status code: " + response.statusCode());
			return -1;
		}
	}

}
