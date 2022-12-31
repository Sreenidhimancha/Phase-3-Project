package restAPI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EndToEndTest {
	Response response;
	RequestSpecification request;
	Map<String,Object> MapObj = new HashMap<String,Object>();
	String baseUri="http://localhost:3000";
	@Test
	public void test1() {
		System.out.println("Fetching All the Employees");
		response=GetAllEmployees();
		Assert.assertEquals(200, response.statusCode());
		
		//Creating the Employee by Name John
		response=CreateEmployee("John" , "8000");
		JsonPath jpath1= response.jsonPath();
		int empid=jpath1.get("id");
		//Validating the Status Code 201	
		Assert.assertEquals(201, response.statusCode());
	
		//Fetching the Single Employee Details	
		response=GetSingleEmployee(empid);
		JsonPath jpath2=response.jsonPath();
		String singlename=jpath2.get("name");
		System.out.println("The name is: " + singlename);
		Assert.assertEquals(singlename, "John");
		//Validating the Status Code 200
		Assert.assertEquals(200, response.statusCode());
		
		//Updating the Employee
		response=UpdatedEmployee(empid, "Smith", "8000");
		System.out.println(response.getBody().asString());
		//Validating the Status Code 200
		Assert.assertEquals(200, response.statusCode());
		
		//Fetching the Single Employee Details
		response=GetSingleEmployee(empid);
		JsonPath jpath3=response.jsonPath();
		String names=jpath3.get("name");
		System.out.println("The name is: " + names);
		Assert.assertEquals(names, "Smith");
		//Validating the Status Code 200
		Assert.assertEquals(200, response.statusCode());
		
		//Deleting the Employee
		System.out.println("Delete the Employee Created");
		response= DeleteEmployee(empid);
		//Validating the Status Code 200
		Assert.assertEquals(200, response.statusCode());
		
		//Fetching the Single Employee Details	
		response=GetSingleEmployee(empid);
		//Validating the Status Code 404
		Assert.assertEquals(404, response.statusCode());
		
		System.out.println("All the Employees after updating");
		response=GetAllEmployees();
		JsonPath jpath4=response.jsonPath();
		List<String> Id=jpath4.get("id");
		Assert.assertFalse(Id.contains(String.valueOf(empid)));		
	}
	
	public Response GetAllEmployees() {
		RestAssured.baseURI=this.baseUri;
		request=RestAssured.given();
		response=request.get("employees");
		System.out.println(response.getBody().asString());
		return response;	
		
	}
	public Response GetSingleEmployee(int empId) {
		RestAssured.baseURI=this.baseUri;
		request=RestAssured.given();
		response =request.get("employees/"+empId); 
		return response;
}
	public Response CreateEmployee(String Name, String Salary) {
		RestAssured.baseURI=this.baseUri;
		request=RestAssured.given();
		MapObj.put("name", Name);
		MapObj.put("salary", Salary);
		response=request.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(MapObj)
				.post("employees/create");
		return response;
	}
	
	public Response UpdatedEmployee(int empId, String Name, String Salary) {
		RestAssured.baseURI=this.baseUri;
		request=RestAssured.given();
		MapObj.put("id", empId);
		MapObj.put("name", Name);
		MapObj.put("salary", Salary);
		response=request.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(MapObj)
				.patch("employees/"+empId);
		return response;
	}
	public Response DeleteEmployee(int empId) {
		RestAssured.baseURI=this.baseUri;
		request=RestAssured.given();
		return response=request.delete("employees/"+empId);
		
		
	}
	
	
}

	
