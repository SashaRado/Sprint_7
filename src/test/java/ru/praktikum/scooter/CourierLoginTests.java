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

public class CourierLoginTests {
    private Response correctLoginCourierResponse;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Проверка правильности авторизации курьера")
    public void correctLoginCourier() {
        File json = new File("src/test/resources/loginCourierDataCorrect.json");
        CourierClient client = new CourierClient();

        client.getCreateCourierResponseCorrect(json)
                .then()
                .statusCode(201)
                .and()
                .assertThat().body("ok", equalTo(true));

        Response loginCourierResponse = client.getLoginCourierResponseWhenCorrectLogin(json);
        loginCourierResponse
                .then()
                .statusCode(200)
                .and()
                .assertThat().body("id", notNullValue());
        correctLoginCourierResponse = loginCourierResponse;
    }

    @Test
    @DisplayName("Проверка сообщения об ошибке для входа курьера без поля входа")
    public void whenTryToLoginWithoutLoginFieldThenNotOk() {
        File json = new File("src/test/resources/loginCourierDataWithoutLoginField.json");
        CourierClient client = new CourierClient();

        client.getLoginCourierResponseWhenTryToLoginWithoutLoginField(json)
                .then()
                .statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
        correctLoginCourierResponse = null;
    }

    @Test
    @DisplayName("Проверка сообщения об ошибке для входа в систему курьера с несуществующим полем для входа")
    public void whenTryToLoginWithNotExistingLoginThenNotOk() {
        File json = new File("src/test/resources/loginCourierDataNotExistingLogin.json");
        CourierClient client = new CourierClient();
        client.getLoginCourierResponseWhenTryToLoginWithNotExistingCredentials(json)
                .then()
                .statusCode(404)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
        correctLoginCourierResponse = null;
    }

    @Test
    @DisplayName("Проверка сообщения об ошибке для входа курьером с неверным паролем")
    public void whenTryToLoginWithNotCorrectPasswordThenNotOk() {
        File jsonNotCorrectData = new File("src/test/resources/loginCourierDataNotCorrect.json");
        File jsonCorrectData = new File("src/test/resources/loginCourierDataCorrect.json");
        CourierClient client = new CourierClient();

        client.getCreateCourierResponseCorrect(jsonCorrectData)
                .then()
                .statusCode(201)
                .and()
                .assertThat().body("ok", equalTo(true));

        Response loginCourierResponse = client.getLoginCourierResponseWhenCorrectLogin(jsonCorrectData);
        loginCourierResponse
                .then()
                .statusCode(200)
                .and()
                .assertThat().body("id", notNullValue());

        client.getLoginCourierResponseWhenTryToLoginWithNotExistingCredentials(jsonNotCorrectData)
                .then()
                .statusCode(404)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
        correctLoginCourierResponse = loginCourierResponse;
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