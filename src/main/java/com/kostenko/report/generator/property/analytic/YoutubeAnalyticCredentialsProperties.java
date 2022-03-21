package com.kostenko.report.generator.property.analytic;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "youtube-analytic.credentials")
public class YoutubeAnalyticCredentialsProperties {
    private String username;
    private String password;
}
