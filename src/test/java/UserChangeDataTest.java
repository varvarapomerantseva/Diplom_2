import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserChangeDataTest {

    private User user;
    private UserClient userClient;
    private String userToken;
    private String accessToken;

    @Before
    public void setUp() {
        user = UserGenerator.getDefault();
        userClient = new UserClient();
    }

    @After
    public void tearDown() {
        userClient.deleteUser(userToken);
    }

    @Test
    @DisplayName("Authorization user can change")
    public void userAuthCanBeChangeTest() {
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken");
        userToken = accessToken.substring(7);
        ValidatableResponse changeResponse = userClient.change(UserGenerator.getUserChangeData(), userToken);
        int statusCode = changeResponse.extract().statusCode();
        boolean isCreated = changeResponse.extract().path("success");
        assertEquals("Status code is incorrect", SC_OK, statusCode);
        assertTrue("User is not created", isCreated);
    }

    @Test
    @DisplayName("Non authorization user can not change")
    public void userNonAuthCanNotBeChangeTest() {
        userToken = "";
        ValidatableResponse changeResponse = userClient.change(UserGenerator.getUserChangeData(), userToken);
        int statusCode = changeResponse.extract().statusCode();
        String messageError = changeResponse.extract().path("message");
        String messageExpected = "You should be authorised";
        assertEquals("Status code is incorrect", SC_UNAUTHORIZED, statusCode);
        assertEquals("Message is not true", messageExpected, messageError);
    }
}


