import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.PropertyUtils;
import utilities.RandomEmailGenerator;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
public class UserProfileOperationsTest {

    @Test
    public void testUserProfileCreationAndPartialUpdate() {
        // Read base URL from the property file
        String baseUrl = PropertyUtils.getProperty("base.url");
        RestAssured.baseURI = baseUrl;

        // Generate a random email using the utility function
        String randomEmail = RandomEmailGenerator.generateRandomEmail();

        // User Signup
        Response signupResponse = given()
                .contentType("application/json")
                .body("{\"email\": \"" + randomEmail + "\", \"password\": \"12345678\"}")
                .post("/api/auth/signup");

        String accessToken = signupResponse.jsonPath().getString("data.session.access_token");

        // Step 2: Create User Profile
        Response createProfileResponse = given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body("{\"first_name\": \"James\", \"last_name\": \"Jenny\", \"address\": \"1st cross, church street, London\", \"mobile_number\": \"1234567890\"}")
                .post("/api/profile");

        assertThat(createProfileResponse.getStatusCode(), equalTo(201));
        assertThat(createProfileResponse.jsonPath().getString("first_name"), equalTo("James"));
        assertThat(createProfileResponse.jsonPath().getString("last_name"), equalTo("Jenny"));
        assertThat(createProfileResponse.jsonPath().getString("mobile_number"), equalTo("1234567890"));

        // Step 3: Partially Update User Profile using PATCH
        Response partialUpdateResponse = given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body("{\"first_name\": \"Kane\", \"last_name\": \"Jennier\"}")
                .patch("/api/profile");

        assertThat(partialUpdateResponse.getStatusCode(), equalTo(200));
        assertThat(partialUpdateResponse.jsonPath().getString("first_name"), equalTo("Kane"));
        assertThat(partialUpdateResponse.jsonPath().getString("last_name"), equalTo("Jennier"));
        assertThat(partialUpdateResponse.jsonPath().getString("mobile_number"), equalTo("1234567890")); // Validate that mobile_number remains unchanged
    }
}
