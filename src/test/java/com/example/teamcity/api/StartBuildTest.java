package com.example.teamcity.api;

import com.example.teamcity.api.models.Build;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.common.WireMock;
import io.qameta.allure.Feature;
import org.apache.http.HttpStatus;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.example.teamcity.api.enums.Endpoint.BUILD_QUEUE;
import static com.github.tomakehurst.wiremock.client.WireMock.post;


@Feature("Start build")
public class StartBuildTest extends BaseApiTest {

    /*
    Настраиваем WireMock для мокирования API так, чтобы
       при POST-запросе на /buildQueue (post(BUILD_QUEUE.getUrl()))
       возвращался код 200 (успешно) (HttpStatus.SC_OK)
       и заранее подготовленный в теле ответа JSON fakeBuild
       с состоянием finished и статусом SUCCESS, имитируя успешный ответ TeamCity
     */
    @BeforeMethod(alwaysRun = true)
    public void setupWireMockServer() {
        var fakeBuild = Build.builder()
                .state("finished")
                .status("SUCCESS")
                .build();

        /*
        Когда приходит POST-запрос на /buildQueue,
        он не отправляется в настоящий TeamCity,
        а сразу возвращается подготовленный ответ fakeBuild.
         */
        WireMock.setupServer(
                post(BUILD_QUEUE.getUrl()),
                HttpStatus.SC_OK,
                fakeBuild);

    }

    @Test(description = "User should be able to start build (with WireMock)", groups = {"Regression"})
    public void userStartsBuildWithWireMockTest() {
        var uncheckedBuildQueueRequest = new UncheckedBase(Specifications.mockSpec(), BUILD_QUEUE);

        /*
        Это не тот же самый объект fakeBuild, а новый экземпляр Build,
        заполненный данными из ответа WireMock.
        */
        var build = uncheckedBuildQueueRequest.create(Build.builder()
                        .buildType(testData.getBuildType())
                        .build())
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(Build.class);

        softy.assertThat(build.getState())
                .as("buildState")
                .isEqualTo("finished");
    }

    @AfterMethod(alwaysRun = true)
    public void stopWireMockServer() {
        WireMock.stopServer();
    }
}