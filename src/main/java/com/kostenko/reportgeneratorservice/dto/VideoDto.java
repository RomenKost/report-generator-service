package com.kostenko.reportgeneratorservice.dto;

import lombok.Data;

import java.util.Date;

@Data
public class VideoDto {
    private String videoId;
    private String title;
    private String description;
    private Date publishedAt;
}
