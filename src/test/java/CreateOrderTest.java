import client.IngredientClient;
import client.OrderClient;
import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pojo.OrderCreate;
import pojo.UserCreate;
import utils.UserGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@DisplayName("Создание заказа")
public class CreateOrderTest {
    private static List<String> ingredientIds;

    private UserClient userClient;
    private OrderClient orderClient;
    private UserCreate user;
    private String accessToken;

    @BeforeClass
    public static void setUpIngredients() {
        ingredientIds = new IngredientClient().getIngredients()
                .statusCode(200)
                .extract().jsonPath().getList("data._id", String.class);
    }

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
        user = UserGenerator.getRandomUser();
        accessToken = userClient.create(user)
                .statusCode(200)
                .extract().path("accessToken");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

    @Test
    @Description("С авторизацией и ингредиентами — код 200 и success: true")
    public void shouldCreateOrderWithAuthAndIngredients() {
        List<String> selected = Arrays.asList(ingredientIds.get(0), ingredientIds.get(1));
        orderClient.createWithAuth(new OrderCreate(selected), accessToken)
                .statusCode(200)
                .body("success", is(true))
                .body("order", notNullValue());
    }

    @Test
    @Description("Без авторизации с ингредиентами — код 200 и success: true")
    public void shouldCreateOrderWithoutAuthAndWithIngredients() {
        List<String> selected = Arrays.asList(ingredientIds.get(0), ingredientIds.get(1));
        orderClient.createWithoutAuth(new OrderCreate(selected))
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    @Description("С авторизацией без ингредиентов — код 400")
    public void shouldReturnErrorWhenNoIngredientsWithAuth() {
        orderClient.createWithAuth(new OrderCreate(new ArrayList<>()), accessToken)
                .statusCode(400)
                .body("success", is(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @Description("Без авторизации без ингредиентов — код 400")
    public void shouldReturnErrorWhenNoIngredientsWithoutAuth() {
        orderClient.createWithoutAuth(new OrderCreate(new ArrayList<>()))
                .statusCode(400)
                .body("success", is(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @Description("С авторизацией и неверным хешем ингредиентов — код 500")
    public void shouldReturnErrorWithInvalidIngredientHashWithAuth() {
        List<String> invalidIds = Collections.singletonList("invalid_hash_000000000000000000000000");
        orderClient.createWithAuth(new OrderCreate(invalidIds), accessToken)
                .statusCode(500);
    }

    @Test
    @Description("Без авторизации и неверным хешем ингредиентов — код 500")
    public void shouldReturnErrorWithInvalidIngredientHashWithoutAuth() {
        List<String> invalidIds = Collections.singletonList("invalid_hash_000000000000000000000000");
        orderClient.createWithoutAuth(new OrderCreate(invalidIds))
                .statusCode(500);
    }
}
