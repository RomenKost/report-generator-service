package com.kostenko.report.generator.service.service.client.youtube.analytic;

import com.kostenko.report.generator.service.dto.youtube.analytic.ChannelDto;
import com.kostenko.report.generator.service.dto.youtube.analytic.VideoDto;
import com.kostenko.report.generator.service.service.health.checker.HealthServiceChecker;

public interface AnalyticClient extends HealthServiceChecker {
    ChannelDto getChannelDto(String id);

    VideoDto[] getVideoDtoArray(String id);
}
