package ru.praktikum.scooter.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.RestAssured;
import ru.praktikum.scooter.model.Courier;

import static io.restassured.RestAssured.given;

public class CourierClient {

    private static final String BASE_URL = "http://qa-scooter.praktikum-services.ru";
    public static final String COURIER_BASE_URL = "/api/v1/courier";
    public static final String COURIER_LOGIN = "/api/v1/courier/login";

    @Step("Создание учетной записи курьера")
    public Response createCourier(Courier courier) {
        RestAssured.baseURI = BASE_URL;
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(COURIER_BASE_URL);
    }

    @Step("Авторизация")
    public Response loginCourier(Courier courier) {
        RestAssured.baseURI = BASE_URL;
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(COURIER_LOGIN);
    }

    @Step("Удаление учетной записи курьера")
    public void deleteCourier(Courier courier) {
        try {
            int id = loginCourier(courier).then().extract().path("id");
            RestAssured.baseURI = BASE_URL;
            given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(courier)
                    .when()
                    .delete(COURIER_BASE_URL + id);

        } catch (NullPointerException e) {
            System.out.println("Нечего удалять после теста");
        }
    }
}
