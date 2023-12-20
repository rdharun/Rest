import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.PropertyUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;

import org.hamcrest.Matchers;


public class ValidateProductsNoAuthTest {

    @Test
    public void testGetProductsWithoutAuthHeader() {

        String baseURL = PropertyUtils.getProperty("base.url");

        RestAssured.baseURI = baseURL;

        Response response = RestAssured.given().get("/api/products/");

//        int statusCode = response.getStatusCode();
//        assertEquals(statusCode, 400);

        // Non-BDD Assertions using Hamcrest
        assertThat(response.getStatusCode(), Matchers.is(400));
        assertThat(response.jsonPath().getString("message"), Matchers.equalTo("Authorization header is missing."));
    }
}
