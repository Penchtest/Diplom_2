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
import static site.nomoreparties.stellarburgers.generator.IngredientsGenerator.*;
import static site.nomoreparties.stellarburgers.generator.RegistrationRequestBodyGenerator.getRandomRegistrationRequestBody;

public class MakeOrderTest {
    private OrderRequest orderRequest;
    private String accessToken;
    private UserRequest userRequest;

    @Before
    public void setUp() {
        orderRequest = new OrderRequest();
    }


    @After
    public void tearDown() {
        if (accessToken != null)
            userRequest.delete(accessToken)
                    .assertThat()
                    .statusCode(SC_ACCEPTED);
    }

    @Test
    @DisplayName("Order without auth should be created")
    public void orderWithoutAuthShouldBeCreated() {
        OrderRequestBody randomOrderRequestBody = getRandomOrderCorrectRequestBody();
        orderRequest.createOrderWithoutAuth(randomOrderRequestBody)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("order.number", notNullValue());

    }

    @Test
    @DisplayName("Order with auth should be created")
    public void orderWithAuthShouldBeCreated() {
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

    }

    @Test
    @DisplayName("Order with wrong ingredients should not be created")
    public void orderWithWrongIngredientsShouldNotBeCreated() {
        OrderRequestBody randomOrderRequestBody = getRandomOrderRequestBodyWrongHash();
        orderRequest.createOrderWithoutAuth(randomOrderRequestBody)
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Order without ingredients should not be created")
    public void orderWithoutIngredientsShouldNotBeCreated() {
        OrderRequestBody randomOrderRequestBody = getRandomOrderRequestBodyWithoutIngredients();
        orderRequest.createOrderWithoutAuth(randomOrderRequestBody)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }


}
