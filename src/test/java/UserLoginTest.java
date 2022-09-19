import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class UserLoginTest {

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
    @DisplayName("User can login")
    public void userCanBeLoginTest() {
        ValidatableResponse response = userClient.create(user);
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        accessToken = response.extract().path("accessToken");
        userToken = accessToken.substring(7);
        int statusCode = loginResponse.extract().statusCode();
        boolean isCreated = loginResponse.extract().path("success");

        assertEquals("Status code is incorrect", SC_OK, statusCode);
        assertTrue("User is not login", isCreated);
    }
        @Test
        @DisplayName("User is not login nonexistent user")
        public void userIsNotLoginNonexistentUserTest() {
            ValidatableResponse loginResponse = userClient.login(UserCredentials.from(UserGenerator.getNonexistentUser()));
            userToken = "";
            int statusCode = loginResponse.extract().statusCode();
            String messageError = loginResponse.extract().path("message");
            String messageExpected = "email or password are incorrect";
            assertEquals("Status code is incorrect", SC_UNAUTHORIZED, statusCode);
            assertEquals("Message is not true", messageExpected, messageError);

        }

    }

