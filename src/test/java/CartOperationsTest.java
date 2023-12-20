import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.PropertyUtils;
import utilities.RandomEmailGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CartOperationsTest {


    // https://ninjacart-tech.atlassian.net/browse/TAB-209
    @Test
    public void cartOperationsFlow() {

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

    }
}
