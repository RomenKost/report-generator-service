package com.kostenko.reportgeneratorservice.model;

import lombok.Data;

import java.util.Date;

@Data
public class YoutubeVideoDto {
    private String videoId;
    private String title;
    private String description;
    private Date publishedAt;
}
