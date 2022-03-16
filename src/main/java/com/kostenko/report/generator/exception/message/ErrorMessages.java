package com.kostenko.report.generator.exception.message;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorMessages {
    public final String YOUTUBE_ANALYTIC_SERVICE_UNAVAILABLE = "Youtube analytic service is unavailable.";
    public final String IMPOSSIBLE_PROCESS_ANALYTIC_SERVICE = "Impossible process analytic service.";

    public final String TOKEN_EXPIRED = "Youtube analytic token was expired.";
    public final String USER_DISABLED = "User was disabled.";
    public final String INVALID_USER_PASSWORD = "Password that was passed to get jwt token is invalid.";
    public final String INVALID_USERNAME = "Username that was passed to get jwt token wasn't found.";
    public final String UNSUPPORTED_RESPONSE_FORMAT = "Format of response entity is unsupported";

    public final String REPORT_NOT_SAVED = "Report for channel with id = %s wasn't saved.";
    public final String CHANNEL_NOT_FOUND = "Channel wasn't found.";
}
