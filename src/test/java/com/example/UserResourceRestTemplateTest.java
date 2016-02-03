package com.example;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.hateoas.Resources;

public class UserResourceRestTemplateTest {

    private static final Logger log = LoggerFactory.getLogger(UserResourceRestTemplateTest.class);

    UserResourceRestTemplate subject;

    @Before
    public void setUp() throws Exception {
        subject = new UserResourceRestTemplate();
    }

    @Test
    public void runRestOperations() {
        UserResource newUser = subject.createUser("Test user");
        assertThat(newUser.getName(), equalTo("Test user"));

        UserResource user1 = subject.getUser();
        assertThat(user1.getName(), equalTo("Test user"));

        Resources<UserResource> users = subject.getUsers();
        assertThat(users.getLinks().size(), equalTo(2));
        assertThat(users.getLinks().get(0).getRel(), equalTo("self"));
        assertThat(users.getLinks().get(0).getHref(), equalTo("http://localhost:8080/users"));

        List<UserResource> userList = new ArrayList<>(users.getContent());
        assertThat(userList.size(), equalTo(4));
        assertThat(userList.get(0).getLinks().size(), equalTo(2));
        assertThat(userList.get(0).getName(), equalTo("Test user"));

        UserResource user2 = subject.saveUser();
        assertThat(user2.getName(), equalTo("modified user"));
    }
}