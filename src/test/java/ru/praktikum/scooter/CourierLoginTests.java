package ru.praktikum.scooter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.RestAssured;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ru.praktikum.scooter.client.CourierClient;
import ru.praktikum.scooter.model.Courier;

import static org.hamcrest.CoreMatchers.*;

public class CourierLoginTests {
    String login = RandomStringUtils.random(10, true, false);
    String password = RandomStringUtils.random(10, true, true);
    String name = RandomStringUtils.random(10, true, false);

    CourierClient courierClient = new CourierClient();

    Courier courier = new Courier(login, password, name);
    @After
    public void deleteCourier() {
        courierClient.deleteCourier(courier);
    }

    @Test
    @DisplayName("Проверка правильности авторизации курьера")
    public void correctLoginCourier() {
        courierClient.createCourier(courier);
        courierClient.loginCourier(courier)
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    @DisplayName("Проверка сообщения об ошибке для входа курьера без поля входа")
    public void whenTryToLoginWithoutLoginFieldThenNotOk() {
        courier = new Courier(login, "", "");
        courierClient.createCourier(courier);
        courierClient.loginCourier(courier)
                .then()
                .assertThat()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));

    }

    @Test
    @DisplayName("Проверка сообщения об ошибке для входа в систему курьера с несуществующим полем для входа")
    public void whenTryToLoginWithNotExistingLoginThenNotOk() {
        courier = new Courier(login, "", "");
        courierClient.createCourier(courier);
        courierClient.loginCourier(courier)
                .then()
                .assertThat()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));


    }

    @Test
    @DisplayName("Проверка сообщения об ошибке для входа курьером с неверным паролем")
    public void whenTryToLoginWithNotCorrectPasswordThenNotOk() {
        courier = new Courier("qwerty", "123123123", "");

        courierClient.loginCourier(courier)
                .then()
                .assertThat()
                .statusCode(404)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }
}