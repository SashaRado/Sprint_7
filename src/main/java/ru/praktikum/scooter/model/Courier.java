package ru.praktikum.scooter.model;

import com.google.gson.Gson;

public class Courier {
    private String login;
    private String password;
    private String firstName;

    // Конструктор, геттеры и сеттеры

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // Преобразование объекта Courier в JSON
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
