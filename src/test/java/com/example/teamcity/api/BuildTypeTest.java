package com.example.teamcity.api;

import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.UncheckedRequests;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.Arrays;

import static com.example.teamcity.api.enums.Endpoint.*;
import static com.example.teamcity.api.generators.TestDataGenerator.generate;
import static io.qameta.allure.Allure.step;

// Create build type (build configuration): POST /app/rest/buildTypes

@Test(groups = {"Regression"})
public class BuildTypeTest extends BaseApiTest {

    @Test(description = "User should be able to create build type", groups = {"Positive", "CRUD"})
    public void userCreatesBuildTypeTest() {
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        var userCheckedRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        userCheckedRequests.getRequest(PROJECTS).create(testData.getProject());
        userCheckedRequests.getRequest(BUILD_TYPES).create(testData.getBuildType());

        var createdBuildType = userCheckedRequests.<BuildType>getRequest(BUILD_TYPES).read(testData.getBuildType().getId());

        softy.assertThat(testData.getBuildType().getName())
                .isEqualTo(createdBuildType.getName());
    }

    @Test(description = "User should not be able to create two build types with the same id", groups = {"Negative", "CRUD"})
    public void userCreatesTwoBuildTypesWithTheSameIdTest() {
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        var userCheckedRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        userCheckedRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
        userCheckedRequests.getRequest(BUILD_TYPES).create(testData.getBuildType());

        var buildTypeWithSameId = generate(Arrays.asList(testData.getProject()), BuildType.class, testData.getBuildType().getId());
        new UncheckedRequests(Specifications.authSpec(testData.getUser()))
                .getRequest(BUILD_TYPES)
                .create(buildTypeWithSameId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(Matchers.containsString("The build configuration / template ID \"%s\" is already used by another configuration or template".formatted(testData.getBuildType().getId())));
    }

    @Test(description = "Project admin should be able to create build type for their project", groups = {"Positive", "Roles"})
    public void projectAdminCreatesBuildTypeTest() {
        step("Create user");
        step("Create project");
        step("Grant user PROJECT_ADMIN role in project");

        step("Create buildType for project by user (PROJECT_ADMIN)");
        step("Check buildType was created successfully");
    }

    @Test(description = "Project admin should not be able to create build type for not their project", groups = {"Negative", "Roles"})
    public void projectAdminCreatesBuildTypeForAnotherUserProjectTest() {
        step("Create user1");
        step("Create project1");
        step("Grant user1 PROJECT_ADMIN role in project1");

        step("Create user2");
        step("Create project2");
        step("Grant user2 PROJECT_ADMIN role in project2");

        step("Create buildType for project1 by user2");
        step("Check buildType was not created with forbidden code");
    }
}
