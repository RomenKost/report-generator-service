package com.kostenko.report.generator.service.mapper.report.tostring.mapper.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JsonKeys {
    CHANNEL_ID("id"),
    CHANNEL_TITLE("title"),
    CHANNEL_DESCRIPTION("description"),
    CHANNEL_PUBLISHED_AT("published_at"),
    CHANNEL_COUNTRY("country"),
    CHANNEL_VIDEOS("videos"),
    VIDEO_ID("id"),
    VIDEO_TITLE("title"),
    VIDEO_DESCRIPTION("description"),
    VIDEO_PUBLISHED_AT("published_at");

    private final String key;
}
