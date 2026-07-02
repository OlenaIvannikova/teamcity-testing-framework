package com.example.teamcity.api.spec;

import com.example.teamcity.api.config.Config;
import com.example.teamcity.api.models.User;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class Specifications {

    private static RequestSpecBuilder requestSpecBuilder() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter());
    }

    /*
    Log in as a Super User in TeamCity (access the server):
    empty username +  auto-generated authentication token as the password
     */
    public static RequestSpecification superUserSpec() {
        return requestSpecBuilder()
                .setBaseUri("http://%s:%s@%s/httpAuth".formatted("", Config.getProperty("superUserToken"), Config.getProperty("host")))
                .build();
    }

    public static RequestSpecification unauthSpec() {
        return requestSpecBuilder().build();
    }

    public static RequestSpecification authSpec(User user) {
        return requestSpecBuilder()
                .setBaseUri("http://%s:%s@%s".formatted(user.getUsername(), user.getPassword(), Config.getProperty("host")))
                .build();
    }
}
