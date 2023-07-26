package ru.praktikum.scooter;

import ru.praktikum.scooter.client.OrdersClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;


public class OrderListTests {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Проверка, что в тело ответа возвращается список заказов.")
    public void correctListOrders() {
        OrdersClient client = new OrdersClient();
        client.getCorrectListOrdersResponse()
                .then()
                .statusCode(200);
    }
}