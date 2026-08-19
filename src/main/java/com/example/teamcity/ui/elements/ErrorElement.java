package com.example.teamcity.ui.elements;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;

public class ErrorElement {

    private static final String ERROR_ID_PREFIX = "#error_%s";

    public static void shouldHaveValidationError(String selector, String errorMessage){
        var error = $(ERROR_ID_PREFIX.formatted(selector));
        error.shouldHave(exactText(errorMessage));
    }
}