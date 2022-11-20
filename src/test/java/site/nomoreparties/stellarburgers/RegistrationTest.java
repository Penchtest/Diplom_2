package site.nomoreparties.stellarburgers;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.dto.LoginRequestBody;
import site.nomoreparties.stellarburgers.dto.RegistrationRequestBody;
import site.nomoreparties.stellarburgers.requests.UserRequest;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static site.nomoreparties.stellarburgers.generator.RegistrationRequestBodyGenerator.*;

public class RegistrationTest {
    private UserRequest userRequest;
    private String accessToken;

    @Before
    public void setUp() {
        userRequest = new UserRequest();
    }

    @After
    public void tearDown() {
        if (accessToken != null)
            userRequest.delete(accessToken)
                    .assertThat()
                    .statusCode(SC_ACCEPTED);
    }

    @Test
    @DisplayName("User should be created")
    public void userShouldBeCreated() {
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
    @DisplayName("Existing user should not be created")
    public void existingUserShouldBeCreated() {
        RegistrationRequestBody randomRegistrationUserBody = getRandomRegistrationRequestBody();

        userRequest.createUser(randomRegistrationUserBody)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true));
        userRequest.createUser(randomRegistrationUserBody)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("User should not be created without password")
    public void userShouldNotBeCreatedWithoutPassword() {
        RegistrationRequestBody randomRegistrationUserBody = getRandomRegistrationRequestBodyWithoutPassword();

        userRequest.createUser(randomRegistrationUserBody)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("User should not be created without name")
    public void userShouldNotBeCreatedWithoutName() {
        RegistrationRequestBody randomRegistrationUserBody = getRandomRegistrationRequestBodyWithoutName();

        userRequest.createUser(randomRegistrationUserBody)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("User should not be created without email")
    public void userShouldNotBeCreatedWithoutEmail() {
        RegistrationRequestBody randomRegistrationUserBody = getRandomRegistrationRequestBodyWithoutEmail();

        userRequest.createUser(randomRegistrationUserBody)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
