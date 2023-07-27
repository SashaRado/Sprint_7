package ru.praktikum.scooter.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.RestAssured;
import ru.praktikum.scooter.model.CourierId;

import static io.restassured.RestAssured.given;

public class CourierClient {

    private static final String BASE_URI = "http://qa-scooter.praktikum-services.ru";
    private static final String COURIER_URI_SUBPATH = "/api/v1/courier";
    private static final String COURIER_LOGIN_URI_SUBPATH = "/api/v1/courier/login";
    private static final String COURIER_DELETE = "/api/v1/courier/:id";

    // Метод для отправки JSON-строки в теле запроса
    private Response postJson(String uri, String jsonBody) {
        RestAssured.baseURI = BASE_URI;
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(jsonBody)
                .when()
                .post(uri);
    }

    @Step("Получение ответа при создании курьера")
    public Response getCreateCourierResponse(String jsonBody) {
        return postJson(COURIER_URI_SUBPATH, jsonBody);
    }

    @Step("Получение ответа для входа курьера")
    public Response getLoginCourierResponse(String jsonBody) {
        return postJson(COURIER_LOGIN_URI_SUBPATH, jsonBody);
    }

    @Step("Идентификатор курьера из ответа на логин курьера")
    public int parseCourierIdFromLoginCourierResponse(Response response) {
        CourierId courierId = response.body().as(CourierId.class);
        return courierId.getId();
    }

    @Step("Получение ответа на запрос на удаление курьера, при правильном удалении курьера")
    public Response getDeleteCourierResponseWhenCorrectDeletion(int id) {
        RestAssured.baseURI = BASE_URI;
        return given()
                .when()
                .delete(COURIER_DELETE + "/" + Integer.toString(id));
    }
}
