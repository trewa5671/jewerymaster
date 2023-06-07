package com.example.jewelrymaster;

public class User {
    private String name;
    private String email;

    public User() {
        // Обязательный пустой конструктор для Firebase
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
