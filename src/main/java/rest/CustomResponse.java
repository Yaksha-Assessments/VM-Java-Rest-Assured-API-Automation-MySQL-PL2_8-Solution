package rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.restassured.response.Response;

public class CustomResponse {
	private Response response;
	private List<Map<String, Object>> listResults;
	private String resultMessage;
	private Map<String, Object> mapResults;
	private List<Object> genericNames;
	private Object storeId;
	private Object category;
	private Object isActive;
	private List<Object> itemIds;
	private List<Object> itemNames;
	private Integer statusCode;
	private String pimEmployeeId;
	private String status;
	private List<Object> counts;
	private Integer empCount;
	private List<Object> deletes;
	private List<Object> situationals;
	private List<Object> props;
	private List<Object> sizes;
	private List<Object> pins;
	private List<Object> cellProperties;
	public List<Integer> statusIdList;
	public List<String> statusNameList;

	@SuppressWarnings("unused")
	private List<Object> Ids;

	private Object data;

	private List<Object> ids;
	private List<Object> names;

	private List<Object> dates;
	private List<Object> recurrings;
	private List<Object> lengths;
	private List<Object> lengthNames;
	private Object name;
	private Object lastName;
	@SuppressWarnings("unused")
	private Object id;

	/**
	 * Constructs a CustomResponse for a report-like structure with multiple lists.
	 * Assigns lists to appropriate fields if they match expected types and are
	 * non-empty.
	 *
	 * @param response   The raw HTTP response
	 * @param statusCode The HTTP status code
	 * @param status     The HTTP status line
	 * @param list1      Expected to contain 'props' (Strings)
	 * @param list2      Expected to contain 'names' (Strings)
	 * @param list3      Expected to contain 'sizes' (Integers)
	 * @param list4      Expected to contain 'pins' (Objects)
	 * @param list5      Expected to contain 'cellProperties' (Objects)
	 */
	public CustomResponse(Response response, Integer statusCode, String status, List<Object> list1, List<Object> list2,
			List<Object> list3, List<Object> list4, List<Object> list5) {

		this.response = response;
		this.statusCode = statusCode;
		this.status = status;

// If props are present, treat as ReportHeader structure
		if (list1 != null && list2 != null && list3 != null && list4 != null && list5 != null && list1.size() > 0
				&& list1.get(0) instanceof String && list3.get(0) instanceof Integer) {
			this.props = list1;
			this.names = list2;
			this.sizes = list3;
			this.pins = list4;
			this.cellProperties = list5;
		}
// Otherwise treat as LeaveType structure
		else {
			this.Ids = list1;
			this.counts = list2;
			this.names = list3;
			this.situationals = list4;
			this.deletes = list5;
		}
	}

	/**
	 * Constructs a CustomResponse containing basic employee or entity details.
	 *
	 * @param response   The raw HTTP response
	 * @param statusCode The HTTP status code
	 * @param status     The HTTP status line
	 * @param id         The unique identifier of the entity
	 * @param name       The first name or primary name of the entity
	 * @param lastName   The last name or secondary name of the entity
	 */
	public CustomResponse(Response response, int statusCode, String status, Object id, Object name, Object lastName) {
		this.response = response;
		this.statusCode = statusCode;
		this.status = status;
		this.setId(id);
		this.name = name;
		this.lastName = lastName;
	}

	/**
	 * Constructs a CustomResponse containing an ID and a list of names.
	 *
	 * @param response   The raw HTTP response
	 * @param statusCode The HTTP status code
	 * @param statusLine The HTTP status line
	 * @param id         The unique identifier of the entity
	 * @param name       A list of names associated with the entity
	 */
	public CustomResponse(Response response, int statusCode, String statusLine, Object id, List<Object> name) {
		this.response = response;
		this.statusCode = statusCode;
		this.statusLine = statusLine;
		this.setId(id);
		this.names = name;
	}

	/**
	 * Constructs a CustomResponse containing a single data object.
	 *
	 * @param response   The raw HTTP response
	 * @param statusCode The HTTP status code
	 * @param status     The HTTP status line
	 * @param data       The response data object
	 */
	public CustomResponse(Response response, int statusCode, String status, Object data) {
		this.response = response;
		this.statusCode = statusCode;
		this.status = status;
		this.data = data;
	}

	/**
	 * Constructs a CustomResponse containing the employee count.
	 *
	 * @param response   The raw HTTP response
	 * @param statusCode The HTTP status code
	 * @param status     The HTTP status line
	 * @param empCount   The number of employees retrieved
	 */
	public CustomResponse(Response response, int statusCode, String status, int empCount) {
		this.response = response;
		this.statusCode = statusCode;
		this.status = status;
		this.empCount = empCount;
	}

	// Getters

	public Object getData() {
		return data;
	}

	/**
	 * Constructs a CustomResponse for leave type data.
	 *
	 * @param response     The raw HTTP response
	 * @param statusCode   The HTTP status code
	 * @param status       The HTTP status line
	 * @param ids          List of leave type IDs
	 * @param names        List of leave type names
	 * @param situationals List indicating if leave types are situational
	 * @param deletes      List indicating if leave types are marked deleted
	 */
	public CustomResponse(Response response, int statusCode, String status, List<Object> ids, List<Object> names,
			List<Object> situationals, List<Object> deletes) {
		this.response = response;
		this.statusCode = statusCode;
		this.status = status;

		this.names = names;
		this.situationals = situationals;
		this.deletes = deletes;
		this.ids = ids;
	}

	/**
	 * Constructs a CustomResponse for leave or holiday data.
	 *
	 * @param response    The raw HTTP response
	 * @param statusCode  The HTTP status code
	 * @param status      The HTTP status line
	 * @param ids         List of IDs for each leave/holiday entry
	 * @param names       List of names for each leave/holiday entry
	 * @param dates       List of dates for each leave/holiday entry
	 * @param recurrings  List indicating if entries are recurring
	 * @param lengths     List of lengths for each entry
	 * @param lengthNames List of length names for each entry
	 */
	public CustomResponse(Response response, int statusCode, String status, List<Object> ids, List<Object> names,
			List<Object> dates, List<Object> recurrings, List<Object> lengths, List<Object> lengthNames) {
		this.response = response;
		this.statusCode = statusCode;
		this.status = status;
		this.ids = ids;
		this.names = names;
		this.dates = dates;
		this.recurrings = recurrings;
		this.lengths = lengths;
		this.lengthNames = lengthNames;
	}

	public Object getId() {
		return id;
	}

	public Object getName() {
		return name;
	}

	public List<Object> getGenericNames() {
		return genericNames;
	}

	public Object getLastName() {
		return lastName;
	}

	public int getEmpCount() {
		return this.empCount;
	}

	public Response getResponse() {
		return response;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getStatus() {
		return status;
	}

	public List<Object> getTitle() {
		return Titles;
	}

	public void setTitle(List<Object> Titles) {
		this.Titles = Titles;
	}

	public List<Object> getIds() {
		return ids;
	}

	public void setIds(List<Object> ids) {
		this.ids = ids;
	}

	public List<Object> getNames() {
		return names;
	}

	public List<Object> getDates() {
		return dates;
	}

	public List<Object> getSituationals() {
		return situationals;
	}

	public List<Object> getDeletes() {
		return deletes;
	}

	public List<Object> getProps() {
		return props;
	}

	public List<Object> getSizes() {
		return sizes;
	}

	public List<Object> getPins() {
		return pins;
	}

	public List<Object> getCellProperties() {
		return cellProperties;
	}

	public void setId(Object id) {
		this.id = id;
	}

	public List<Integer> getEmpNumbers() {
		return empNumbers;
	}

	public List<String> getFirstNames() {
		return firstNames;
	}

	public List<String> getLastNames() {
		return lastNames;
	}

	public List<String> getEmployeeNumbers() {
		return employeeNumbers;
	}

	public String getResponseBody() {
		return response.getBody().asString();
	}

	private Map<String, Integer> workweekData;

	public Map<String, Integer> getWorkweekData() {
		this.data = workweekData;
		return workweekData;
	}

	public void setWorkweekData(Map<String, Integer> workweekData) {
		this.workweekData = workweekData;
	}

	private List<String> empStatusIdList;
	private List<String> empStatusNameList;

	/**
	 * Constructs a CustomResponse for employment status data.
	 *
	 * @param response          The raw HTTP response
	 * @param statusCode        The HTTP status code
	 * @param statusLine        The HTTP status line
	 * @param empStatusIdList   List of employment status IDs
	 * @param empStatusNameList List of employment status names
	 */
	public CustomResponse(Response response, int statusCode, String statusLine, List<String> empStatusIdList,
			List<String> empStatusNameList) {
		this.response = response;
		this.statusCode = statusCode;
		this.status = statusLine;

		// Store parsed lists
		this.empStatusIdList = empStatusIdList;
		this.empStatusNameList = empStatusNameList;
	}

	public Integer getEmpNumber() {
		return empNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getNationalityName() {
		return nationalityName;
	}

	public boolean containsText(String text) {
		return response.asString().contains(text);
	}

	private String responseBody;

	private List<Integer> empNumbers;
	private List<String> firstNames;
	private List<String> lastNames;
	private List<String> employeeNumbers;

	public void setEmpNumbers(List<Integer> empNumbers) {
		this.empNumbers = empNumbers;
	}

	public void setFirstNames(List<String> firstNames) {
		this.firstNames = firstNames;
	}

	public void setLastNames(List<String> lastNames) {
		this.lastNames = lastNames;
	}

	public void setEmployeeNumbers(List<String> employeeNumbers) {
		this.employeeNumbers = employeeNumbers;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	/**
	 * Constructs a CustomResponse for employee data lists.
	 *
	 * @param response        The raw HTTP response
	 * @param statusCode      The HTTP status code
	 * @param statusLine      The HTTP status line
	 * @param empNumbers      List of employee numbers
	 * @param firstNames      List of employee first names
	 * @param lastNames       List of employee last names
	 * @param employeeNumbers List of employee IDs
	 */
	public CustomResponse(Response response, Integer statusCode, String statusLine, List<Integer> empNumbers,
			List<String> firstNames, List<String> lastNames, List<String> employeeNumbers) {
		this.response = response;
		this.statusCode = statusCode;
		this.statusLine = statusLine;
		this.empNumbers = empNumbers;
		this.firstNames = firstNames;
		this.lastNames = lastNames;
		this.employeeNumbers = employeeNumbers;
	}

	/**
	 * Constructs a CustomResponse for a single employee's personal details.
	 *
	 * @param response        The raw HTTP response
	 * @param statusCode      The HTTP status code
	 * @param status          The HTTP status line
	 * @param empNumber       Employee number
	 * @param firstName       Employee's first name
	 * @param lastName        Employee's last name
	 * @param nationalityName Employee's nationality name
	 */
	public CustomResponse(Response response, Integer statusCode, String status, Integer empNumber, String firstName,
			String lastName, String nationalityName) {
		this.response = response;
		this.statusCode = statusCode;
		this.statusLine = status;
		this.empNumber = empNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.nationalityName = nationalityName;
	}

	private String statusLine;
	private List<Object> description;
	private List<Object> numOfPositions;
	private List<Object> statusList;
	private List<Object> isPublished;

	private List<Object> jobTitleIds;
	private List<Object> jobTitles;
	private List<Object> jobDeleted;

	private List<Object> hiringManagerIds;
	private List<Object> hiringManagerFirstNames;
	private List<Object> hiringManagerMiddleNames;
	private List<Object> hiringManagerLastNames;
	private List<Object> hiringManagerTerminationIds;
	private List<Object> Titles;

	// Add missing fields for employee details
	private String middleName;
	private String firstName;
	private Integer empNumber;
	private String employeeId;
	private String otherId;
	private String drivingLicenseNo;
	private String drivingLicenseExpiredDate;
	private Integer gender;
	private String maritalStatus;
	private String birthday;
	private String terminationId;
	private Integer nationalityId;
	private String nationalityName;

	/**
	 * Constructs a CustomResponse for job vacancies.
	 *
	 * @param response       The raw HTTP response
	 * @param statusCode     The HTTP status code
	 * @param statusLine     The HTTP status line
	 * @param ids            List of vacancy IDs
	 * @param names          List of vacancy names
	 * @param description    List of vacancy descriptions
	 * @param numOfPositions List of number of positions per vacancy
	 * @param statusList     List of vacancy statuses
	 * @param isPublished    List indicating if each vacancy is published
	 * @param jobTitles      List of job titles for the vacancies
	 */
	public CustomResponse(Response response, int statusCode, String statusLine, List<Object> ids, List<Object> names,
			List<Object> description, List<Object> numOfPositions, List<Object> statusList, List<Object> isPublished,
			List<Object> jobTitles) {
		this.response = response;
		this.statusCode = statusCode;
		this.statusLine = statusLine; // ✅ now aligned with getStatus()
		this.ids = ids; // ✅ correct field
		this.names = names; // ✅ correct field
		this.description = description;
		this.numOfPositions = numOfPositions;
		this.statusList = statusList;
		this.isPublished = isPublished;
		this.jobTitles = jobTitles;
	}

	/**
	 * Basic constructor for CustomResponse with only HTTP status details.
	 *
	 * @param response2   The raw HTTP response
	 * @param statusCode2 The HTTP status code
	 * @param statusLine2 The HTTP status line
	 */
	public CustomResponse(Response response2, int statusCode2, String statusLine2) {
		this.response = response2;
		this.statusCode = statusCode2;
		this.statusLine = statusLine2;
	}

	public String getStatusLine() {
		return statusLine;
	}

	public List<Object> getDescription() {
		return description;
	}

	public List<Object> getNumOfPositions() {
		return numOfPositions;
	}

	public List<Object> getStatusList() {
		return statusList;
	}

	public List<Object> getIsPublished() {
		return isPublished;
	}

	public List<Object> getJobTitleIds() {
		return jobTitleIds;
	}

	public List<Object> getJobTitles() {
		return jobTitles;
	}

	public List<Object> getJobDeleted() {
		return jobDeleted;
	}

	public List<Object> getHiringManagerIds() {
		return hiringManagerIds;
	}

	public List<Object> getHiringManagerFirstNames() {
		return hiringManagerFirstNames;
	}

	public List<Object> getHiringManagerMiddleNames() {
		return hiringManagerMiddleNames;
	}

	public List<Object> getHiringManagerLastNames() {
		return hiringManagerLastNames;
	}

	public List<Object> getHiringManagerTerminationIds() {
		return hiringManagerTerminationIds;
	}

	public List<Object> getTitles() {
		return Titles;
	}

}
