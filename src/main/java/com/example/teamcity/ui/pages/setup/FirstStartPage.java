package com.example.teamcity.ui.pages.setup;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.pages.BasePage;

import static com.codeborne.selenide.Selenide.$;
import static io.qameta.allure.Allure.step;

public class FirstStartPage extends BasePage {
    private final SelenideElement restoreButton = $("#restoreButton");
    private final SelenideElement proceedButton = $("#proceedButton");
    private final SelenideElement dbTypeSelect = $("#dbType");
    private final SelenideElement acceptLicenseCheckbox = $("#accept");
    private final SelenideElement submitButton = $("input[type='submit']");

    public FirstStartPage() {
        restoreButton.shouldBe(Condition.visible, LONG_WAITING);
    }

    public static FirstStartPage open() {
        step("Open first start page");
        return Selenide.open("/", FirstStartPage.class);
    }

    public FirstStartPage setupFirstStart() {
        step("Proceed to database setup");
        proceedButton.click();

        step("Select database type");
        dbTypeSelect.shouldBe(Condition.visible, LONG_WAITING);
        proceedButton.click();

        step("Accept license agreement");
        acceptLicenseCheckbox.should(Condition.exist, LONG_WAITING).scrollTo().click();

        step("Submit first start setup");
        submitButton.click();
        return this;
    }

}

