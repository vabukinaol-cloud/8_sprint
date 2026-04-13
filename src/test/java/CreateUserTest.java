import client.UserClient;
import io.qameta.allure.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pojo.UserCreate;
import utils.UserGenerator;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@DisplayName("Создание пользователя")
public class CreateUserTest {
    private UserClient userClient;
    private String accessToken;

    @BeforeEach
    public void setUp() {
        userClient = new UserClient();
    }

    @AfterEach
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

    @Test
    @Description("Успешное создание уникального пользователя — код 200 и success: true")
    public void shouldCreateUniqueUser() {
        UserCreate user = UserGenerator.getRandomUser();

        accessToken = userClient.create(user)
                .statusCode(200)
                .body("success", is(true))
                .extract().path("accessToken");
    }

    @Test
    @Description("Нельзя создать пользователя, который уже зарегистрирован — код 403")
    public void shouldReturnErrorForDuplicateUser() {
        UserCreate user = UserGenerator.getRandomUser();

        accessToken = userClient.create(user)
                .statusCode(200)
                .extract().path("accessToken");

        userClient.create(user)
                .statusCode(403)
                .body("success", is(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @Description("Нельзя создать пользователя без обязательного поля email — код 403")
    public void shouldReturnErrorWhenEmailIsMissing() {
        UserCreate user = new UserCreate();
        user.setPassword("password123");
        user.setName("TestName");

        userClient.create(user)
                .statusCode(403)
                .body("success", is(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @Description("Нельзя создать пользователя без обязательного поля password — код 403")
    public void shouldReturnErrorWhenPasswordIsMissing() {
        UserCreate user = new UserCreate();
        user.setEmail(UserGenerator.getRandomUser().getEmail());
        user.setName("TestName");

        userClient.create(user)
                .statusCode(403)
                .body("success", is(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @Description("Нельзя создать пользователя без обязательного поля name — код 403")
    public void shouldReturnErrorWhenNameIsMissing() {
        UserCreate user = new UserCreate();
        user.setEmail(UserGenerator.getRandomUser().getEmail());
        user.setPassword("password123");

        userClient.create(user)
                .statusCode(403)
                .body("success", is(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
