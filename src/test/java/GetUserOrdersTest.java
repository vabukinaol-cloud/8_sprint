import client.OrderClient;
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
import static org.hamcrest.Matchers.notNullValue;

@DisplayName("Получение заказов конкретного пользователя")
public class GetUserOrdersTest {
    private UserClient userClient;
    private OrderClient orderClient;
    private String accessToken;

    @BeforeEach
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
        UserCreate user = UserGenerator.getRandomUser();
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
    @Description("Авторизованный пользователь получает список своих заказов — код 200")
    public void shouldReturnUserOrdersWhenAuthorized() {
        orderClient.getUserOrdersWithAuth(accessToken)
                .statusCode(200)
                .body("success", is(true))
                .body("orders", notNullValue());
    }

    @Test
    @Description("Неавторизованный пользователь получает ошибку 401")
    public void shouldReturnErrorWhenNotAuthorized() {
        orderClient.getUserOrdersWithoutAuth()
                .statusCode(401)
                .body("success", is(false))
                .body("message", equalTo("You should be authorised"));
    }
}
