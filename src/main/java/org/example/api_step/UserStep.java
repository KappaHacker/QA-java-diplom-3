package org.example.api_step;

import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.example.pojo.RegisterRequest;
import org.example.pojo.SignInRequest;

import static io.restassured.RestAssured.given;
import static org.example.constant.BaseConstant.BASE_TEST_URL;
import static org.example.constant.UserConstant.*;
import static org.example.constant.UserConstant.BASE_LOGIN_URL;

public class UserStep {
    public static final RequestSpecification REQUEST_SPECIFICATION =
            new RequestSpecBuilder()
                    .setBaseUri(BASE_TEST_URL + "/api")
                    .setBasePath(BASE_AUTH_URL)
                    .setContentType(ContentType.JSON)
                    .build();

    @Step("Создаём уникального юзера")
    public static Response createUniqueUser(RegisterRequest body) {
        return given()
                .spec(REQUEST_SPECIFICATION)
                .body(body)
                .when()
                .post(BASE_REGISTER_URL);
    }

    @Step("Удаляем пользователя")
    public static Response deleteUser(String accessToken) {
        return given()
                .spec(REQUEST_SPECIFICATION)
                .header("Authorization", accessToken)
                .when()
                .delete(BASE_USER_URL);
    }

    @Step("Выполняем авторизацию с помощью тела запроса на авторизацию")
    public static Response signInWithSignInRequest(SignInRequest body) {
        return given()
                .spec(REQUEST_SPECIFICATION)
                .body(body)
                .when()
                .post(BASE_LOGIN_URL);
    }
}
