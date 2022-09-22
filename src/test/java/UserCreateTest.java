import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserCreateTest {
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
    @DisplayName("User can be created")
    public void courierCanBeCreatedTest() {
        ValidatableResponse response = userClient.create(user);
        int statusCode = response.extract().statusCode();
        boolean isCreated = response.extract().path("success");
        accessToken = response.extract().path("accessToken");
        userToken = accessToken.substring(7);
        assertEquals("Status code is incorrect", SC_OK, statusCode);
        assertTrue("User is not created", isCreated);
    }

    @Test
    @DisplayName("Cannot create two identical user")
    public void doubleUserNotCanBeCreatedTest() {
        ValidatableResponse response = userClient.create(user);
        accessToken = response.extract().path("accessToken");
        userToken = accessToken.substring(7);
        ValidatableResponse responseTwo = userClient.create(user);
        int statusCode = responseTwo.extract().statusCode();
        String messageError = responseTwo.extract().path("message");
        String messageExpected = "User already exists";
        assertEquals("Status code is incorrect", SC_FORBIDDEN, statusCode);
        assertEquals("Message is not true", messageExpected, messageError);
    }

    @Test
    @DisplayName("User is not created without email")
    public void userIsNotCreatedWithoutEmailTest() {
        ValidatableResponse response = userClient.create(UserGenerator.getWithoutEmail());

        userToken = "";
        int statusCode = response.extract().statusCode();
        String messageError = response.extract().path("message");
        String messageExpected = "Email, password and name are required fields";
        assertEquals("Status code is incorrect", SC_FORBIDDEN, statusCode);
        assertEquals("Message is not true", messageExpected, messageError);
    }

    @Test
    @DisplayName("User is not created without password")
    public void userIsNotCreatedWithoutLoginTest() {
        ValidatableResponse response = userClient.create(UserGenerator.getWithoutPassword());
        userToken = "";
        int statusCode = response.extract().statusCode();
        String messageError = response.extract().path("message");
        String messageExpected = "Email, password and name are required fields";
        assertEquals("Status code is incorrect", SC_FORBIDDEN, statusCode);
        assertEquals("Message is not true", messageExpected, messageError);
    }

    @Test
    @DisplayName("User is not created without name")
    public void userIsNotCreatedWithoutNameTest() {
        ValidatableResponse response = userClient.create(UserGenerator.getWithoutName());
        userToken = "";
        int statusCode = response.extract().statusCode();
        String messageError = response.extract().path("message");
        String messageExpected = "Email, password and name are required fields";
        assertEquals("Status code is incorrect", SC_FORBIDDEN, statusCode);
        assertEquals("Message is not true", messageExpected, messageError);
    }
}
