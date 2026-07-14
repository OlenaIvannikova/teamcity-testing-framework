package com.example.teamcity;

import com.example.teamcity.api.generators.TestDataStorage;
import com.example.teamcity.api.models.TestData;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.spec.Specifications;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.assertj.core.api.SoftAssertions;

import static com.example.teamcity.api.generators.TestDataGenerator.generate;

public class BaseTest {
    protected SoftAssertions softy;
    protected CheckedRequests superUserCheckRequests = new CheckedRequests(Specifications.superUserSpec());
    protected TestData testData;

    @BeforeMethod(alwaysRun = true)
    public void beforeTest() {
        softy = new SoftAssertions();
        testData = generate();
    }

    @AfterMethod(alwaysRun = true)
    public void afterTest() {
        TestDataStorage.getStorage().deleteCreatedEntities();
        softy.assertAll();
    }
}
