import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.PropertyUtils;
import utilities.RandomEmailGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import static io.restassured.RestAssured.*;


public class RetrieveProductDetailTest {


    @Test
    public void retrieveProductDetail() {

        String baseUrl = PropertyUtils.getProperty("base.url");

        RestAssured.baseURI = baseUrl;

        String randomEmail = RandomEmailGenerator.generateRandomEmail();


        // Signup to get an access token
        Response signupResponse = given()
                .contentType("application/json")
                .body("{\"email\": \"" + randomEmail + "\", \"password\": \"12345678\"}")
                .post("/api/auth/signup");

        String accessToken = signupResponse.jsonPath().getString("data.session.access_token");

        // Fetch Product List
        Response productListResponse = given()
                .header("Authorization", "Bearer " + accessToken)
                .get("/api/products/");

        String productFirstId = productListResponse.jsonPath().getString("[0].id");

        Response productDetailsResponse = given()
                .header("Authorization", "Bearer" + accessToken)
                .get("/api/products/" + productFirstId);

        //Validation
        assertThat(productDetailsResponse.jsonPath().getString("id"), equalTo(productFirstId));
        assertThat(productDetailsResponse.getStatusCode(), equalTo(200));
    }
}


