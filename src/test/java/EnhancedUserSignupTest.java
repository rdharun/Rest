import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import utilities.PropertyUtils;
import utilities.RandomEmailGenerator;
import static org.hamcrest.MatcherAssert.assertThat;

public class EnhancedUserSignupTest {

    @Test
    public void successfullySignupUser() {
        // Load the base URL from the property file
        String baseUrl = PropertyUtils.getProperty("base.url");
        RestAssured.baseURI = baseUrl;

        // Generate a random email using the utility function
        String randomEmail = RandomEmailGenerator.generateRandomEmailId();

        // Construct the request body using the generated email
        String requestBody = String.format("{\"email\": \"%s\", \"password\": \"12345678\"}", randomEmail);

        // Send POST request and capture the response
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/auth/signup");

        // Validate the response using Hamcrest assertions
        assertThat(response.getStatusCode(), Matchers.is(201));
        assertThat(response.jsonPath().get("data"), Matchers.notNullValue());
        assertThat(response.jsonPath().get("data.user"), Matchers.notNullValue());
        assertThat(response.jsonPath().get("data.session"), Matchers.notNullValue());

        // Additional Assertions
        assertThat(response.jsonPath().getString("data.user.email"), Matchers.equalTo(randomEmail));
        assertThat(response.jsonPath().getString("data.session.token_type"), Matchers.equalTo("bearer"));
        assertThat(response.jsonPath().getString("data.session.refresh_token"), Matchers.notNullValue());
        assertThat(response.jsonPath().getString("data.user.id"), Matchers.equalTo(response.jsonPath().getString("data.session.user.id")));
        assertThat(response.jsonPath().getList("data.user.app_metadata.providers"), Matchers.contains("email"));
        assertThat(response.jsonPath().getString("data.user.aud"), Matchers.equalTo("authenticated"));
        assertThat(response.jsonPath().getString("data.user.role"), Matchers.equalTo("authenticated"));
        assertThat(response.jsonPath().getString("data.user.created_at"), Matchers.matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.*"));
        assertThat(response.jsonPath().getString("data.user.updated_at"), Matchers.matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.*"));

    }
}