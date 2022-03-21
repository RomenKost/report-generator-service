package com.kostenko.report.generator.property.analytic;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "youtube-analytic.urls")
public class YoutubeAnalyticUrlsProperties {
    private String channelsURL;
    private String videosURL;
    private String healthURL;

    private String loginURL;
}
