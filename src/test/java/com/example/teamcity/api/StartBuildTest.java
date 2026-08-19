package com.example.teamcity.api;

import com.example.teamcity.BaseTest;
import com.example.teamcity.api.enums.BuildState;
import com.example.teamcity.api.enums.BuildStatus;
import com.example.teamcity.api.locator.Locator;
import com.example.teamcity.api.models.*;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.PathParams;
import com.example.teamcity.api.requests.QueryParams;
import com.example.teamcity.api.requests.UncheckedRequests;
import com.example.teamcity.api.spec.Specifications;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

import static com.example.teamcity.api.enums.Endpoint.*;
import static com.example.teamcity.api.enums.Endpoint.BUILD_TYPES;


@Feature("Start build")
public class StartBuildTest extends BaseTest {
    private static final String EXPECTED_LOG_MESSAGE = "Hello, world!";

    private static final int TIMEOUT = 80_000;
    private static final long POLL_INTERVAL = 20_000;

    @Test(description = "User should be able to start build")
    public void userStartsBuildTest() {
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        var userCheckedRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));
        var userUncheckedRequests = new UncheckedRequests(Specifications.authSpec(testData.getUser()));

        userCheckedRequests.getRequest(PROJECTS).create(testData.getProject());

        // Создание Build Configuration
        userCheckedRequests.getRequest(BUILD_TYPES).create(testData.getBuildType());

        // Добавление Build Steps в Build Configuration
        userUncheckedRequests.getRequest(BUILD_STEPS)
                .create(
                        testData.getBuildStep(),
                        PathParams.create().buildTypeId(testData.getBuildType().getId())
                );

        // Запрос на запуск
        Build buildQueueRequest = Build.builder()
                .buildType(
                        BuildType.builder()
                                .id(testData.getBuildType().getId())
                                .build())
                .build();

        // Запуск Build по уже существующей Build Configuration-> Build попадает в очередь
        Build queuedBuild = userUncheckedRequests.getRequest(BUILD_QUEUE)
                .create(buildQueueRequest)
                .then()
                .extract()
                .as(Build.class);

        Build finishedBuild = waitUntilFinished(
                queuedBuild.getId(),
                userCheckedRequests,
                TIMEOUT,
                POLL_INTERVAL);

        softy.assertThat(finishedBuild.getState()).isEqualTo(BuildState.FINISHED.getValue());
        softy.assertThat(finishedBuild.getStatus()).isEqualTo(BuildStatus.SUCCESS.getValue());

        String log = superUserCheckRequests.getRequest(BUILD_LOG)
                .read(QueryParams.create().buildId(finishedBuild.getId()).build());

        softy.assertThat(log).contains(EXPECTED_LOG_MESSAGE);
    }

    private Build waitUntilFinished(String buildId,
                                    CheckedRequests requests,
                                    int timeout,
                                    long interval) {

        long deadline = System.currentTimeMillis() + timeout;

        while (System.currentTimeMillis() < deadline) {

            Build build = requests
                    .<Build>getRequest(BUILDS)
                    .read(Locator.byId(buildId));

            if (BuildState.FINISHED.getValue().equals(build.getState())) {
                return build;
            }

            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Waiting for build was interrupted.", e);
            }
        }

        throw new AssertionError(
                "Build " + buildId + " did not finish within " + timeout + " ms.");
    }

}
