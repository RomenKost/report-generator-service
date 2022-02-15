package com.kostenko.reportgeneratorservice.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;


@Getter
@Component
@AllArgsConstructor
@ConfigurationProperties(prefix = "channels")
public class ApplicationProperties {
    private List<String> id;
}
