import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Objects;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class OrderCreateTest {
    private OrderClient orderClient;
    private User user;
    private UserClient userClient;
    private String userToken;
    private String accessToken;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        user = UserGenerator.getDefault();
        userClient = new UserClient();
    }

    @After
    public void tearDown() {
        userClient.deleteUser(userToken);
    }

    @Test
    @DisplayName("Authorization user can create order")
    public void orderCreateAuthUser() {
        ValidatableResponse responseUser = userClient.create(user);
        accessToken = responseUser.extract().path("accessToken");
        userToken = accessToken.substring(7);
        ValidatableResponse responseOrder = orderClient.createOrder(userToken, OrderGenerator.orderWithValidIngredients());
        int statusCode = responseOrder.extract().statusCode();
        boolean isCreated = responseOrder.extract().path("success");
        int number = responseOrder.extract().path("order.number");
        assertEquals("Status code is incorrect", SC_OK, statusCode);
        assertTrue("Order is not created", isCreated);
        assertNotEquals("No order number", 0, number);
    }

    @Test
    @DisplayName("Non authorization user cannot create order")
    public void orderNotCreateNonAuthUser() {
        userToken = "";
        ValidatableResponse responseOrder = orderClient.createOrder(userToken, OrderGenerator.orderWithValidIngredients());
        int statusCode = responseOrder.extract().statusCode();
        assertEquals("Status code is incorrect", SC_UNAUTHORIZED, statusCode);
    }

    @Test
    @DisplayName("Order not create without ingredients")
    public void orderNotCreateWithoutIng() {
        ValidatableResponse responseUser = userClient.create(user);
        accessToken = responseUser.extract().path("accessToken");
        userToken = accessToken.substring(7);
        ValidatableResponse responseOrder = orderClient.createOrder(userToken, OrderGenerator.orderWithoutIngredients());
        int statusCode = responseOrder.extract().statusCode();
        String messageError = responseOrder.extract().path("message");
        String messageExpected = "Ingredient ids must be provided";
        assertEquals("Status code is incorrect", SC_BAD_REQUEST, statusCode);
        assertEquals("Message is not true", messageExpected, messageError);
    }

    @Test
    @DisplayName("Order not create with invalid ingredients")
    public void orderNotCreateWithInvalidIng() {
        ValidatableResponse responseUser = userClient.create(user);
        accessToken = responseUser.extract().path("accessToken");
        userToken = accessToken.substring(7);
        ValidatableResponse responseOrder = orderClient.createOrder(userToken, OrderGenerator.orderWithInvalidIngredients());
        int statusCode = responseOrder.extract().statusCode();
        assertEquals("Status code is incorrect", SC_INTERNAL_SERVER_ERROR, statusCode);
    }
}

