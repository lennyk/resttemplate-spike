package com.example;

public class User {

    private String name;

    public User() {
        this.name = "";
    }

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("User{name='%s'}", name);
    }
}
