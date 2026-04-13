package client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import pojo.OrderCreate;
import utils.Config;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String ORDERS_PATH = "/api/orders";

    @Step("Создание заказа с авторизацией")
    public ValidatableResponse createWithAuth(OrderCreate order, String accessToken) {
        return given().log().all()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .baseUri(Config.BASE_URL)
                .body(order)
                .when()
                .post(ORDERS_PATH)
                .then().log().all();
    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse createWithoutAuth(OrderCreate order) {
        return given().log().all()
                .header("Content-type", "application/json")
                .baseUri(Config.BASE_URL)
                .body(order)
                .when()
                .post(ORDERS_PATH)
                .then().log().all();
    }

    @Step("Получение заказов авторизованного пользователя")
    public ValidatableResponse getUserOrdersWithAuth(String accessToken) {
        return given().log().all()
                .header("Authorization", accessToken)
                .baseUri(Config.BASE_URL)
                .when()
                .get(ORDERS_PATH)
                .then().log().all();
    }

    @Step("Получение заказов без авторизации")
    public ValidatableResponse getUserOrdersWithoutAuth() {
        return given().log().all()
                .baseUri(Config.BASE_URL)
                .when()
                .get(ORDERS_PATH)
                .then().log().all();
    }
}
