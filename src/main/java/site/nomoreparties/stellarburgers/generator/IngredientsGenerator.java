package site.nomoreparties.stellarburgers.generator;

import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;
import site.nomoreparties.stellarburgers.dto.OrderRequestBody;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class IngredientsGenerator {
    @Step("Generate order data with correct ingredients")
    public static OrderRequestBody getRandomOrderCorrectRequestBody() {
        ArrayList<Map<String, String>> buns = new ArrayList<>();
        ArrayList<Map<String, String>> mains = new ArrayList<>();
        ArrayList<Map<String, String>> sauces = new ArrayList<>();

        ArrayList<Map<String, String>> ingredients = given()
                .get("https://stellarburgers.nomoreparties.site/api/ingredients")
                .then()
                .extract()
                .path("data");

        for (int i = 0; i < ingredients.size(); i++) {
            if ((ingredients.get(i).get("type")).equals("bun"))
                buns.add(ingredients.get(i));
            if ((ingredients.get(i).get("type")).equals("main"))
                mains.add(ingredients.get(i));
            if ((ingredients.get(i).get("type")).equals("sauce"))
                sauces.add(ingredients.get(i));
        }
        OrderRequestBody orderRequestBody = new OrderRequestBody();
        Random random = new Random();
        String randomBun = buns.get(random.nextInt(buns.size())).get("_id");
        String randomMain = mains.get(random.nextInt(mains.size())).get("_id");
        String randomSauce = sauces.get(random.nextInt(sauces.size())).get("_id");

        ArrayList<String> randomIngredients = new ArrayList<>();

        randomIngredients.add(randomBun);
        randomIngredients.add(randomMain);
        randomIngredients.add(randomSauce);

        orderRequestBody.setIngredients(randomIngredients);
        return orderRequestBody;
    }

    @Step("Generate order data with wrong hash of ingredients")
    public static OrderRequestBody getRandomOrderRequestBodyWrongHash() {
        ArrayList<String> randomIngredients = new ArrayList<>();
        OrderRequestBody orderRequestBody = new OrderRequestBody();
        randomIngredients.add((RandomStringUtils.randomAlphanumeric(24)).toLowerCase());
        randomIngredients.add((RandomStringUtils.randomAlphanumeric(24)).toLowerCase());
        randomIngredients.add((RandomStringUtils.randomAlphanumeric(24)).toLowerCase());
        orderRequestBody.setIngredients(randomIngredients);
        return orderRequestBody;
    }

    @Step("Generate order data without ingredients")
    public static OrderRequestBody getRandomOrderRequestBodyWithoutIngredients() {
        OrderRequestBody orderRequestBody = new OrderRequestBody();
        return orderRequestBody;
    }
}
