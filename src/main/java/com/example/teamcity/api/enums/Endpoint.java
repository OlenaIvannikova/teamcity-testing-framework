package com.example.teamcity.api.enums;

import com.example.teamcity.api.models.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Для связки 2х констант: URL эндпоинта и model, в которую мы сериализуем тело запроса и тело ответа
 */

@Getter
@AllArgsConstructor
public enum Endpoint {
    USERS("/app/rest/users", User.class),
    PROJECTS("/app/rest/projects", Project.class),
    BUILD_TYPES("/app/rest/buildTypes", BuildType.class),
    BUILD_TYPES_LIST("/app/rest/buildTypes", BuildTypes.class),
    BUILD_STEPS("/app/rest/buildTypes/id:%s/steps", Step.class),
    BUILD_QUEUE("/app/rest/buildQueue", Build.class),
    BUILDS("/app/rest/builds", Build.class),
    BUILD_LOG("downloadBuildLog.html", null);

    private final String url;
    private final Class<? extends BaseModel> modelClass;

    public String getUrl() {
        return url;
    }

    public String getUrl(Object... params) {
        return String.format(url, params);
    }
}
