package com.kostenko.report.generator.service.dto.youtube.analytic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ChannelDto {
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
}
