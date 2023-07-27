package ru.praktikum.scooter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ru.praktikum.scooter.client.CourierClient;
import ru.praktikum.scooter.model.Courier;

import static org.hamcrest.CoreMatchers.*;

public class CourierLoginTests {
    private Response correctLoginCourierResponse;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Проверка правильности авторизации курьера")
    public void correctLoginCourier() {
        // Создаем объект Courier и заполняем его данными
        Courier courier = new Courier();
        courier.setLogin("novruz561195145125132");
        courier.setPassword("beshbarmak111");

        Gson gson = new GsonBuilder().create();
        String jsonLoginData = gson.toJson(courier);

        CourierClient client = new CourierClient();
        Response loginResponse = client.getLoginCourierResponse(jsonLoginData)
                .then()
                .statusCode(200)
                .and()
                .assertThat().body("id", notNullValue())
                .extract().response();
    }

    @Test
    @DisplayName("Проверка сообщения об ошибке для входа курьера без поля входа")
    public void whenTryToLoginWithoutLoginFieldThenNotOk() {
        // Создаем объект Courier и заполняем его данными
        Courier courier = new Courier();
        courier.setPassword("beshbarmak111");

        Gson gson = new GsonBuilder().create();

        String json = gson.toJson(courier);

        CourierClient client = new CourierClient();
        client.getLoginCourierResponse(json)
                .then()
                .statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
        correctLoginCourierResponse = null;
    }

    @Test
    @DisplayName("Проверка сообщения об ошибке для входа в систему курьера с несуществующим полем для входа")
    public void whenTryToLoginWithNotExistingLoginThenNotOk() {
        // Создаем объект Courier и заполняем его данными
        Courier courierNotExistingLogin = new Courier();
        courierNotExistingLogin.setLogin("INVALID_LOGIN_7777_aaaaaaa");
        courierNotExistingLogin.setPassword("beshbarmak111");

        Gson gson = new GsonBuilder().create();

        String jsonNotExistingLogin = gson.toJson(courierNotExistingLogin);

        CourierClient client = new CourierClient();
        client.getLoginCourierResponse(jsonNotExistingLogin)
                .then()
                .statusCode(404)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
        correctLoginCourierResponse = null;
    }

    @Test
    @DisplayName("Проверка сообщения об ошибке для входа курьером с неверным паролем")
    public void whenTryToLoginWithNotCorrectPasswordThenNotOk() {

        Courier courier = new Courier();
        courier.setLogin("novruz561195145125132");
        courier.setPassword("beshbarmak110");

        Gson gson = new GsonBuilder().create();

        String jsonLoginData = gson.toJson(courier);

        CourierClient client = new CourierClient();
        Response loginResponse = client.getLoginCourierResponse(jsonLoginData)
                .then()
                .statusCode(404)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"))
                .extract().response();
    }

    @After
    public void tearDown() throws Exception {
        if (correctLoginCourierResponse != null) {

            CourierClient client = new CourierClient();
            int courierId = client.parseCourierIdFromLoginCourierResponse(correctLoginCourierResponse);
            client.getDeleteCourierResponseWhenCorrectDeletion(courierId)
                    .then()
                    .statusCode(200);
        }
    }
}