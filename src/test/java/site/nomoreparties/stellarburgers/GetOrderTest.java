package site.nomoreparties.stellarburgers;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.dto.LoginRequestBody;
import site.nomoreparties.stellarburgers.dto.OrderRequestBody;
import site.nomoreparties.stellarburgers.dto.RegistrationRequestBody;
import site.nomoreparties.stellarburgers.requests.OrderRequest;
import site.nomoreparties.stellarburgers.requests.UserRequest;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static site.nomoreparties.stellarburgers.generator.IngredientsGenerator.getRandomOrderCorrectRequestBody;
import static site.nomoreparties.stellarburgers.generator.RegistrationRequestBodyGenerator.getRandomRegistrationRequestBody;

public class GetOrderTest {
    private OrderRequest orderRequest;
    private String accessToken;
    private UserRequest userRequest;

    @Before
    public void setUp() {
        orderRequest = new OrderRequest();
    }

    @After
    public void deleteUser() {
        if (accessToken != null)
            userRequest.delete(accessToken)
                    .assertThat()
                    .statusCode(SC_ACCEPTED);
    }

    @Test
    @DisplayName("Orders with auth should be returned")
    public void ordersWithAuthShouldBeReturned() {
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

        accessToken = accessTokenFull.replace("Bearer ", "");
        OrderRequestBody randomOrderRequestBody = getRandomOrderCorrectRequestBody();
        orderRequest.createOrderWithAuth(randomOrderRequestBody, accessToken)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("order.number", notNullValue());

        orderRequest.getUserOrdersWithAuth(accessToken)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("orders", notNullValue());
    }

    @Test
    @DisplayName("Orders without auth should not be returned")
    public void ordersWithoutAuthShouldNotBeReturned() {
        userRequest = new UserRequest();
        orderRequest.getUserOrdersWithoutAuth()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"));
    }
}
