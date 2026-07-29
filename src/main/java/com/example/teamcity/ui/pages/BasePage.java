package com.example.teamcity.ui.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.elements.BasePageElement;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

// Родитель всех Page Object
public abstract class BasePage {
    protected static final Duration BASE_WAITING = Duration.ofSeconds(50);
    protected static final Duration LONG_WAITING = Duration.ofMinutes(3);

    /*
    ElementCollection: SelenideElement 1, SelenideElement 2 и т.д.
    collection.stream() -> Конвеер: SelenideElement 1, SelenideElement 2 и т.д.
    creator(SelenideElement 1) -> T -> add to list
    creator(SelenideElement 2) -> T -> add to list
     */
    protected <T extends BasePageElement> List<T> generatePageElements(
            ElementsCollection collection, Function<SelenideElement, T> creator) {
        return collection.stream().map(creator).toList();
    }

}
