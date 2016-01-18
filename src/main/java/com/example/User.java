package com.example;

import org.springframework.hateoas.ResourceSupport;

public class User extends ResourceSupport {

    private String name;

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
