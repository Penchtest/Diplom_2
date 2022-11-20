package site.nomoreparties.stellarburgers.requests;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import site.nomoreparties.stellarburgers.dto.OrderRequestBody;

import static io.restassured.RestAssured.given;

public class OrderRequest extends RestRequestModel {
    @Step("Create order without auth")
    public ValidatableResponse createOrderWithoutAuth(OrderRequestBody orderRequestBody) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(orderRequestBody)
                .post("orders")
                .then();
    }

    @Step("Create order with auth")
    public ValidatableResponse createOrderWithAuth(OrderRequestBody orderRequestBody, String accessToken) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(orderRequestBody)
                .auth().oauth2(accessToken)
                .post("orders")
                .then();
    }

    @Step("Get user's orders with auth")
    public ValidatableResponse getUserOrdersWithAuth(String accessToken) {
        return given()
                .spec(getDefaultRequestSpec())
                .auth().oauth2(accessToken)
                .get("orders")
                .then();
    }

    @Step("Get user's orders without auth")
    public ValidatableResponse getUserOrdersWithoutAuth() {
        return given()
                .spec(getDefaultRequestSpec())
                .get("orders")
                .then();
    }
}
