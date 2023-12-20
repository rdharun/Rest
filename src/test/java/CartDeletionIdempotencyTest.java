import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.PropertyUtils;
import utilities.RandomEmailGenerator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CartDeletionIdempotencyTest {

    @Test
    public void verifyCartDeletionAndIdempotency() {

        String baseUrl = PropertyUtils.getProperty("base.url");

        RestAssured.baseURI = baseUrl;

        String randomEmail = RandomEmailGenerator.generateRandomEmailId();

        Response signUpResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\"email\": \"" + randomEmail + "\", \"password\": \"12345678\"}")
                .post("/api/auth/signup");

//        System.out.println("Sign Up Response Body: " + signUpResponse.getBody().asPrettyString());

        String accessToken = signUpResponse.jsonPath().getString("data.session.access_token");

        Response cartCreationResponse = RestAssured.given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + accessToken)

                .post("/api/cart/");


        System.out.println("Cart Creation Response Body: " + cartCreationResponse.getBody().asPrettyString());

        String cartId = cartCreationResponse.jsonPath().getString("cart_id");

        Response cartDeleteResponse = RestAssured.given()
                .header("Authorization", "Bearer " + accessToken)
                .delete("/api/cart/" + cartId);

        assertThat(cartDeleteResponse.getStatusCode(), equalTo(204));

        Response cartDeletionIdempotentResponse = RestAssured.given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + accessToken)
                .delete("/api/cart/" + cartId);

        // Validate Idempotent Cart Deletion
        assertThat(cartDeleteResponse.getStatusCode(), equalTo(204));

        Response cartRetrivalResponse = RestAssured.given()
                .header("Authorization", "Bearer " + accessToken)
                .get("/api/cart");

        // Validate Cart Retrieval Response
        assertThat(cartRetrivalResponse.getStatusCode(), equalTo(200));  // Assuming 200 for a missing cart
        assertThat(cartRetrivalResponse.jsonPath().getString("message"), equalTo("No cart found"));
    }
}
