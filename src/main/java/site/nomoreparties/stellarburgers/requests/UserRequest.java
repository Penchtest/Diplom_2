package site.nomoreparties.stellarburgers.requests;

import io.restassured.response.ValidatableResponse;
import site.nomoreparties.stellarburgers.dto.ChangeUserDataRequestBody;
import site.nomoreparties.stellarburgers.dto.LoginRequestBody;
import site.nomoreparties.stellarburgers.dto.RegistrationRequestBody;

import static io.restassured.RestAssured.given;

public class UserRequest extends RestRequestModel {
    public ValidatableResponse createUser(RegistrationRequestBody registrationRequestBody) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(registrationRequestBody)
                .post("auth/register")
                .then();
    }

    public ValidatableResponse login(LoginRequestBody loginRequestBody) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(loginRequestBody)
                .post("auth/login")
                .then();
    }

    public ValidatableResponse changeUserDataWithAuth(ChangeUserDataRequestBody changeUserDataRequestBody, String accessToken) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(changeUserDataRequestBody)
                .auth().oauth2(accessToken)
                .patch("auth/user")
                .then();
    }

    public ValidatableResponse changeUserDataWithoutAuth(ChangeUserDataRequestBody changeUserDataRequestBody) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(changeUserDataRequestBody)
                .patch("auth/user")
                .then();
    }



    public ValidatableResponse delete(String accessToken) {
        return given()
                .spec(getDefaultRequestSpec())
                .auth().oauth2(accessToken)
                .delete("auth/user")
                .then();
    }

}
