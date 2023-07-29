package ru.praktikum.scooter;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import ru.praktikum.scooter.client.CourierClient;
import ru.praktikum.scooter.model.Courier;

public class CourierCreateTests {

    String login = RandomStringUtils.random(10, true, false);
    String password = RandomStringUtils.random(10, true, true);
    String name = RandomStringUtils.random(10, true, false);
    CourierClient courierClient = new CourierClient();
    Courier courier = new Courier(login, password, name);

    @After
    public void tearDown() {
        courierClient.deleteCourier(courier);
    }
    @Test
    @DisplayName("Проверка правильности создания учетной записи курьера")
    public void correctCreateCourier() {
        courierClient.createCourier(courier)
                .then()
                .assertThat().statusCode(201);
    }
    @Test
    @DisplayName("Проверка сообщения об ошибке при создании существующей учетной записи курьера")
    public void whenCreateExistingCourierThenNotOk() {
        courierClient.createCourier(courier);
        courierClient.createCourier(courier)
                .then()
                .assertThat()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }
    @Test
    @DisplayName("Проверка сообщения об ошибке при создании учетной записи курьера без логина")
    public void whenCreateWithNoLoginThenNotOk() {
        courier = new Courier("", password, name);
        courierClient.createCourier(courier)
                .then()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Test
    @DisplayName("Проверка сообщения об ошибке при учетной записи курьера без пароля")
    public void whenCreateWithNoPasswordThenNotOk() {
        courier = new Courier(login, "", name);
        courierClient.createCourier(courier)
                .then()
                .assertThat().
                body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}