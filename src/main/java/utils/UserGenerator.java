package utils;

import com.github.javafaker.Faker;
import pojo.UserCreate;
import java.util.UUID;

public class UserGenerator {
    private static final Faker faker = new Faker();

    public static UserCreate getRandomUser() {
        String uniqueEmail = UUID.randomUUID().toString().substring(0, 8) + "@test.com";
        return new UserCreate(
                uniqueEmail,
                faker.internet().password(8, 16),
                faker.name().firstName()
        );
    }
}
