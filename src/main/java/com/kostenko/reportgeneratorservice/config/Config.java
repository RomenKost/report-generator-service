package com.kostenko.reportgeneratorservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

@Configuration
@EnableScheduling
@PropertySource(value = "classpath:settings.yaml", factory = YamlPropertySourceFactory.class)
public class Config {
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Logger getLogger() {
        Handler handler = new ConsoleHandler();
        Logger logger = Logger.getLogger(Config.class.getName());
        logger.addHandler(handler);
        return logger;
    }
}
