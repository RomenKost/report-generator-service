package com.kostenko.reportgeneratorservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum URLs {
    CHANNEL_STATISTIC("http://{address}:{port}/statistic?id={id}"),
    VIDEO_STATISTIC("http://{address}:{port}/videos?id={id}"),
    HEALTH("http://{address}:{port}/actuator/health");

    private final String url;
}
