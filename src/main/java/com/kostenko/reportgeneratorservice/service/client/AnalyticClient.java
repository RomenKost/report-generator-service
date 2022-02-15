package com.kostenko.reportgeneratorservice.service.client;

import com.kostenko.reportgeneratorservice.dto.ChannelDto;
import com.kostenko.reportgeneratorservice.dto.VideoDto;
import com.kostenko.reportgeneratorservice.service.healthchecker.HealthServiceChecker;

public interface AnalyticClient extends HealthServiceChecker {
    ChannelDto getChannelDto(String id);

    VideoDto[] getVideoDtoArray(String id);
}
