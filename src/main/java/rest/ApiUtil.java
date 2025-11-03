package rest;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonSerializable.Base;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ApiUtil {
	private static final Set<Integer> usedNumbers = new HashSet<>();
	private static final Random random = new Random();
	private static String BASE_URL;
	Properties prop;

	/**
	 * Reads and returns the base URL from the config.properties file.
	 *
	 * @return the base URL as a String if found; otherwise, returns null
	 */
	public String getBaseUrl() {
		prop = new Properties();
		try (FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\resources\\config.properties")) {
			prop.load(fis);
			BASE_URL = prop.getProperty("base.url");
			return BASE_URL;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Reads and returns the username value from the config.properties file.
	 *
	 * @return the username as a String if found; otherwise, returns null
	 */
	public String getUsername() {
		prop = new Properties();
		try (FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\resources\\config.properties")) {
			prop.load(fis);
			return prop.getProperty("username");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Reads and returns the password value from the config.properties file.
	 *
	 * @return the password as a String if found; otherwise, returns null
	 */
	public String getPassword() {
		prop = new Properties();
		try (FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\resources\\config.properties")) {
			prop.load(fis);
			return prop.getProperty("password");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Retrieves holiday data from the server by sending a GET request to the
	 * specified endpoint.
	 *
	 * @param endpoint    the API endpoint to fetch holiday data
	 * @param cookieValue the session cookie used for authentication
	 * @param body        optional request body parameters to include in the GET
	 *                    request
	 * @return CustomResponse containing the response, status code, status line, and
	 *         lists of holiday details: IDs, names, dates, recurring flags,
	 *         lengths, and length names
	 */
	public CustomResponse GetHolidayData(String endpoint, String cookieValue, Map<String, String> body) {

		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (body != null) {
			request.body(body);
		}

		Response response = request.get(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		List<Object> ids = new ArrayList<>();
		List<Object> names = new ArrayList<>();
		List<Object> dates = new ArrayList<>();
		List<Object> recurrings = new ArrayList<>();
		List<Object> lengths = new ArrayList<>();
		List<Object> lengthNames = new ArrayList<>();

		JsonPath jsonPath = response.jsonPath();
		List<Map<String, Object>> data = jsonPath.getList("data");

		if (data != null) {
			for (Map<String, Object> holiday : data) {
				ids.add(holiday.get("id"));
				names.add(holiday.get("name"));
				dates.add(holiday.get("date"));
				recurrings.add(holiday.get("recurring"));
				lengths.add(holiday.get("length"));
				lengthNames.add(holiday.get("lengthName"));
			}
		} else {
			System.out.println("⚠️ 'data' field is null in response. Status code: " + statusCode);
		}

		return new CustomResponse(response, statusCode, status, ids, names, dates, recurrings, lengths, lengthNames);

	}

	/**
	 * Retrieves leave data from the server by sending a GET request to the
	 * specified endpoint.
	 *
	 * @param endpoint    the API endpoint to fetch leave data
	 * @param cookieValue the session cookie used for authentication
	 * @param body        optional request body parameters to include in the GET
	 *                    request
	 * @return CustomResponse containing the response, status code, status line, and
	 *         lists of leave details: IDs, names, dates, recurring flags, lengths,
	 *         and length names
	 */
	public CustomResponse GetLeaveData(String endpoint, String cookieValue, Map<String, String> body) {

		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (body != null) {
			request.body(body);
		}

		Response response = request.get(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		List<Object> ids = new ArrayList<>();
		List<Object> names = new ArrayList<>();
		List<Object> dates = new ArrayList<>();
		List<Object> recurrings = new ArrayList<>();
		List<Object> lengths = new ArrayList<>();
		List<Object> lengthNames = new ArrayList<>();

		JsonPath jsonPath = response.jsonPath();
		List<Map<String, Object>> data = jsonPath.getList("data");

		if (data != null) {
			for (Map<String, Object> Leave : data) {
				ids.add(Leave.get("id"));
				names.add(Leave.get("name"));
				dates.add(Leave.get("date"));
				recurrings.add(Leave.get("recurring"));
				lengths.add(Leave.get("length"));
				lengthNames.add(Leave.get("lengthName"));
			}
		} else {
			System.out.println("⚠️ 'data' field is null in response. Status code: " + statusCode);
		}

		return new CustomResponse(response, statusCode, status, ids, names, dates, recurrings, lengths, lengthNames);
	}

	/**
	 * Retrieves the total employee count by sending a GET request.
	 *
	 * @param endpoint    the API endpoint to fetch employee count
	 * @param cookieValue the session cookie used for authentication
	 * @param body        the optional request body to include in the GET request
	 * @return CustomResponse containing the response, status code, status line, and
	 *         employee count
	 */
	public CustomResponse GetEmpCount(String endpoint, String cookieValue, Map<String, String> body) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (body != null) {
			request.body(body);
		}

		Response response = request.get(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		System.out.println("Raw response:");
		response.prettyPrint(); // ✅ Debug print

		JsonPath jsonPath = response.jsonPath();

		// ✅ Get the "count" directly from the "data" object
		Map<String, Object> data = jsonPath.getMap("data");
		int count = 0;

		count = (int) data.get("count");
		System.out.println("⚠️ 'count' key is missing inside 'data'.");

		return new CustomResponse(response, statusCode, status, count);
	}

	/**
	 * Retrieves leave types by sending a GET request and extracts leave-related
	 * information.
	 *
	 * @param endpoint    the API endpoint to fetch leave types
	 * @param cookieValue the session cookie used for authentication
	 * @param body        the optional request body to include in the GET request
	 * @return CustomResponse containing the response, status code, status line, and
	 *         lists of leave IDs, names, situational flags, and deletion status
	 */
	public CustomResponse GetLeaveType(String endpoint, String cookieValue, Map<String, String> body) {

		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (body != null) {
			request.body(body);
		}
		Response response = request.get(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		List<Object> ids = new ArrayList<>();
		List<Object> names = new ArrayList<>();
		List<Object> situationals = new ArrayList<>();
		List<Object> Deletes = new ArrayList<>();

		JsonPath jsonPath = response.jsonPath();
		List<Map<String, Object>> data = jsonPath.getList("data");

		if (data != null) {
			for (Map<String, Object> Leave : data) {
				ids.add(Leave.get("id"));
				names.add(Leave.get("name"));
				situationals.add(Leave.get("situational"));
				Deletes.add(Leave.get("deleted"));

			}
		} else {
			System.out.println("⚠️ 'data' field is null in response. Status code: " + statusCode);
		}

		return new CustomResponse(response, statusCode, status, ids, names, situationals, Deletes);
	}

	/**
	 * Retrieves a usage report by sending a GET request and extracts header-related
	 * information.
	 *
	 * @param endpoint    the API endpoint to fetch the usage report
	 * @param cookieValue the session cookie used for authentication
	 * @param body        the optional request body to include in the GET request
	 * @return CustomResponse containing the response, status code, status line, and
	 *         lists of properties, names, sizes, pins, and cell properties from the
	 *         headers
	 */
	public CustomResponse GetUsageReport(String endpoint, String cookieValue, Map<String, String> body) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (body != null) {
			request.body(body);
		}

		Response response = request.get(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		List<Object> names = new ArrayList<>();
		List<Object> props = new ArrayList<>();
		List<Object> sizes = new ArrayList<>();
		List<Object> pins = new ArrayList<>();
		List<Object> cellProperties = new ArrayList<>(); // Keep it List<Object> for flexibility in CustomResponse

		JsonPath jsonPath = response.jsonPath();
		List<Map<String, Object>> headers = jsonPath.getList("data.headers");

		if (headers != null) {
			for (Map<String, Object> header : headers) {
				names.add(header.get("name"));
				props.add(header.get("prop"));
				sizes.add(header.get("size"));
				pins.add(header.get("pin"));

				Object cellProp = header.get("cellProperties");
				if (cellProp instanceof Map || cellProp == null) {
					cellProperties.add(cellProp); // add map or null as-is
				} else {
					System.out.println("⚠️ Unexpected type for cellProperties: " + cellProp.getClass().getSimpleName());
					cellProperties.add(null);
				}
			}
		} else {
			System.out.println("❌ 'data.headers' is missing or empty in the response. Status code: " + statusCode);
		}

		return new CustomResponse(response, statusCode, status, props, names, sizes, pins, cellProperties);
	}

	/**
	 * Sends a GET request to retrieve vacancy details and constructs a
	 * CustomResponse object containing parsed data from the response.
	 *
	 * @param endpoint    the API endpoint to fetch vacancies
	 * @param cookieValue the session cookie value for authentication
	 * @param queryParams a map of query parameters to include in the request
	 * @return a CustomResponse object containing response details and vacancy data
	 */
	public CustomResponse GetVacancies(String endpoint, String cookieValue, Map<String, Object> queryParams) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (queryParams != null) {
			request.queryParams(queryParams);
		}

		Response response = request.get(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String statusLine = response.getStatusLine();

		JsonPath jsonPath = response.jsonPath();
		List<Object> dataList = jsonPath.getList("data");

		List<Object> id = jsonPath.getList("data.id");
		List<Object> name = jsonPath.getList("data.name");
		List<Object> description = jsonPath.getList("data.description");
		List<Object> numOfPositions = jsonPath.getList("data.numOfPositions");
		List<Object> statusList = jsonPath.getList("data.status");
		List<Object> isPublished = jsonPath.getList("data.isPublished");
		List<Object> jobTitle = jsonPath.getList("data.jobTitle");

		return new CustomResponse(response, statusCode, statusLine, id, name, description, numOfPositions, statusList,
				isPublished, jobTitle);
	}

	/**
	 * Sends a GET request to retrieve job titles and constructs a CustomResponse
	 * object containing the parsed data from the response.
	 *
	 * @param endpoint    the API endpoint to fetch job titles
	 * @param cookieValue the session cookie value for authentication
	 * @param queryParams a map of query parameters to include in the request
	 * @return a CustomResponse object containing response details and job title
	 *         data
	 */
	public CustomResponse GetJobTitles(String endpoint, String cookieValue, Map<String, Object> queryParams) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (queryParams != null) {
			request.queryParams(queryParams);
		}

		Response response = request.get(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		JsonPath jsonPath = response.jsonPath();

		System.out.println("Response Body: " + response.asString());

		// ✅ Extract with correct types
		List<Integer> ids = jsonPath.getList("data.id", Integer.class);
		List<String> titles = jsonPath.getList("data.title", String.class);

		// ✅ Convert to Object lists for flexibility in CustomResponse
		List<Object> idsAsObjects = new ArrayList<>(ids);
		List<Object> titlesAsObjects = new ArrayList<>(titles);

		// ✅ Create response object
		return new CustomResponse(response, statusCode, status, idsAsObjects, titlesAsObjects);
	}

	/**
	 * Retrieves employee personal details by sending a GET request to the specified
	 * endpoint. Supports dynamic response handling where "data" may be either an
	 * object or an array.
	 *
	 * @param endpoint    the API endpoint to fetch employee details
	 * @param cookieValue the session cookie value for authentication
	 * @param queryParams a map of query parameters to include in the request
	 * @return a CustomResponse object containing response details and employee
	 *         personal data
	 */
	public CustomResponse GetEmpPersonalDetails(String endpoint, String cookieValue, Map<String, Object> queryParams) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (queryParams != null) {
			request.queryParams(queryParams);
		}

		Response response = request.get(BASE_URL + endpoint).then().extract().response();

		Integer statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		JsonPath jsonPath = response.jsonPath();

		System.out.println("Response Body: " + response.asString());

		// ✅ Safe extraction for empNumber (works if "data" is object or array)
		Integer empNumber = null;
		String lastName = null;
		String firstName = null;
		String nationalityName = null;

		if (jsonPath.get("data.empNumber") != null) {
			// Case 1: "data" is an object
			empNumber = jsonPath.getInt("data.empNumber");
			lastName = jsonPath.getString("data.lastName");
			firstName = jsonPath.getString("data.firstName");
			nationalityName = jsonPath.getString("data.nationality.name");
		} else if (jsonPath.get("data[0].empNumber") != null) {
			empNumber = jsonPath.getInt("data[0].empNumber");
			lastName = jsonPath.getString("data[0].lastName");
			firstName = jsonPath.getString("data[0].firstName");
			nationalityName = jsonPath.getString("data[0].nationality.name");
		}

		return new CustomResponse(response, statusCode, status, empNumber, firstName, lastName, nationalityName);
	}

	/**
	 * Retrieves employee data by sending a GET request to the specified endpoint.
	 * Handles dynamic response structures where the "data" field can be either an
	 * array or a single object.
	 *
	 * @param endpoint    the API endpoint to fetch employee data
	 * @param cookieValue the session cookie value for authentication
	 * @param body        the request body (currently unused in this method)
	 * @return a CustomResponse object containing the response and employee details
	 *         such as employee numbers, first names, last names, and employee IDs
	 */
	public CustomResponse GetEmpData(String endpoint, String cookieValue, String body) {
		Response response = RestAssured.given().header("Content-Type", "application/json")
				.cookie("orangehrm", cookieValue).get(BASE_URL + endpoint).then().extract().response();

		Integer statusCode = response.getStatusCode();
		String statusLine = response.getStatusLine();

		JsonPath jsonPath = response.jsonPath();

		// Initialize lists
		List<Integer> empNumbers = new ArrayList<>();
		List<String> firstNames = new ArrayList<>();
		List<String> lastNames = new ArrayList<>();
		List<String> employeeNumbers = new ArrayList<>();

		// Handle "data" being array or single object
		Object dataNode = jsonPath.get("data");

		if (dataNode instanceof List) {
			// ✅ data is an array
			empNumbers = jsonPath.getList("data.empNumber");
			firstNames = jsonPath.getList("data.firstName");
			lastNames = jsonPath.getList("data.lastName");
			employeeNumbers = jsonPath.getList("data.employeeId");
		} else if (dataNode instanceof Map) {
			// ✅ data is a single object
			empNumbers.add(jsonPath.getInt("data.empNumber"));
			firstNames.add(jsonPath.getString("data.firstName"));
			lastNames.add(jsonPath.getString("data.lastName"));
			employeeNumbers.add(jsonPath.getString("data.employeeId"));
		}
		return new CustomResponse(response, statusCode, statusLine, empNumbers, firstNames, lastNames, employeeNumbers);
	}

	/**
	 * Retrieves the leave workweek configuration from the given API endpoint.
	 * Extracts workweek schedule data where each key represents a day number and
	 * the corresponding value represents the work status for that day.
	 *
	 * @param endpoint    the API endpoint to fetch leave workweek data
	 * @param cookieValue the session cookie value used for authentication
	 * @return a CustomResponse object containing the HTTP response, status code,
	 *         status line, and a map of workweek data with day numbers as keys and
	 *         work status as values
	 */
	public CustomResponse GetLeaveWorkWeek(String endpoint, String cookieValue) {
		Response response = RestAssured.given().cookie("orangehrm", cookieValue).when().get(BASE_URL + endpoint).then()
				.extract().response();

		int statusCode = response.getStatusCode();
		String statusLine = response.getStatusLine();

		JsonPath jsonPath = response.jsonPath();

		// Extract "data" (workweek schedule per day, where keys are day numbers)
		Map<String, Integer> workweekData = jsonPath.getMap("data", String.class, Integer.class);

		return new CustomResponse(response, statusCode, statusLine, workweekData);
	}

	/**
	 * Sends a POST request to create a new employee using the provided API endpoint
	 * and request body. The method includes authentication via session cookie and
	 * content type as JSON.
	 *
	 * @param endpoint    the API endpoint to create the employee
	 * @param cookieValue the session cookie value for authentication
	 * @param body        the JSON request body containing employee details
	 * @return a CustomResponse object containing the HTTP response, status code,
	 *         and status line
	 */
	public CustomResponse PostEmployee(String endpoint, String cookieValue, String body) {
		Response response = RestAssured.given().relaxedHTTPSValidation().header("Content-Type", "application/json")
				.cookie("orangehrm", cookieValue).body(body).when().post(BASE_URL + endpoint).then().extract()
				.response();
		int statusCode = response.getStatusCode();
		String statusLine = response.getStatusLine();

		// Wrap response inside CustomResponse (basic version)
		return new CustomResponse(response, statusCode, statusLine);
	}

	/**
	 * Sends a POST request to create a new candidate using the provided endpoint,
	 * session cookie, and request body. Extracts the candidate ID and name from the
	 * response and returns them encapsulated in a CustomResponse.
	 *
	 * @param endpoint    the API endpoint to create a candidate
	 * @param cookieValue the session cookie value for authentication
	 * @param requestBody the JSON request body containing candidate details
	 * @return a CustomResponse containing the HTTP response, status code, status
	 *         line, and lists of candidate IDs and names
	 */
	public CustomResponse PostCandidate(String endpoint, String cookieValue, String requestBody) {
		Response response = RestAssured.given().relaxedHTTPSValidation().cookie("orangehrm", cookieValue)
				.header("Content-Type", "application/json").body(requestBody).when().post(BASE_URL + endpoint).then()
				.extract().response();

		JsonPath jsonPath = response.jsonPath();

		List<Integer> empStatusIdList = new ArrayList<>();
		List<String> empStatusNameList = new ArrayList<>();

		Object dataObj = jsonPath.get("data");
		if (dataObj instanceof Map) {
			Map<String, Object> status = (Map<String, Object>) dataObj;
			empStatusIdList.add(((Number) status.get("id")).intValue());
			empStatusNameList.add((String) status.get("name"));
		}
		int statusCode = response.getStatusCode();
		String statusLine = response.getStatusLine();

		return new CustomResponse(response, statusCode, statusLine, empStatusIdList, empStatusNameList, null);
	}

	/**
	 * Sends a POST request to create a new job category using the provided
	 * endpoint, session cookie, and request body. Extracts the job category ID and
	 * name from the response and returns them in a CustomResponse.
	 *
	 * @param endpoint    the API endpoint to create a job category
	 * @param cookieValue the session cookie value for authentication
	 * @param requestBody the JSON request body containing job category details
	 * @return a CustomResponse containing the HTTP response, status code, status
	 *         line, and lists of job category IDs and names
	 */
	public CustomResponse PostJobCategoriesTest(String endpoint, String cookieValue, String requestBody) {
		Response response = RestAssured.given().cookie("orangehrm", cookieValue)
				.header("Content-Type", "application/json").body(requestBody).when().post(BASE_URL + endpoint).then()
				.extract().response();

		JsonPath jsonPath = response.jsonPath();

		List<Integer> empStatusIdList = new ArrayList<>();
		List<String> empStatusNameList = new ArrayList<>();

		Object dataObj = jsonPath.get("data");
		if (dataObj instanceof Map) {
			Map<String, Object> status = (Map<String, Object>) dataObj;
			empStatusIdList.add(((Number) status.get("id")).intValue());
			empStatusNameList.add((String) status.get("name"));
		}
		int statusCode = response.getStatusCode();
		String statusLine = response.getStatusLine();

		return new CustomResponse(response, statusCode, statusLine, empStatusIdList, empStatusNameList, null);
	}

	/**
	 * Sends a PUT request to update employee details using the provided API
	 * endpoint, session cookie, and request body. Handles response data whether
	 * it's a single employee object or empty.
	 *
	 * @param endpoint    the API endpoint to update employee details
	 * @param cookieValue the session cookie value for authentication
	 * @param requestBody the JSON request body containing updated employee data
	 * @return a CustomResponse containing the HTTP response, status code, status
	 *         line, and employee detail lists (empNumbers, firstNames, lastNames,
	 *         employeeIds)
	 */
	public CustomResponse putEmployeeDetails(String endpoint, String cookieValue, String requestBody) {
		// Send PUT request
		Response response = RestAssured.given().header("Content-Type", "application/json")
				.cookie("orangehrm", cookieValue).body(requestBody).put(BASE_URL + endpoint).then().extract()
				.response();

		Integer statusCode = response.getStatusCode();
		String statusLine = response.getStatusLine();

		JsonPath jsonPath = response.jsonPath();
		Object dataNode = jsonPath.get("data");

		// Initialize lists
		List<Integer> empNumbers = new ArrayList<>();
		List<String> firstNames = new ArrayList<>();
		List<String> lastNames = new ArrayList<>();
		List<String> employeeIds = new ArrayList<>();

		if (dataNode instanceof Map) {
			Map<String, Object> dataMap = (Map<String, Object>) dataNode;

			// Only add non-null/non-empty values
			if (dataMap.get("empNumber") != null) {
				empNumbers.add((Integer) dataMap.get("empNumber"));
			}
			if (dataMap.get("firstName") != null && !dataMap.get("firstName").toString().isEmpty()) {
				firstNames.add(dataMap.get("firstName").toString());
			}
			if (dataMap.get("lastName") != null && !dataMap.get("lastName").toString().isEmpty()) {
				lastNames.add(dataMap.get("lastName").toString());
			}
			if (dataMap.get("employeeId") != null && !dataMap.get("employeeId").toString().isEmpty()) {
				employeeIds.add(dataMap.get("employeeId").toString());
			}
		}

		// Create CustomResponse object with lists
		return new CustomResponse(response, statusCode, statusLine, empNumbers, firstNames, lastNames, employeeIds);
	}

	/**
	 * Sends a DELETE request to remove a recruitment candidate using the provided
	 * endpoint, session cookie, and request body. Extracts the list of deleted
	 * candidate IDs from the response.
	 *
	 * @param endpoint    the API endpoint for deleting the candidate
	 * @param cookieValue the session cookie value for authentication
	 * @param requestBody the JSON request body containing candidate deletion
	 *                    details
	 * @return a CustomResponse containing the HTTP response, status code, status
	 *         line, and a list of deleted candidate IDs
	 */
	public CustomResponse DeleterecruitmentCad(String endpoint, String cookieValue, String requestBody) {
		Response response = RestAssured.given().relaxedHTTPSValidation().cookie("orangehrm", cookieValue)
				.header("Content-Type", "application/json").body(requestBody).when().delete(BASE_URL + endpoint).then()
				.extract().response();

		JsonPath jsonPath = response.jsonPath();

		// Extract [id] from: { "data": [19], ... }
		List<Integer> empStatusIdList = jsonPath.getList("data", Integer.class);

		// For DELETE, name list is usually not present — leave empty
		List<String> empStatusNameList = new ArrayList<>();
		int statusCode = response.getStatusCode();
		String statusLine = response.getStatusLine();

		return new CustomResponse(response, statusCode, statusLine, empStatusIdList, empStatusNameList, null);
	}
}
