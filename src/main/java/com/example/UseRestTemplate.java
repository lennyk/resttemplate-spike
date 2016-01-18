package com.example;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class UseRestTemplate {

    private ParameterizedTypeReference<User> parameterizedUserType;
    private ParameterizedTypeReference<Resources<User>> parameterizedUsersType;
    private RestTemplate restTemplate;

    public UseRestTemplate() {
        parameterizedUserType = new ParameterizedTypeReference<User>() { };
        parameterizedUsersType = new ParameterizedTypeReference<Resources<User>>() { };
        restTemplate = getRestTemplate();
    }

    private RestTemplate getRestTemplate() {
        RestTemplate template = new RestTemplate();

        List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
        converters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Jackson2HalModule());
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

    public void getUser() {
        User user = restTemplate.exchange(
                "http://localhost:8080/users/1",
                HttpMethod.GET,
                null,
                parameterizedUserType)
                .getBody();

        System.out.println(user);
    }

    public void getUsers() {
        Collection<User> users = restTemplate.exchange(
                "http://localhost:8080/users",
                HttpMethod.GET,
                null,
                parameterizedUsersType)
                .getBody().getContent();

        System.out.println(users);
    }

    public void saveUser() {
        User user = restTemplate.exchange(
                "http://localhost:8080/users/1",
                HttpMethod.GET,
                null,
                parameterizedUserType)
                .getBody();

        user.setName("modified user");

        User savedUser = restTemplate.exchange(
                "http://localhost:8080/users/1",
                HttpMethod.PATCH,
                new HttpEntity<User>(user),
                parameterizedUserType)
                .getBody();

        System.out.println(savedUser);
    }
}
