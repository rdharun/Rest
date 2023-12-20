import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.PropertyUtils;
import utilities.RandomEmailGenerator;
import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class ValidateAPIResponseAttributesTest {


    @Test
    public void validateAPIResponseAttributes() {

        String baseUrl = PropertyUtils.getProperty("base.url");

        RestAssured.baseURI = baseUrl;

        String randomEmail = RandomEmailGenerator.generateRandomEmailId();

        // Signup to get an access token
        Response signupResponse = given()
                .header("Content-Type", "application/json")
                .body("{\"email\": \"" + randomEmail + "\", \"password\": \"12345678\"}")
                .post("/api/auth/signup");

        String accessToken = signupResponse.jsonPath().getString("data.session.access_token");

        // Fetch products and validate response
        Response productsResponse = given()
                .header("Authorization", "Bearer " + accessToken)
                .get("/api/products/");

        Headers responseHeaders =  productsResponse.getHeaders();
        long responseTime = productsResponse.getTime();

        // Validate the Content-Type header in the response
        assertThat(responseHeaders.getValue("Content-Type"), equalTo("application/json; charset=utf-8"));


        // Validate response time
        assertThat(responseTime, lessThan(3000L));

        // Validate status and status text
        assertThat(productsResponse.getStatusCode(), equalTo(200));
        assertThat(productsResponse.getStatusLine(), containsString("OK"));

    }

}
