import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.PropertyUtils;
import utilities.RandomEmailGenerator;
import org.hamcrest.Matchers;
import static org.hamcrest.MatcherAssert.assertThat;



public class GetProductsPaginatedTest {

    @Test
    public void retrivePaginatedListOfProducts() {

        String base_url = PropertyUtils.getProperty("base.url");

        RestAssured.baseURI = base_url;

        String randomEmail = RandomEmailGenerator.generateRandomEmail();

        String requestBody = String.format("{\"email\": \"%s\", \"password\": \"12345678\"}", randomEmail);

        //Sign up to get the access token

        Response signUpResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post("/api/auth/signup");

        JsonPath jsonPath = signUpResponse.jsonPath();
        String accessToken = jsonPath.getString("data.session.access_token");

        // Make a GET request with pagination
        Response productsResponse = RestAssured.given()
                .header("Authorization", "Bearer " + accessToken)
                .queryParam("page", 1)
                .queryParam("limit", 2)
                .get("/api/products/");

        // Validate the response
        jsonPath = productsResponse.jsonPath();
        int productCount = jsonPath.getList("$").size();

        System.out.println(productsResponse.getBody().asPrettyString());


        // Validating the status code
        assertThat(productsResponse.getStatusCode(), Matchers.equalTo(200));
        // Validating the length of returned products array with limited value
        assertThat(productCount, Matchers.equalTo(2));
    }
}
