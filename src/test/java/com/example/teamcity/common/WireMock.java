package com.example.teamcity.common;

import com.example.teamcity.api.models.BaseModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.common.ContentTypes.APPLICATION_JSON;
import static com.github.tomakehurst.wiremock.common.ContentTypes.CONTENT_TYPE;

public final class WireMock {
    private static WireMockServer wireMockServer;

    private WireMock() {
    }


    public static void setupServer(MappingBuilder mappingBuilder, int status, BaseModel model) {
        if (wireMockServer == null) {
            wireMockServer = new WireMockServer(8089);
            wireMockServer.start();
        }

        try {
            // ObjectMapper из библиотеки Jackson преобразует Java-объект в JSON
            var jsonModel = new ObjectMapper().writeValueAsString(model);

        /*
        Если придет запрос, соответствующий mappingBuilder, то он не пересылается настоящему серверу,
        а сразу возвращается ответ с jsonModel
         */
            wireMockServer.stubFor(mappingBuilder
                    .willReturn(aResponse()
                            .withStatus(status)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                            .withBody(jsonModel)));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize model", e);
        }
    }

    public static void stopServer() {
        if (wireMockServer != null) {
            wireMockServer.stop();
            wireMockServer = null;
        }
    }
}



