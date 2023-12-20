import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;
import utilities.PropertyUtils;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import org.hamcrest.Matchers;
import utilities.RandomEmailGenerator;

import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.util.random.RandomGenerator;

public class UserSignUpTest {

    @Test
    public void validateSignUpTest() {
        // Read base URL from the property file
        String base_url = PropertyUtils.getProperty("base.url");

        RestAssured.baseURI = base_url;

        // Generate random email and password
//        String randomEmail = "test" + RandomStringUtils.randomNumeric(5) + "@example.com";
        String randomPassword = RandomStringUtils.randomAlphanumeric(7);
        String randomEmail = RandomEmailGenerator.generateRandomEmail();


//        File file = new File("/Users/testvagrant_1/Desktop/Intellij workspace/RestAssured/src/main/resources/input.json");

        // Send a POST request and get the response
        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
//                .body(file)
                .body("\n" +
                        "{\n" +
                        "   \"email\":\"" + randomEmail + "\",\n" +
                        "   \"password\":\"" + randomPassword + "\"\n" +
                        "}\n")
                .when()
                .post("/api/auth/signup");

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, 201);

//        // Validate response body properties
//        assertNotNull(response.jsonPath().get("data"));        // Assert 'data' field is not null
//        assertNotNull(response.jsonPath().get("data.user"));   // Assert 'user' field inside 'data' is not null
//        assertNotNull(response.jsonPath().get("data.session")); // Assert 'session' field inside 'data' is not null

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

