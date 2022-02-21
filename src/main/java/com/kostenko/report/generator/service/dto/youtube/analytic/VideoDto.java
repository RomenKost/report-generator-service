package com.kostenko.report.generator.service.dto.youtube.analytic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class VideoDto {
    @JsonProperty("videoId")
    private String videoId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("publishedAt")
    private Date publishedAt;
}
