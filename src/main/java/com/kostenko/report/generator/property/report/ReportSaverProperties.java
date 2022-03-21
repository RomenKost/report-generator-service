package com.kostenko.report.generator.property.report;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "report-generator")
public class ReportSaverProperties {
    private String folder;
    private Patterns patterns;

    @Getter
    public static class Patterns {
        private DateFormat time;

        @Setter
        private String report;

        public void setTime(String time){
            this.time = new SimpleDateFormat(time);
        }
    }
}
