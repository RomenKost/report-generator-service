package com.kostenko.reportgeneratorservice.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Logs {
    UNAVAILABLE("Youtube analytic server is unavailable."),
    CREATED_REPORT("Report was created id = %s."),
    REPORT_SAVING_EXCEPTION("Exception was thrown during saving report."),
    MICROSERVICE_DISCONNECTED("Microservice was disconnected during making request.");

    private final String message;

    public String getMessage() {
        return message;
    }

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
