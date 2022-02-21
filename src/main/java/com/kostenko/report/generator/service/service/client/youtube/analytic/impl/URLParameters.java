package com.kostenko.report.generator.service.service.client.youtube.analytic.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum URLParameters {
    ADDRESS("address"),
    PORT("port"),
    ID("channelId");

    private final String name;
}
