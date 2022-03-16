package com.kostenko.report.generator.model.analytic;

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
