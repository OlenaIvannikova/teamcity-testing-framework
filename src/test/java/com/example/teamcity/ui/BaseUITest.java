package com.example.teamcity.ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.example.teamcity.BaseTest;
import com.example.teamcity.api.config.Config;
import com.example.teamcity.api.models.User;
import com.example.teamcity.ui.pages.LoginPage;
import io.qameta.allure.selenide.AllureSelenide;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;

import java.util.Map;

import static com.example.teamcity.api.enums.Endpoint.USERS;
import static io.qameta.allure.Allure.step;

public class BaseUITest extends BaseTest {

    protected static final String REPO_URL = "https://github.com/AlexPshe/spring-core-for-qa";
    private static final String HTTP = "http://";

    @BeforeSuite(alwaysRun = true)
    public void setUpUITest() {
        Configuration.browser = Config.getProperty("browser");
        Configuration.baseUrl = HTTP + Config.getProperty("host");

        // НЕ ПИШИТЕ UI ТЕСТЫ С ЛОКАЛЬНЫМ БРАУЗЕРОМ, А ПОТОМ ЗАПУСКАЕТЕ НА REMOTE BROWSER !!!!
        Configuration.remote = Config.getProperty("remote");
        Configuration.browserSize = Config.getProperty("browserSize");

        Configuration.browserCapabilities.setCapability(
                "selenoid:options",
                Map.of(
                        "enableVNC", true, //Разрешить подключение к браузеру через VNC -> позволяет видеть браузер в режиме реального времени
                        "enableLog", true     // Сохранять лог браузерной сессии
                )
        );
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true)
                .includeSelenideSteps(true));
    }

    @AfterMethod(alwaysRun = true)
    public void closeWebDriver() {
        Selenide.closeWebDriver();
    }

    protected User loginAsNewUser() {
        User user = createUser();
        LoginPage.open().login(user);
        return user;
    }

    protected User createUser() {
        return step(
                "Create user",
                () -> {
                    var user = testData.getUser();
                    superUserCheckRequests
                            .getRequest(USERS)
                            .create(user);
                    return user;
                }
        );
    }
}

