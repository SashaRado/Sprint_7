package ru.praktikum.scooter.client;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import ru.praktikum.scooter.model.Order;

import static io.restassured.RestAssured.given;

public class OrdersClient {

    public static final String BASE_URI = "http://qa-scooter.praktikum-services.ru";
    public static final String ORDERS_URI_SUBPATH = "/api/v1/orders";

    @Step("Проверка заказа")
    public Response getCorrectCreateOrderResponse(Order order) {
        RestAssured.baseURI = BASE_URI;
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post(ORDERS_URI_SUBPATH);
    }

    @Step("Проверка списка заказа")
    public Response getCorrectListOrdersResponse() {
        RestAssured.baseURI = BASE_URI;
        return given()
                .get(ORDERS_URI_SUBPATH);
    }
}