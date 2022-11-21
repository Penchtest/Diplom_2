package site.nomoreparties.stellarburgers.generator;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;
import site.nomoreparties.stellarburgers.dto.RegistrationRequestBody;

public class RegistrationRequestBodyGenerator {
    @Step("Generate correct user data for registration")
    public static RegistrationRequestBody getRandomRegistrationRequestBody(){
        RegistrationRequestBody registrationRequestBody = new RegistrationRequestBody();
        Faker faker = new Faker();
        registrationRequestBody.setEmail(faker.internet().emailAddress());
        registrationRequestBody.setPassword(RandomStringUtils.randomAlphanumeric(7));
        registrationRequestBody.setName(faker.name().name());
        return registrationRequestBody;
    }

    @Step("Generate user data without password")
    public static RegistrationRequestBody getRandomRegistrationRequestBodyWithoutPassword(){
        RegistrationRequestBody registrationRequestBody = new RegistrationRequestBody();
        Faker faker = new Faker();
        registrationRequestBody.setEmail(faker.internet().emailAddress());
        registrationRequestBody.setName(faker.name().name());
        return registrationRequestBody;
    }

    @Step("Generate user data without email")
    public static RegistrationRequestBody getRandomRegistrationRequestBodyWithoutEmail(){
        RegistrationRequestBody registrationRequestBody = new RegistrationRequestBody();
        Faker faker = new Faker();
        registrationRequestBody.setPassword(RandomStringUtils.randomAlphanumeric(7));
        registrationRequestBody.setName(faker.name().name());
        return registrationRequestBody;
    }

    @Step("Generate user data without name")
    public static RegistrationRequestBody getRandomRegistrationRequestBodyWithoutName(){
        RegistrationRequestBody registrationRequestBody = new RegistrationRequestBody();
        Faker faker = new Faker();
        registrationRequestBody.setPassword(RandomStringUtils.randomAlphanumeric(7));
        registrationRequestBody.setEmail(faker.internet().emailAddress());
        return registrationRequestBody;
    }
}
