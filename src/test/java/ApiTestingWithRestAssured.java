import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.testng.annotations.Test;

public class ApiTestingWithRestAssured {


    @Test
    public void fetchAndLogStatusCode() {
        String url = "https://reqres.in/api/users?page=2";
        Response response =  RestAssured.get(url);
        ResponseBody body = response.body();
        int statusCode = response.getStatusCode();
        System.out.println(body.asPrettyString());
        System.out.println("StatusCode " + statusCode);
    }

    @Test
    public void fetchAndLog() {
        // Specify the URL for the API call
//        String apiUrl = "https://www.apicademy.dev/";
        RestAssured.baseURI = "https://www.apicademy.dev/";

        // Send a GET request and get the response
//        Response response = RestAssured.get(apiUrl);

        // Send a GET request and get the response
        Response response = RestAssured.given().get();

        // Get the status code from the response
        int statusCode = response.getStatusCode();

        // Log the status code
        System.out.println("Status Code: " + statusCode);
    }
}
