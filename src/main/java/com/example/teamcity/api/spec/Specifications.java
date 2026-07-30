package com.example.teamcity.api.spec;

import com.example.teamcity.api.config.Config;
import com.example.teamcity.api.models.User;
import com.github.viclovsky.swagger.coverage.FileSystemOutputWriter;
import com.github.viclovsky.swagger.coverage.SwaggerCoverageRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.nio.file.Paths;

import static com.github.viclovsky.swagger.coverage.SwaggerCoverageConstants.OUTPUT_DIRECTORY;

public class Specifications {

    private static RequestSpecBuilder requestSpecBuilder() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new SwaggerCoverageRestAssured(
                        new FileSystemOutputWriter(
                                Paths.get("target/" + OUTPUT_DIRECTORY)
                        )
                ));
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
        return requestSpecBuilder()
                .setBaseUri("http://%s:%s@%s".formatted("", "", Config.getProperty("host")))
                .build();
    }

    public static RequestSpecification authSpec(User user) {
        return requestSpecBuilder()
                .setBaseUri("http://%s:%s@%s".formatted(user.getUsername(), user.getPassword(), Config.getProperty("host")))
                .build();
    }

    public static RequestSpecification mockSpec() {
        return requestSpecBuilder()
                .setBaseUri("http://localhost:8089")
                .build();
    }
}
