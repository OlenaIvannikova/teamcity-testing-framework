package com.example.teamcity.ui;

import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.locator.Locator;
import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.BuildTypes;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.ui.pages.BuildTypePage;
import com.example.teamcity.ui.pages.admin.CreateBuildTypePage;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Condition.exactText;
import static com.example.teamcity.api.enums.Endpoint.*;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

@Test(groups = {"Regression"})
public class CreateBuildTypeTest extends BaseUITest {

    @Test(description = "User should be able to create build type", groups = {"Positive", "CRUD"})
    public void userCreatesBuildTypeTest() {
        step("Login as new user");
        loginAsNewUser();

        step("Create project through API");
        superUserCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        // взаимодействие с UI
        step("Create build type through UI");
        CreateBuildTypePage.open(testData.getProject().getId())
                .createForm(REPO_URL)
                .setBuildTypeName(testData.getBuildType().getName())
                .submit()
                .shouldBeCreated();

        // Проверяем через API, что Build Type действительно создан.
        // Если объект не найден, дальнейшая UI-проверка не имеет смысла.
        step("Verify build type is created through API");
        var createdBuildType = superUserCheckRequests.<BuildType>getRequest(Endpoint.BUILD_TYPES).read(Locator.byName(testData.getBuildType().getName()));
        assertThat(createdBuildType).isNotNull();

        // проверка состояния UI (корректность считывания данных и отображение данных на UI)
        step("Verify build type name is displayed on UI");
        BuildTypePage.open(createdBuildType.getId())
                .title.shouldHave(exactText(testData.getBuildType().getName()));
    }

    @Test(description = "User should not be able to create build type without name", groups = {"Negative", "CRUD"})
    public void userCreatesBuildTypeWithoutNameTest() {
        step("Login as new user");
        loginAsNewUser();

        step("Create project through API");
        superUserCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        step("Get build type count before test");
        var buildTypeCountBeforeTest = superUserCheckRequests.<BuildTypes>getRequest(BUILD_TYPES_LIST).read().getCount();

        // Check that 'Build configuration name must not be empty' validation error is displayed
        step("Verify build type creation is rejected without name");
        CreateBuildTypePage.open(testData.getProject().getId())
                .createForm(REPO_URL)
                .setBuildTypeName(null)
                .submit()
                .shouldHaveNameRequiredError();

        step("Verify build type was not created");
        var buildTypeCountAfterTest = superUserCheckRequests.<BuildTypes>getRequest(BUILD_TYPES_LIST).read().getCount();

        softy.assertThat(buildTypeCountBeforeTest).isEqualTo(buildTypeCountAfterTest);
    }

}

