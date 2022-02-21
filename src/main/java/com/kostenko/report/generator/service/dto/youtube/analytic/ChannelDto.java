package com.kostenko.report.generator.service.dto.youtube.analytic;

import lombok.Data;

import java.util.Date;

@Data
public class ChannelDto {
    private String channelId;
    private String title;
    private String description;
    private String country;
    private Date publishedAt;
}
