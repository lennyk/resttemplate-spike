package com.example;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.EvoInflectorRelProvider;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class UserResourceRestTemplate {

    private ParameterizedTypeReference<UserResource> parameterizedUserType;
    private ParameterizedTypeReference<Resources<UserResource>> parameterizedUsersType;
    private RestTemplate restTemplate;

    public UserResourceRestTemplate() {
        parameterizedUserType = new ParameterizedTypeReference<UserResource>() { };
        parameterizedUsersType = new ParameterizedTypeReference<Resources<UserResource>>() { };
        restTemplate = getRestTemplate();
    }

    private RestTemplate getRestTemplate() {
        RestTemplate template = new RestTemplate();

        List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
        converters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));

        RelProvider relProvider = new EvoInflectorRelProvider();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Jackson2HalModule());
        mapper.setHandlerInstantiator(new Jackson2HalModule.HalHandlerInstantiator(relProvider, null, null));
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MappingJackson2HttpMessageConverter halConverter = new MappingJackson2HttpMessageConverter();
        halConverter.setObjectMapper(mapper);
        halConverter.setSupportedMediaTypes(Arrays.asList(MediaTypes.HAL_JSON));
        converters.add(halConverter);

        template.setMessageConverters(converters);

        // Use Apache HTTP client library for PATCH etc. support
        HttpClient httpClient = HttpClients.createDefault();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        template.setRequestFactory(requestFactory);

        return template;
    }

    public UserResource createUser(String name) {
        UserResource newUser = new UserResource(name);

        return restTemplate.exchange(
                "http://localhost:8080/users/1",
                HttpMethod.PUT,
                new HttpEntity<>(newUser),
                parameterizedUserType).getBody();
    }

    public UserResource getUser() {
        UserResource user = restTemplate.exchange(
                "http://localhost:8080/users/1",
                HttpMethod.GET,
                null,
                parameterizedUserType)
                .getBody();

        System.out.println(user);

        return user;
    }

    public Resources<UserResource> getUsers() {
        Resources<UserResource> users = restTemplate.exchange(
                "http://localhost:8080/users",
                HttpMethod.GET,
                null,
                parameterizedUsersType)
                .getBody();

        System.out.println(users);

        return users;
    }

    public UserResource saveUser() {
        UserResource user = restTemplate.exchange(
                "http://localhost:8080/users/1",
                HttpMethod.GET,
                null,
                parameterizedUserType)
                .getBody();

        user.setName("modified user");

        UserResource savedUser = restTemplate.exchange(
                "http://localhost:8080/users/1",
                HttpMethod.PATCH,
                new HttpEntity<>(user),
                parameterizedUserType)
                .getBody();

        System.out.println(savedUser);

        return savedUser;
    }
}
