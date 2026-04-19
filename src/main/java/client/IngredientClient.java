package client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import utils.Config;

import static io.restassured.RestAssured.given;

public class IngredientClient {
    private static final String INGREDIENTS_PATH = "/api/ingredients";

    @Step("Получение списка ингредиентов")
    public ValidatableResponse getIngredients() {
        return given().log().all()
                .baseUri(Config.BASE_URL)
                .when()
                .get(INGREDIENTS_PATH)
                .then().log().all();
    }
}
