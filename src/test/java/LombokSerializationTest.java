import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.auth.SignupRequestModel;
import org.testng.annotations.Test;
import utilities.EndpointConfig;
import utilities.PropertyUtils;
import utilities.RandomEmailGenerator;

import static org.testng.Assert.assertEquals;

public class LombokSerializationTest {


    @Test
    public void testSignupWithLombokSerialization() {

        String baseUrl = PropertyUtils.getProperty("base.url");
        RestAssured.baseURI = baseUrl;

        String randomEmail = RandomEmailGenerator.generateRandomEmail();

        String signUpEndpoint = EndpointConfig.getEndpoint("auth", "signUp");

        // Using Lombok's builder pattern to construct the Signup request model
        SignupRequestModel signupRequestModel = SignupRequestModel.builder()
                .email(randomEmail)
                .password("1234453")
                .build();


        // Performing the signup operation
        Response signupResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(signupRequestModel)
                .post(signUpEndpoint);

        // Extracting the status code from the response
        int statusCode = signupResponse.getStatusCode();
        assertEquals(statusCode, 201, "Status code is not as expected");

    }
}
