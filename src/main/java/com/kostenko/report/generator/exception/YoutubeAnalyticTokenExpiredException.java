package com.kostenko.report.generator.exception;

import com.kostenko.report.generator.exception.message.ErrorMessages;
import lombok.Getter;

@Getter
public class YoutubeAnalyticTokenExpiredException extends RuntimeException{
    private final String token;

    public YoutubeAnalyticTokenExpiredException(String token, Throwable throwable) {
        super(ErrorMessages.TOKEN_EXPIRED, throwable);
        this.token = token;
    }
}
