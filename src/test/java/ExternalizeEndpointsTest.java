import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.EndpointConfig;
import utilities.PropertyUtils;
import utilities.RandomEmailGenerator;
import static org.testng.Assert.assertEquals;

public class ExternalizeEndpointsTest {


    @Test
    public void testExternalizeEndpoints() {

        String baseUrl = PropertyUtils.getProperty("base.url");

        RestAssured.baseURI = baseUrl;

        String randomEmail = RandomEmailGenerator.generateRandomEmailId();

        // Fetching the signup endpoint from our external JSON file
        String signUpEndPoint = EndpointConfig.getEndpoint("auth", "signUp");

        String requestBody = String.format("{\"email\": \"" + randomEmail + "\", \"password\": \"12345678\"}");

        // Sending a signup request and getting the response
        Response signUpResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post(signUpEndPoint);

        int statusCode = signUpResponse.getStatusCode();
        assertEquals(statusCode, 201);



    }
}
