package com.example.teamcity.api.spec;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;

public class ResponseSpecifications {

    private static final String DUPLICATE_BUILD_TYPE_ID_ERROR =
            "The build configuration / template ID \"%s\" is already used by another configuration or template";
    private static final String DUPLICATE_PROJECT_ID_ERROR =
            "Project ID \"%s\" is already used by another project";
    private static final String PROJECT_NAME_CANNOT_BE_EMPTY_ERROR =
            "Project name cannot be empty";
    private static final String EMPTY_REQUEST_BODY_ERROR =
            "Cannot read field \"name\" because \"descriptor\" is null";
    private static final String INCORRECT_CREDENTIALS_ERROR =
            "Incorrect username or password";

    public static ResponseSpecification duplicateBuildTypeId(String buildTypeId) {
        return new ResponseSpecBuilder()
                .expectStatusCode(HttpStatus.SC_BAD_REQUEST)
                .expectBody(Matchers.containsString(
                        DUPLICATE_BUILD_TYPE_ID_ERROR.formatted(buildTypeId)))
                .build();
    }

    public static ResponseSpecification duplicateProjectId(String projectId) {
        return new ResponseSpecBuilder()
                .expectStatusCode(HttpStatus.SC_BAD_REQUEST)
                .expectBody(Matchers.containsString(
                        DUPLICATE_PROJECT_ID_ERROR.formatted(projectId)))
                .build();
    }

    public static ResponseSpecification projectNameCannotBeEmpty() {
        return new ResponseSpecBuilder()
                .expectStatusCode(HttpStatus.SC_BAD_REQUEST)
                .expectBody(Matchers.containsString(
                        PROJECT_NAME_CANNOT_BE_EMPTY_ERROR))
                .build();
    }

    public static ResponseSpecification emptyRequestBody() {
        return new ResponseSpecBuilder()
                .expectStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .expectBody(Matchers.containsString(EMPTY_REQUEST_BODY_ERROR))
                .build();
    }

    public static ResponseSpecification unauthorized() {
        return new ResponseSpecBuilder()
                .expectStatusCode(HttpStatus.SC_UNAUTHORIZED)
                .expectBody(Matchers.containsString(INCORRECT_CREDENTIALS_ERROR))
                .build();
    }
}

