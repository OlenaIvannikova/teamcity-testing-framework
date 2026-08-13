package com.example.teamcity.api;

import com.example.teamcity.api.locator.Locator;
import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.UncheckedRequests;
import com.example.teamcity.api.spec.ResponseSpecifications;
import com.example.teamcity.api.spec.Specifications;
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
        var userCheckedRequests = step(
                "Create user",
                () -> {
                    superUserCheckRequests
                            .getRequest(USERS)
                            .create(testData.getUser());

                    return new CheckedRequests(
                            Specifications.authSpec(testData.getUser())
                    );
                }
        );

        step("Create project",
                () -> userCheckedRequests
                        .<Project>getRequest(PROJECTS)
                        .create(testData.getProject())
        );

        step("Create build type",
                () -> userCheckedRequests
                        .getRequest(BUILD_TYPES)
                        .create(testData.getBuildType())
        );

        var createdBuildType = step("Get build type details by build type id",
                () -> userCheckedRequests
                        .<BuildType>getRequest(BUILD_TYPES)
                        .read(Locator.byId(testData.getBuildType().getId()))
        );

        step("Verify build type name",
                () -> softy.assertThat(testData.getBuildType().getName())
                        .isEqualTo(createdBuildType.getName()));
    }

    @Test(description = "User should not be able to create two build types with the same id", groups = {"Negative", "CRUD"})
    public void userCreatesTwoBuildTypesWithTheSameIdTest() {

        var userCheckedRequests = step(
                "Create user",
                () -> {
                    superUserCheckRequests
                            .getRequest(USERS)
                            .create(testData.getUser());

                    return new CheckedRequests(
                            Specifications.authSpec(testData.getUser())
                    );
                }
        );

        step("Create project",
                () -> userCheckedRequests
                        .<Project>getRequest(PROJECTS)
                        .create(testData.getProject())
        );

        step("Create build type",
                () -> userCheckedRequests
                        .getRequest(BUILD_TYPES)
                        .create(testData.getBuildType())
        );

        step("Verify build type creation is rejected with duplicate ID",
                () -> {
                    var buildTypeWithSameId = generate(Arrays.asList(testData.getProject()), BuildType.class, testData.getBuildType().getId());
                    new UncheckedRequests(Specifications.authSpec(testData.getUser()))
                            .getRequest(BUILD_TYPES)
                            .create(buildTypeWithSameId)
                            .then()
                            .spec(ResponseSpecifications.duplicateBuildTypeId(testData.getBuildType().getId()));
                }
        );
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

