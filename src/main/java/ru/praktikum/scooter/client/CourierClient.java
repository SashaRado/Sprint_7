package ru.praktikum.scooter.client;

import java.io.File;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.RestAssured;
import ru.praktikum.scooter.model.CourierId;

import static io.restassured.RestAssured.given;

public class CourierClient {

    private static final String BASE_URI = "http://qa-scooter.praktikum-services.ru";
    private static final String COURIER_URI_SUBPATH = "/api/v1/courier";
    private static final String COURIER_LOGIN_URI_SUBPATH = "/api/v1/courier/login";

    private Response getCreateCourierResponse(File body) {
        RestAssured.baseURI = BASE_URI;
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post(COURIER_URI_SUBPATH);
    }

    @Step("Получение ответа для правильного создания курьера")
    public Response getCreateCourierResponseCorrect(File body) {
        return getCreateCourierResponse(body);
    }

    @Step("Получение ответа о неправильном создании курьера при попытке создать существующего курьера")
    public Response getCreateCourierResponseWhenTryToCreateExistingCourier(File body) {
        return getCreateCourierResponse(body);
    }

    @Step("Получение ответа о неправильном создании курьера при попытке создать курьера без авторизации")
    public Response getCreateCourierResponseWhenTryToCreateCourierWithoutLogin(File body) {
        return getCreateCourierResponse(body);
    }

    @Step("Получение ответа о неправильном создании курьера при попытке создать курьера без пароля")
    public Response getCreateCourierResponseWhenTryToCreateCourierWithoutPassword(File body) {
        return getCreateCourierResponse(body);
    }

    private Response getLoginCourierResponse(File body) {
        RestAssured.baseURI = BASE_URI;
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post(COURIER_LOGIN_URI_SUBPATH);
    }

    @Step("Получение ответа для правильного входа курьера")
    public Response getLoginCourierResponseWhenCorrectLogin(File body) {
        return getLoginCourierResponse(body);
    }

    @Step("Получение ответа на неверный логин курьера, попробовать войти курьером без поля логина")
    public Response getLoginCourierResponseWhenTryToLoginWithoutLoginField(File body) {
        return getLoginCourierResponse(body);
    }

    @Step("Получение ответа о неправильном входе курьера, попробуйте войти курьером с несуществующими учетными данными")
    public Response getLoginCourierResponseWhenTryToLoginWithNotExistingCredentials(File body) {
        return getLoginCourierResponse(body);
    }

    @Step("Идентификатор курьера из ответа на логин курьера")
    public int parseCourierIdFromLoginCourierResponse(Response response) {
        CourierId courierId = response.body().as(CourierId.class);
        return courierId.getId();
    }

    private Response getDeleteCourierResponse(int id) {
        RestAssured.baseURI = BASE_URI;
        return given()
                .when()
                .delete(COURIER_URI_SUBPATH + "/" + Integer.toString(id));
    }

    @Step("Получение ответа на запрос на удаление курьера, при правильном удалении курьера")
    public Response getDeleteCourierResponseWhenCorrectDeletion(int id) {
        return getDeleteCourierResponse(id);
    }

}