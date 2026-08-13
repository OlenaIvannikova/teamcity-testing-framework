package com.example.teamcity.api;

import com.example.teamcity.api.generators.RandomData;
import com.example.teamcity.api.locator.Locator;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.models.User;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.UncheckedRequests;
import com.example.teamcity.api.spec.ResponseSpecifications;
import com.example.teamcity.api.spec.Specifications;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.example.teamcity.api.enums.Endpoint.*;
import static com.example.teamcity.api.generators.TestDataGenerator.generate;
import static io.qameta.allure.Allure.step;
import static org.apache.commons.lang3.StringUtils.EMPTY;


@Test(groups = {"Regression"})
public class ProjectTest extends BaseApiTest {
    private static final String SQL_INJECTION_PAYLOAD = "admin' --";

    @Test(description = "User should be able to create project", groups = {"Positive", "CRUD"})
    public void userCreatesProjectTest() {
        step("Create user");
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        var userCheckedRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        step("Create project");
        userCheckedRequests.getRequest(PROJECTS).create(testData.getProject());

        step("Get created project");
        var createdProject = userCheckedRequests.<Project>getRequest(PROJECTS).read(Locator.byId(testData.getProject().getId()));

        step("Verify project name");
        softy.assertThat(testData.getProject().getName())
                .isEqualTo(createdProject.getName());
    }

    @Test(description = "User should not be able to create two projects with the same id", groups = {"Negative", "CRUD"})
    public void userCreatesTwoProjectsWithTheSameIdTest() {
        step("Create user");
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        var userCheckedRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        step("Create project");
        userCheckedRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        step("Verify project creation is rejected with duplicate ID");
        var projectWithSameId = generate(Project.class, testData.getProject().getId());
        new UncheckedRequests(Specifications.authSpec(testData.getUser()))
                .getRequest(PROJECTS)
                .create(projectWithSameId)
                .then()
                .spec(ResponseSpecifications.duplicateProjectId(testData.getProject().getId()));
    }

    @DataProvider(name = "invalidProjectNames")
    public Object[][] invalidProjectNames() {
        return new String[][]{
                {EMPTY},
                {null}
        };
    }

    @Test(
            description = "User should not be able to create project with invalid name",
            groups = {"Negative", "Validation"},
            dataProvider = "invalidProjectNames"
    )
    public void userCreatesProjectWithInvalidNameTest(String projectName) {
        step("Create user");
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());

        step("Verify project creation is rejected with invalid name");
        Project project = testData.getProject();
        project.setName(projectName);

        new UncheckedRequests(Specifications.authSpec(testData.getUser()))
                .getRequest(PROJECTS)
                .create(project)
                .then()
                .spec(ResponseSpecifications.projectNameCannotBeEmpty());
    }

    @Test(description = "User should not be able to create project with empty request body", groups = {"Negative", "Body validation"})
    public void userCreatesProjectWithEmptyRequestBodyTest() {
        step("Create user");
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());

        step("Verify project creation is rejected with empty request body");
        new UncheckedRequests(Specifications.authSpec(testData.getUser()))
                .getRequest(PROJECTS)
                .create(new byte[0])
                .then()
                .spec(ResponseSpecifications.emptyRequestBody());
    }

    @Test(description = "User should not be able to create project without authentication", groups = {"Negative", "Authentication"})
    public void userCreatesProjectWithoutAuthenticationTest() {
        step("Create user");
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());

        step("Verify project creation is rejected without authentication");
        new UncheckedRequests(Specifications.unauthSpec())
                .getRequest(PROJECTS)
                .create(testData.getProject())
                .then()
                .spec(ResponseSpecifications.unauthorized());
    }

    @Test(description = "User should not be able to create project with invalid user password", groups = {"Negative", "Authentication"})
    public void userCreatesProjectWithInvalidPasswordTest() {
        step("Create user");
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());

        User user = testData.getUser();
        user.setPassword(RandomData.getString());

        step("Verify project creation is rejected with invalid user password");
        new UncheckedRequests(Specifications.authSpec(user))
                .getRequest(PROJECTS)
                .create(testData.getProject())
                .then()
                .spec(ResponseSpecifications.unauthorized());
    }

    @Test(description = "User should treat SQL Injection payload as plain text ", groups = {"Positive", "Security"})
    public void userCreatesProjectWithSqlInjectionPayloadInNameTest() {
        step("Create user");
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        var userCheckedRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        step("Create project with SQL Injection payload in name");
        Project project = testData.getProject();
        project.setName(SQL_INJECTION_PAYLOAD);

        userCheckedRequests.getRequest(PROJECTS).create(project);

        step("Verify SQL Injection payload is stored as plain text");
        var createdProject = userCheckedRequests.<Project>getRequest(PROJECTS).read(Locator.byId(testData.getProject().getId()));

        softy.assertThat(createdProject.getName())
                .isEqualTo(SQL_INJECTION_PAYLOAD);
    }

    @Test(description = "User should be able to get project details by project name", groups = {"Positive", "CRUD"})
    public void userGetsProjectDetailsByProjectNameTest() {
        step("Create user");
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        var userCheckedRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        step("Create project");
        userCheckedRequests.getRequest(PROJECTS).create(testData.getProject());

        step("Get project details by project name");
        var createdProject = userCheckedRequests.<Project>getRequest(PROJECTS).read(Locator.byName(testData.getProject().getName()));

        step("Verify project name");
        softy.assertThat(testData.getProject().getName())
                .isEqualTo(createdProject.getName());
    }
}
