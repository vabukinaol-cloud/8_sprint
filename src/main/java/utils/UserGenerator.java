package utils;

import com.github.javafaker.Faker;
import pojo.UserCreate;

public class UserGenerator {
    private static final Faker faker = new Faker();

    public static UserCreate getRandomUser() {
        return new UserCreate(
                faker.internet().emailAddress(),
                faker.internet().password(8, 16),
                faker.name().firstName()
        );
    }
}
