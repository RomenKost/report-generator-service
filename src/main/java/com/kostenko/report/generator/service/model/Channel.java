package com.kostenko.report.generator.service.model;

import lombok.Data;

import java.util.Date;

@Data
public class Channel {
    private String channelId;
    private String title;
    private String description;
    private String country;
    private Date publishedAt;
}
