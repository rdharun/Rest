import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utilities.PropertyUtils;
import utilities.RandomEmailGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
public class CartOperationsAddAndUpdateCartItemTest {

    @Test
    public void cartOperationsAddAndUpdateCartItemTest() {

        String baseUrl = PropertyUtils.getProperty("base.url");

        RestAssured.baseURI = baseUrl;

        // Generate a random email using the utility function
        String randomEmail = RandomEmailGenerator.generateRandomEmail();

        // User Signup
        Response signupResponse = RestAssured.given()
                .contentType("application/json")
                .body("{\"email\": \"" + randomEmail + "\", \"password\": \"12345678\"}")
                .post("/api/auth/signup");

        String accessToken = signupResponse.jsonPath().getString("data.session.access_token");

        // Step 2: List Products
        Response productsResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + accessToken)
                .get("/api/products/");

        // Step 3: Create Cart
        Response cartCreationResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + accessToken)
                .post("/api/cart");

        String cartId = cartCreationResponse.jsonPath().getString("cart_id");

        // Step 4: Add Item to Cart and Update It
        String product1Id = productsResponse.jsonPath().getString("[0].id");
        String product2Id = productsResponse.jsonPath().getString("[1].id");

        Response addItemResponse = RestAssured.given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body("{\"product_id\": \"" + product1Id + "\", \"quantity\": 10}")
                .post("/api/cart/" + cartId + "/items");

        String cartItemId = addItemResponse.jsonPath().getString("cart_item_id");
        Response updateItemResponse = RestAssured.given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body("{\"product_id\": \"" + product2Id + "\", \"quantity\": 20}")
                .put("/api/cart/" + cartId + "/items/" + cartItemId);

        assertThat(updateItemResponse.jsonPath().getString("product_id"), equalTo(product2Id));
        assertThat(updateItemResponse.jsonPath().getInt("quantity"), equalTo(20));

    }

}
