import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class OrderListTest {

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
    @DisplayName("Authorization user can get order list")
    public void GetOrderListAuthUser() {
        ValidatableResponse responseUser = userClient.create(user);
        accessToken = responseUser.extract().path("accessToken");
        userToken = accessToken.substring(7);
        orderClient.createOrder(userToken, OrderGenerator.orderWithValidIngredients());
        ValidatableResponse responseOrder = orderClient.getOrder(userToken);
        int statusCode = responseOrder.extract().statusCode();
        boolean isCreated = responseOrder.extract().path("success");
        assertEquals("Status code is incorrect", SC_OK, statusCode);
        assertTrue("Order is not created", isCreated);
    }

    @Test
    @DisplayName("Non authorization user cannot get order list")
    public void NoGetOrderListNonAuthUser() {
        userToken = "";
        ValidatableResponse responseOrder = orderClient.getOrder(userToken);
        int statusCode = responseOrder.extract().statusCode();
        String messageError = responseOrder.extract().path("message");
        String messageExpected = "You should be authorised";
        assertEquals("Status code is incorrect", SC_UNAUTHORIZED, statusCode);
        assertEquals("Message is not true", messageExpected, messageError);
    }
}

