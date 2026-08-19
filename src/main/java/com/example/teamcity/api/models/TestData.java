package com.example.teamcity.api.models;

import lombok.Data;

import java.util.List;

@Data
public class TestData {
    private Project project;
    private User user;
    private BuildType buildType;

    public Step getBuildStep() {
        List<Property> propertyList = List.of(
                Property.builder().name("use.custom.script").value("true").build(),
                Property.builder().name("script.content").value("echo \"Hello, world!\"").build(),
                Property.builder().name("teamcity.step.mode").value("default").build()
        );

        return Step.builder()
                .name("Print Hello")
                .type("simpleRunner")
                .properties(
                        Properties.builder()
                                .property(propertyList)
                                .count(propertyList.size())
                                .build()
                )
                .build();
    }
}
