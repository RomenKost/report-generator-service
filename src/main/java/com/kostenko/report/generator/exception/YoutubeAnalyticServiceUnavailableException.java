package com.kostenko.report.generator.exception;

public class YoutubeAnalyticServiceUnavailableException extends RuntimeException {
    public YoutubeAnalyticServiceUnavailableException(String message) {
        super(message);
    }

    public YoutubeAnalyticServiceUnavailableException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
