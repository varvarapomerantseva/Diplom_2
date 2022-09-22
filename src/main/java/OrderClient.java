import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient {

    private static final String CREATE_ORDER = "/api/orders";

    private static final String GET_ORDER = "/api/orders";

    @Step("Создание заказа")
    public ValidatableResponse createOrder(String accessToken, Order order) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .body(order)
                .when()
                .post(CREATE_ORDER)
                .then();
    }

    @Step("Получние заказов")
    public ValidatableResponse getOrder(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .when()
                .get(GET_ORDER)
                .then();
    }
}
