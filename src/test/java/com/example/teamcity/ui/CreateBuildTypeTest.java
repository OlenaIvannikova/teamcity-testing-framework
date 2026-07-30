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

@Test(groups = {"Regression"})
public class CreateBuildTypeTest extends BaseUITest {

    @Test(description = "User should be able to create build type", groups = {"Positive", "CRUD"})
    public void userCreatesBuildTypeTest() {
        loginAsNewUser();
        superUserCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        // взаимодействие с UI
        CreateBuildTypePage.open(testData.getProject().getId())
                .createForm(REPO_URL)
                .setBuildTypeName(testData.getBuildType().getName())
                .submit()
                .shouldBeCreated();

        // проверка состояния API (корректность отправки данных с UI на API)
        var createdBuildType = superUserCheckRequests.<BuildType>getRequest(Endpoint.BUILD_TYPES).read(Locator.byName(testData.getBuildType().getName()));
        softy.assertThat(createdBuildType).isNotNull();

        // проверка состояния UI (корректность считывания данных и отображение данных на UI)
        BuildTypePage.open(createdBuildType.getId())
                .title.shouldHave(exactText(testData.getBuildType().getName()));
    }

    @Test(description = "User should not be able to create build type without name", groups = {"Negative", "CRUD"})
    public void userCreatesBuildTypeWithoutNameTest() {
        loginAsNewUser();
        superUserCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        var buildTypeCountBeforeTest = superUserCheckRequests.<BuildTypes>getRequest(BUILD_TYPES_LIST).read().getCount();

        // Check that 'Build configuration name must not be empty' validation error is displayed
        CreateBuildTypePage.open(testData.getProject().getId())
                .createForm(REPO_URL)
                .setBuildTypeName(null)
                .submit()
                .shouldHaveNameRequiredError();

        var buildTypeCountAfterTest = superUserCheckRequests.<BuildTypes>getRequest(BUILD_TYPES_LIST).read().getCount();

        softy.assertThat(buildTypeCountBeforeTest).isEqualTo(buildTypeCountAfterTest);
    }

}
