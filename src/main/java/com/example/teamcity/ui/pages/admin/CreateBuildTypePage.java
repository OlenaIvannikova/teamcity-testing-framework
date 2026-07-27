package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.elements.ErrorElement;

import static com.codeborne.selenide.Selenide.$;

public class CreateBuildTypePage extends CreateBasePage {

    private static final String BUILD_TYPE_SHOW_MODE = "createBuildTypeMenu";
    private static final String BUILD_TYPE_NAME_REQUIRED_ERROR_MESSAGE = "Build configuration name must not be empty";

    private SelenideElement buildTypeNameInput = $("#buildTypeName");
    protected SelenideElement successMessage = $("#unprocessed_objectsCreated");


    public static CreateBuildTypePage open(String projectId) {
        return Selenide.open(CREATE_URL.formatted(projectId, BUILD_TYPE_SHOW_MODE), CreateBuildTypePage.class);
    }

    public CreateBuildTypePage createForm(String url) {
        baseCreateForm(url);
        return this;
    }

    public CreateBuildTypePage setBuildTypeName(String buildTypeName) {
        buildTypeNameInput.setValue(buildTypeName);
        return this;
    }

    public CreateBuildTypePage submit() {
        submitButton.click();
        return this;
    }

    public CreateBuildTypePage shouldBeCreated() {
        successMessage.should(Condition.appear, BASE_WAITING);
        return this;
    }

    // error_buildTypeName
    public CreateBuildTypePage shouldHaveNameRequiredError() {
        ErrorElement.shouldHaveValidationError(
                "buildTypeName",
                BUILD_TYPE_NAME_REQUIRED_ERROR_MESSAGE
        );
        return this;
    }
}
