package ru.praktikum.scooter;

import ru.praktikum.scooter.client.OrdersClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import ru.praktikum.scooter.model.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreateTests {

    private final List<String> color;

    @Parameterized.Parameters(name="Цвет самоката. Тестовые данные: {0}.")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { List.of("BLACK", "GREY")},
                { List.of("BLACK")},
                { List.of("GREY")},
                { List.of()},
        });
    }

    public OrderCreateTests(List<String> color) {
        this.color = color;
    }

    @Before
    public void setUp() {

        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Проверка правильности создания заказа")
    public void correctCreateOrder() {
        Order order = new Order("Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", color);
        OrdersClient client = new OrdersClient();
        client.getCorrectCreateOrderResponse(order)
                .then()
                .statusCode(201)
                .and()
                .assertThat().body("track", notNullValue());
    }
}