package site.nomoreparties.stellarburgers;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.dto.LoginRequestBody;
import site.nomoreparties.stellarburgers.dto.RegistrationRequestBody;
import site.nomoreparties.stellarburgers.requests.UserRequest;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static site.nomoreparties.stellarburgers.generator.RegistrationRequestBodyGenerator.getRandomRegistrationRequestBody;

public class LoginTest {
    private UserRequest userRequest;
    private String accessToken;

    @Before
    public void setUp() {
        userRequest = new UserRequest();
    }

    @After
    public void deleteUser() {
        if (accessToken != null)
            userRequest.delete(accessToken)
                    .assertThat()
                    .statusCode(SC_ACCEPTED);
    }

    @Test
    @DisplayName("Existing user login should be successful")
    public void existingUserLoginShouldBeSuccessful() {

        RegistrationRequestBody randomRegistrationUserBody = getRandomRegistrationRequestBody();

        userRequest.createUser(randomRegistrationUserBody)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));

        LoginRequestBody loginRequestBody = new LoginRequestBody();
        loginRequestBody.setEmail(randomRegistrationUserBody.getEmail());
        loginRequestBody.setPassword(randomRegistrationUserBody.getPassword());

        String accessTokenFull = userRequest.login(loginRequestBody)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true))
                .extract()
                .path("accessToken");

        accessToken = accessTokenFull.replace("Bearer ", "");
    }

    @Test
    @DisplayName("Not existing user login should not be successful")
    public void notExistingUserLoginShouldNotBeSuccessful() {
        LoginRequestBody loginRequestBody = new LoginRequestBody();
        Faker faker = new Faker();
        loginRequestBody.setEmail(faker.internet().emailAddress());
        loginRequestBody.setPassword(RandomStringUtils.randomAlphanumeric(7));

        userRequest.login(loginRequestBody)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));

    }
}
