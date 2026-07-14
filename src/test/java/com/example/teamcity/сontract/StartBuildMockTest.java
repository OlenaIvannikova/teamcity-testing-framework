package com.example.teamcity.сontract;

import com.example.teamcity.api.BaseApiTest;
import com.example.teamcity.api.models.Build;
import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.PathParams;
import com.example.teamcity.api.requests.UncheckedRequests;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.common.WireMock;
import io.qameta.allure.Feature;
import org.apache.http.HttpStatus;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.example.teamcity.api.enums.Endpoint.*;
import static com.example.teamcity.api.enums.Endpoint.BUILD_STEPS;
import static com.example.teamcity.api.enums.Endpoint.BUILD_TYPES;
import static com.github.tomakehurst.wiremock.client.WireMock.post;


@Feature("Start build")
public class StartBuildMockTest extends BaseApiTest {
    /*
    Настраиваем WireMock для мокирования API так, чтобы
       при POST /buildQueue возвращался код 200 (успешно)
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
        Когда приходит POST-запрос на /buildQueue, он не отправляется в настоящий TeamCity,
        а сразу возвращается подготовленный ответ fakeBuild.
         */
        WireMock.setupServer(
                post(BUILD_QUEUE.getUrl()),
                HttpStatus.SC_OK,
                fakeBuild);

    }

    @Test(description = "User should be able to start build (with WireMock)", groups = {"Regression"})
    public void userStartsBuildWithWireMockTest() {
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        var userCheckedRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));
        var userUncheckedRequests = new UncheckedRequests(Specifications.authSpec(testData.getUser()));

        userCheckedRequests.getRequest(PROJECTS).create(testData.getProject());
        userCheckedRequests.getRequest(BUILD_TYPES).create(testData.getBuildType());
        userUncheckedRequests.getRequest(BUILD_STEPS)
                .create(testData.getBuildStep(), PathParams.create().buildTypeId(testData.getBuildType().getId()));

        Build buildRequest = Build.builder()
                .buildType(
                        BuildType.builder()
                                .id(testData.getBuildType().getId())
                                .build())
                .build();

        // Это не тот же самый объект fakeBuild, а новый экземпляр Build, заполненный данными из ответа WireMock.
        var build = new UncheckedBase(Specifications.mockSpec(), BUILD_QUEUE)
                .create(buildRequest)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(Build.class);

        softy.assertThat(build.getState()).isEqualTo("finished");
        softy.assertThat(build.getStatus()).isEqualTo("SUCCESS");
    }

    @AfterMethod(alwaysRun = true)
    public void stopWireMockServer() {
        WireMock.stopServer();
    }
}