package com.kostenko.reportgeneratorservice.service.client.impl.youtubeanalyticclient;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum URLParameters {
    ADDRESS("address"),
    PORT("port"),
    ID("id");

    private final String name;
}
