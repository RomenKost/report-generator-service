package com.kostenko.reportgeneratorservice.model;

import lombok.Data;

import java.util.Date;

@Data
public class YoutubeChannelDto {
    private String title;
    private String description;
    private String country;
    private Date publishedAt;
}
