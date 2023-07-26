package ru.praktikum.scooter;

import java.io.File;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

import ru.praktikum.scooter.client.CourierClient;

public class CourierCreateTests {
    private File loginData;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Проверка правильности создания курьера")
    public void correctCreateCourier() {
        File jsonCreateData = new File("src/test/resources/createCourierCorrectDataWhenCreate.json");
        loginData = new File("src/test/resources/createCourierCorrectDataWhenLogin.json");
        CourierClient client = new CourierClient();

        client.getCreateCourierResponseCorrect(jsonCreateData)
                .then()
                .statusCode(201)
                .and()
                .assertThat().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Проверка сообщения об ошибке для создания существующего курьера")
    public void whenCreateExistingCourierThenNotOk() {
        File jsonCreateData = new File("src/test/resources/createCourierCorrectDataWhenCreate.json");
        loginData = new File("src/test/resources/createCourierCorrectDataWhenLogin.json");
        CourierClient client = new CourierClient();

        client.getCreateCourierResponseCorrect(jsonCreateData)
                .then()
                .statusCode(201)
                .and()
                .assertThat().body("ok", equalTo(true));

        client.getCreateCourierResponseWhenTryToCreateExistingCourier(jsonCreateData)
                .then()
                .statusCode(409)
                .and()
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Проверка сообщения об ошибке для создания курьера без входа в систему")
    public void whenCreateWithNoLoginThenNotOk() {
        File json = new File("src/test/resources/createCourierDataWithoutLoginField.json");
        loginData = null;
        CourierClient client = new CourierClient();
        client.getCreateCourierResponseWhenTryToCreateCourierWithoutLogin(json)
                .then()
                .statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Проверка сообщения об ошибке для создания курьера без пароля")
    public void whenCreateWithNoPasswordThenNotOk() {
        File json = new File("src/test/resources/createCourierDataWithoutPasswordField.json");
        loginData = null;
        CourierClient client = new CourierClient();
        client.getCreateCourierResponseWhenTryToCreateCourierWithoutPassword(json)
                .then()
                .statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @After
    public void tearDown() throws Exception {
        if (loginData != null) {

            CourierClient client = new CourierClient();
            Response loginCourierResponse = client.getLoginCourierResponseWhenCorrectLogin(loginData);
            loginCourierResponse
                    .then()
                    .statusCode(200)
                    .and()
                    .assertThat().body("id", notNullValue());
            int courierId = client.parseCourierIdFromLoginCourierResponse(loginCourierResponse);
            client.getDeleteCourierResponseWhenCorrectDeletion(courierId)
                    .then()
                    .statusCode(200);
        }
    }
}