package com.example.teamcity.api.enums;

import com.example.teamcity.api.models.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
Для связки 2х констант: URL эндпоинта и model, в которую мы сериализуем тело запроса и тело ответа
 */
@AllArgsConstructor
@Getter
public enum Endpoint {
    BUILD_TYPES("/app/rest/buildTypes", BuildType.class),
    BUILD_QUEUE("/app/rest/buildQueue", Build.class),
    PROJECTS("/app/rest/projects", Project.class),
    USERS("/app/rest/users", User.class);

    private final String url;
    private final Class<? extends BaseModel> modelClass;
}
