package com.kostenko.report.generator.dto.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.Date;
import java.util.List;

@Getter
@Builder
@Jacksonized
@EqualsAndHashCode
public class ReportDto {
    @JsonProperty("id")
    private String channelId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("country")
    private String country;

    @JsonProperty("publishedAt")
    private Date publishedAt;

    @JsonProperty("videos")
    private List<VideoDto> videos;

    @Getter
    @Builder
    @Jacksonized
    @EqualsAndHashCode
    public static class VideoDto {
        @JsonProperty("videoId")
        private String videoId;

        @JsonProperty("title")
        private String title;

        @JsonProperty("description")
        private String description;

        @JsonProperty("publishedAt")
        private Date publishedAt;
    }
}
