package com.example;

import org.springframework.hateoas.ResourceSupport;

public class UserResource extends ResourceSupport {

    private String name;

    public UserResource() {
        this.name = "";
    }

    public UserResource(String name) {
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
