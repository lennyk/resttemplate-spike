package com.example;

import org.junit.Before;
import org.junit.Test;

public class UseRestTemplateTest {

    UseRestTemplate subject;

    @Before
    public void setUp() throws Exception {
        subject = new UseRestTemplate();
    }

    @Test
    public void runRestOperations() {
        subject.getUser();
        subject.getUsers();
        subject.saveUser();
    }
}