package site.nomoreparties.stellarburgers.requests;

import io.restassured.response.ValidatableResponse;
import site.nomoreparties.stellarburgers.dto.OrderRequestBody;

import static io.restassured.RestAssured.given;

public class OrderRequest extends RestRequestModel {
    public ValidatableResponse createOrderWithoutAuth(OrderRequestBody orderRequestBody) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(orderRequestBody)
                .post("orders")
                .then();
    }

    public ValidatableResponse createOrderWithAuth(OrderRequestBody orderRequestBody, String accessToken) {
        return given()
                .spec(getDefaultRequestSpec())
                .body(orderRequestBody)
                .auth().oauth2(accessToken)
                .post("orders")
                .then();
    }

    public ValidatableResponse getUserOrdersWithAuth(String accessToken) {
        return given()
                .spec(getDefaultRequestSpec())
                .auth().oauth2(accessToken)
                .get("orders")
                .then();
    }

    public ValidatableResponse getUserOrdersWithoutAuth() {
        return given()
                .spec(getDefaultRequestSpec())
                .get("orders")
                .then();
    }

}
