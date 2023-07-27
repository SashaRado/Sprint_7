package ru.praktikum.scooter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;

import ru.praktikum.scooter.client.CourierClient;
import ru.praktikum.scooter.model.Courier;

public class CourierCreateTests {

    private int courierId;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Проверка правильности создания курьера")
    public void correctCreateCourier() {
        Courier courier = new Courier();
        courier.setLogin("Naom5m07");
        courier.setPassword("Okoko_133324");
        courier.setFirstName("Lego");
        Gson gson = new GsonBuilder().create();
        String jsonCreateData = gson.toJson(courier);
        CourierClient client = new CourierClient();
        Response response = client.getCreateCourierResponse(jsonCreateData)
                .then()
                .statusCode(201)
                .and()
                .assertThat().body("ok", equalTo(true))
                .extract().response();

        courierId = client.parseCourierIdFromLoginCourierResponse(response);
    }

    @Test
    @DisplayName("Проверка сообщения об ошибке для создания существующего курьера")
    public void whenCreateExistingCourierThenNotOk() {
        Courier courier = new Courier();
        courier.setLogin("Naom5m02");
        courier.setPassword("Okoko_133324");
        courier.setFirstName("Lego");
        Gson gson = new GsonBuilder().create();
        String jsonCreateData = gson.toJson(courier);
        CourierClient client = new CourierClient();
        Response response = client.getCreateCourierResponse(jsonCreateData)
                .then()
                .statusCode(409)
                .and()
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .extract().response();

        courierId = client.parseCourierIdFromLoginCourierResponse(response);
    }
    @Test
    @DisplayName("Проверка сообщения об ошибке для создания курьера без входа в систему")
    public void whenCreateWithNoLoginThenNotOk() {
        Courier courier = new Courier();
        courier.setPassword("pahlavaYa");
        courier.setFirstName("Novruzik");

        Gson gson = new GsonBuilder().create();

        String jsonCreateData = gson.toJson(courier);

        CourierClient client = new CourierClient();
        client.getCreateCourierResponse(jsonCreateData)
                .then()
                .statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Проверка сообщения об ошибке для создания курьера без пароля")
    public void whenCreateWithNoPasswordThenNotOk() {
        Courier courier = new Courier();
        courier.setLogin("novruz10000");
        courier.setFirstName("Novruz");

        Gson gson = new GsonBuilder().create();

        String jsonCreateData = gson.toJson(courier);

        CourierClient client = new CourierClient();
        client.getCreateCourierResponse(jsonCreateData)
                .then()
                .statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @After
    public void tearDown() {
            CourierClient client = new CourierClient();
            client.getDeleteCourierResponseWhenCorrectDeletion(courierId);
    }
}