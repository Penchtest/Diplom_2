package site.nomoreparties.stellarburgers;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import site.nomoreparties.stellarburgers.dto.ChangeUserDataRequestBody;
import site.nomoreparties.stellarburgers.dto.LoginRequestBody;
import site.nomoreparties.stellarburgers.dto.RegistrationRequestBody;
import site.nomoreparties.stellarburgers.requests.UserRequest;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static site.nomoreparties.stellarburgers.generator.RegistrationRequestBodyGenerator.getRandomRegistrationRequestBody;

public class ChangeUserDataTest {
    private static UserRequest userRequest;
    private static String accessToken;

    @BeforeClass
    public static void createUser() {
        userRequest = new UserRequest();

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
        if (accessTokenFull != null)
            accessToken = accessTokenFull.replace("Bearer ", "");
    }


    @AfterClass
    public static void deleteUser() {
        if (accessToken != null)
            userRequest.delete(accessToken)
                    .assertThat()
                    .statusCode(SC_ACCEPTED);
    }

    @Test
    @DisplayName("User data should change with auth")
    public void userDataShouldChangeWithAuth() {
        ChangeUserDataRequestBody changeUserDataRequestBody = new ChangeUserDataRequestBody();
        Faker faker = new Faker();
        changeUserDataRequestBody.setEmail(faker.internet().emailAddress());
        changeUserDataRequestBody.setName(faker.name().name());

        userRequest.changeUserDataWithAuth(changeUserDataRequestBody, accessToken)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("user.email", equalTo(changeUserDataRequestBody.getEmail()))
                .and()
                .body("user.name", equalTo(changeUserDataRequestBody.getName()));
    }

    @Test
    @DisplayName("User data should not change without auth")
    public void userDataShouldNotChangeWithoutAuth() {
        ChangeUserDataRequestBody changeUserDataRequestBody = new ChangeUserDataRequestBody();
        Faker faker = new Faker();
        changeUserDataRequestBody.setEmail(faker.internet().emailAddress());
        changeUserDataRequestBody.setName(faker.name().name());

        userRequest.changeUserDataWithoutAuth(changeUserDataRequestBody)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"));
    }


}
