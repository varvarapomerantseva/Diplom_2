import io.restassured.http.Header;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.oauth2;

public class UserClient extends RestClient {
    private static final String USER_PATH = "/api/auth/register";

    private static final String LOGIN_PATH = "/api/auth/login";
    private static final String DELETE_PATH = "/api/auth/user";
    private static final String CHANGE_PATH= "/api/auth/user";

    public ValidatableResponse create(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_PATH)
                .then();

    }

    public ValidatableResponse login(UserCredentials credentials) {
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(LOGIN_PATH)
                .then();

    }

    public ValidatableResponse change(User user, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .body(user)
                .when()
                .patch(CHANGE_PATH)
                .then();
    }
    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(accessToken)
                .when()
                .delete(DELETE_PATH)
                .then();
    }
}

