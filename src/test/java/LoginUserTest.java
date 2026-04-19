import client.UserClient;
import io.qameta.allure.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pojo.UserCreate;
import pojo.UserLogin;
import utils.UserGenerator;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@DisplayName("Логин пользователя")
public class LoginUserTest {
    private UserClient userClient;
    private UserCreate user;
    private String accessToken;

    @BeforeEach
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandomUser();
        accessToken = userClient.create(user)
                .statusCode(200)
                .extract().path("accessToken");
    }

    @AfterEach
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

    @Test
    @Description("Успешный логин под существующим пользователем — код 200 и accessToken в ответе")
    public void shouldLoginWithValidCredentials() {
        accessToken = userClient.login(new UserLogin(user.getEmail(), user.getPassword()))
                .statusCode(200)
                .body("success", is(true))
                .body("accessToken", notNullValue())
                .extract().path("accessToken");
    }

    @Test
    @Description("Логин с неверным паролем — код 401 и сообщение об ошибке")
    public void shouldReturnErrorWithWrongPassword() {
        userClient.login(new UserLogin(user.getEmail(), "wrong_password_999"))
                .statusCode(401)
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @Description("Логин с неверным email — код 401 и сообщение об ошибке")
    public void shouldReturnErrorWithWrongEmail() {
        userClient.login(new UserLogin("nonexistent@fake.com", user.getPassword()))
                .statusCode(401)
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}
