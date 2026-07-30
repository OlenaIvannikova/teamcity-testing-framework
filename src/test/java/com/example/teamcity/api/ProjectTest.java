package com.example.teamcity.api;

import com.example.teamcity.api.locator.Locator;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.models.User;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.UncheckedRequests;
import com.example.teamcity.api.spec.ResponseSpecifications;
import com.example.teamcity.api.spec.Specifications;
import org.testng.annotations.Test;

import static com.example.teamcity.api.enums.Endpoint.*;
import static com.example.teamcity.api.generators.TestDataGenerator.generate;
import static org.apache.commons.lang3.StringUtils.EMPTY;


@Test(groups = {"Regression"})
public class ProjectTest extends BaseApiTest {

    @Test(description = "User should be able to create project", groups = {"Positive", "CRUD"})
    public void userCreatesProjectTest() {
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        var userCheckedRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        userCheckedRequests.getRequest(PROJECTS).create(testData.getProject());

        var createdProject = userCheckedRequests.<Project>getRequest(PROJECTS).read(Locator.byId(testData.getProject().getId()));

        softy.assertThat(testData.getProject().getName())
                .isEqualTo(createdProject.getName());
    }

    @Test(description = "User should not be able to create two projects with the same id", groups = {"Negative", "CRUD"})
    public void userCreatesTwoProjectsWithTheSameIdTest() {
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        var userCheckedRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        userCheckedRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        var projectWithSameId = generate(Project.class, testData.getProject().getId());
        new UncheckedRequests(Specifications.authSpec(testData.getUser()))
                .getRequest(PROJECTS)
                .create(projectWithSameId)
                .then()
                .spec(ResponseSpecifications.duplicateProjectId(testData.getProject().getId()));
    }

    @Test(description = "User should not be able to create project with empty name", groups = {"Negative", "Validation"})
    public void userCreatesProjectWithEmptyNameTest() {
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());

        Project project = testData.getProject();
        project.setName(EMPTY);

        new UncheckedRequests(Specifications.authSpec(testData.getUser()))
                .getRequest(PROJECTS)
                .create(project)
                .then()
                .spec(ResponseSpecifications.projectNameCannotBeEmpty());
    }

    @Test(description = "User should not be able to create project with null name", groups = {"Negative", "Validation"})
    public void userCreatesProjectWithNullNameTest() {
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());

        Project project = testData.getProject();
        project.setName(null);

        new UncheckedRequests(Specifications.authSpec(testData.getUser()))
                .getRequest(PROJECTS)
                .create(project)
                .then()
                .spec(ResponseSpecifications.projectNameCannotBeEmpty());
    }

    @Test(description = "User should not be able to create project with empty request body", groups = {"Negative", "Body validation"})
    public void userCreatesProjectWithEmptyRequestBodyTest() {
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());

        new UncheckedRequests(Specifications.authSpec(testData.getUser()))
                .getRequest(PROJECTS)
                .create(new byte[0])
                .then()
                .spec(ResponseSpecifications.emptyRequestBody());
    }

    @Test(description = "User should not be able to create project without authentication", groups = {"Negative", "Authentication"})
    public void userCreatesProjectWithoutAuthenticationTest() {
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());

        new UncheckedRequests(Specifications.unauthSpec())
                .getRequest(PROJECTS)
                .create(testData.getProject())
                .then()
                .spec(ResponseSpecifications.unauthorized());
    }

    @Test(description = "User should not be able to create project with invalid user password", groups = {"Negative", "Authentication"})
    public void userCreatesProjectWithInvalidPasswordTest() {
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());

        User user = testData.getUser();
        user.setPassword("invalidPassword");

        new UncheckedRequests(Specifications.authSpec(user))
                .getRequest(PROJECTS)
                .create(testData.getProject())
                .then()
                .spec(ResponseSpecifications.unauthorized());
    }

    @Test(description = "User should treat SQL Injection payload as plain text ", groups = {"Positive", "Security"})
    public void userCreatesProjectWithSqlInjectionPayloadInNameTest() {
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        var userCheckedRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        Project project = testData.getProject();
        project.setName("admin' --");

        userCheckedRequests.getRequest(PROJECTS).create(project);

        var createdProject = userCheckedRequests.<Project>getRequest(PROJECTS).read(Locator.byId(testData.getProject().getId()));

        softy.assertThat(testData.getProject().getName())
                .isEqualTo(createdProject.getName());
    }

    @Test(description = "User should be able to get project details by project name", groups = {"Positive", "CRUD"})
    public void userGetsProjectDetailsByProjectNameTest() {
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        var userCheckedRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        userCheckedRequests.getRequest(PROJECTS).create(testData.getProject());

        var createdProject = userCheckedRequests.<Project>getRequest(PROJECTS).read(Locator.byName(testData.getProject().getName()));

        softy.assertThat(testData.getProject().getName())
                .isEqualTo(createdProject.getName());
    }
}