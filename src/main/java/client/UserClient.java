package client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import pojo.UserCreate;
import pojo.UserLogin;
import utils.Config;

import static io.restassured.RestAssured.given;

public class UserClient {
    private static final String AUTH_PATH = "/api/auth";

    @Step("Создание пользователя {user.email}")
    public ValidatableResponse create(UserCreate user) {
        return given().log().all()
                .header("Content-type", "application/json")
                .baseUri(Config.BASE_URL)
                .body(user)
                .when()
                .post(AUTH_PATH + "/register")
                .then().log().all();
    }

    @Step("Логин пользователя {credentials.email}")
    public ValidatableResponse login(UserLogin credentials) {
        return given().log().all()
                .header("Content-type", "application/json")
                .baseUri(Config.BASE_URL)
                .body(credentials)
                .when()
                .post(AUTH_PATH + "/login")
                .then().log().all();
    }

    @Step("Обновление данных пользователя с авторизацией")
    public ValidatableResponse update(Object body, String accessToken) {
        return given().log().all()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .baseUri(Config.BASE_URL)
                .body(body)
                .when()
                .patch(AUTH_PATH + "/user")
                .then().log().all();
    }

    @Step("Обновление данных пользователя без авторизации")
    public ValidatableResponse updateWithoutAuth(Object body) {
        return given().log().all()
                .header("Content-type", "application/json")
                .baseUri(Config.BASE_URL)
                .body(body)
                .when()
                .patch(AUTH_PATH + "/user")
                .then().log().all();
    }

    @Step("Удаление пользователя")
    public void delete(String accessToken) {
        given().log().all()
                .header("Authorization", accessToken)
                .baseUri(Config.BASE_URL)
                .when()
                .delete(AUTH_PATH + "/user")
                .then().log().all();
    }
}
