package com.example;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

public class UseRestTemplateTest {

    private static final Logger log = LoggerFactory.getLogger(UseRestTemplateTest.class);

    UseRestTemplate subject;

    @Before
    public void setUp() throws Exception {
        subject = new UseRestTemplate();
    }

    @Test
    public void runRestOperations() {
        Resource<User> newUser = subject.createUser("Test user");
        assertThat(newUser.getContent().getName(), equalTo("Test user"));

        Resource<User> user1 = subject.getUser();
        assertThat(user1.getContent().getName(), equalTo("Test user"));

        Resources<Resource<User>> users = subject.getUsers();
        assertThat(users.getLinks().size(), equalTo(2));
        assertThat(users.getLinks().get(0).getRel(), equalTo("self"));
        assertThat(users.getLinks().get(0).getHref(), equalTo("http://localhost:8080/users"));

        List<Resource<User>> userList = new ArrayList<>(users.getContent());
        assertThat(userList.size(), equalTo(4));
        assertThat(userList.get(0).getLinks().size(), equalTo(2));
        assertThat(userList.get(0).getContent().getName(), equalTo("Test user"));

        Resource<User> user2 = subject.saveUser();
        assertThat(user2.getContent().getName(), equalTo("modified user"));
    }
}