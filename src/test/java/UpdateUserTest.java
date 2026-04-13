import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.UserCreate;
import pojo.UserLogin;
import utils.UserGenerator;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@DisplayName("Изменение данных пользователя")
public class UpdateUserTest {
    private UserClient userClient;
    private UserCreate user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
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
    @Description("С авторизацией: изменение email пользователя — код 200 и success: true")
    public void shouldUpdateEmailWithAuth() {
        UserCreate updatedUser = UserGenerator.getRandomUser();
        Map<String, String> body = new HashMap<>();
        body.put("email", updatedUser.getEmail());

        userClient.update(body, accessToken)
                .statusCode(200)
                .body("success", is(true))
                .body("user.email", equalTo(updatedUser.getEmail().toLowerCase()));
    }

    @Test
    @Description("С авторизацией: изменение name пользователя — код 200 и success: true")
    public void shouldUpdateNameWithAuth() {
        Map<String, String> body = new HashMap<>();
        body.put("name", "NewUpdatedName");

        userClient.update(body, accessToken)
                .statusCode(200)
                .body("success", is(true))
                .body("user.name", equalTo("NewUpdatedName"));
    }

    @Test
    @Description("С авторизацией: изменение password пользователя — код 200 и success: true")
    public void shouldUpdatePasswordWithAuth() {
        Map<String, String> body = new HashMap<>();
        body.put("password", "newPassword999");

        userClient.update(body, accessToken)
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    @Description("Без авторизации: изменение email возвращает ошибку 401")
    public void shouldReturnErrorWhenUpdateEmailWithoutAuth() {
        Map<String, String> body = new HashMap<>();
        body.put("email", "newemail@test.com");

        userClient.updateWithoutAuth(body)
                .statusCode(401)
                .body("success", is(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @Description("Без авторизации: изменение name возвращает ошибку 401")
    public void shouldReturnErrorWhenUpdateNameWithoutAuth() {
        Map<String, String> body = new HashMap<>();
        body.put("name", "SomeName");

        userClient.updateWithoutAuth(body)
                .statusCode(401)
                .body("success", is(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @Description("Без авторизации: изменение password возвращает ошибку 401")
    public void shouldReturnErrorWhenUpdatePasswordWithoutAuth() {
        Map<String, String> body = new HashMap<>();
        body.put("password", "somepass123");

        userClient.updateWithoutAuth(body)
                .statusCode(401)
                .body("success", is(false))
                .body("message", equalTo("You should be authorised"));
    }
}
