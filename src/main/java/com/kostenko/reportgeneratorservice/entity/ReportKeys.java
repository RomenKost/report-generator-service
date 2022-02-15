package com.kostenko.reportgeneratorservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportKeys {
    CHANNEL_TITLE("channelTitle"),
    CHANNEL_DESCRIPTION("channelDescription"),
    CHANNEL_PUBLISHED_AT("channelPublishedAt"),
    CHANNEL_COUNTRY("channelCountry"),

    CHANNEL_VIDEOS("videos"),

    VIDEO_TITLE("title"),
    VIDEO_DESCRIPTION("description"),
    VIDEO_PUBLISHED_AT("publishedAt"),
    VIDEO_ID("id");

    private final String name;
}
