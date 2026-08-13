package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.locator.Locator;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.ui.pages.ProjectPage;
import com.example.teamcity.ui.pages.ProjectsPage;
import com.example.teamcity.ui.pages.admin.CreateProjectPage;
import org.testng.annotations.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

@Test(groups = {"Regression"})
public class CreateProjectTest extends BaseUITest {

    @Test(description = "User should be able to create project", groups = {"Positive"})
    public void userCreatesProjectTest() {

        // подготовка окружения
        step(
                "Login as new user",
                () -> loginAsNewUser()
        );

        // взаимодействие с UI
        step("Create project through UI",
                () -> CreateProjectPage.open()
                        .createForm(REPO_URL)
                        .setupProject(testData.getProject().getName(), testData.getBuildType().getName())
        );

        // Проверка состояния API (корректность отправки данных с UI на API)
        // Проверяем через API- если объект не найден, дальнейшая UI-проверка не имеет смысла.
        var createdProject = step("Verify project is created through API",
                () -> superUserCheckRequests
                        .<Project>getRequest(Endpoint.PROJECTS)
                        .waitFor(Locator.byName(testData.getProject().getName()))
        );

        assertThat(createdProject).isNotNull();

        // проверка состояния UI
        // (корректность считывания данных и отображение данных на UI)
        step("Verify project details on project page",
                () -> ProjectPage.open(createdProject.getId())
                        .title.shouldHave(Condition.exactText(testData.getProject().getName()))
        );

        var foundProjects = step("Verify project is displayed in projects list",
                () -> ProjectsPage.open()
                        .getProjects()
                        .stream()
                        .anyMatch(project -> project.getName().text().equals(testData.getProject().getName()))
        );

        softy.assertThat(foundProjects).isTrue();
    }

    @Test(description = "User should not be able to create project without name", groups = {"Negative"})
    public void userCreatesProjectWithoutNameTest() {
        // подготовка окружения
        step("Login as user");
        step("Check number of projects");

        // взаимодействие с UI
        step("Open `Create Project Page` (http://localhost:8111/admin/createObjectMenu.html)");
        step("Send all project parameters (repository URL)");
        step("Click `Proceed`");
        step("Set Project Name");
        step("Click `Proceed`");

        // проверка состояния API
        // (корректность отправки данных с UI на API)
        step("Check that number of projects did not change");

        // проверка состояния UI
        // (корректность считывания данных и отображение данных на UI)
        step("Check that error appears `Project name must not be empty`");
    }


}

