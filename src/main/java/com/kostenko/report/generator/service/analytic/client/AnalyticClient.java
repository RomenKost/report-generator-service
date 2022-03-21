package com.kostenko.report.generator.service.analytic.client;

import com.kostenko.report.generator.service.health.HealthServiceChecker;
import com.kostenko.report.generator.model.analytic.Channel;
import com.kostenko.report.generator.model.analytic.Video;

import java.util.List;
import java.util.Optional;

public interface AnalyticClient extends HealthServiceChecker {
    Optional<Channel> getChannel(String id, String token);

    List<Video> getVideos(String id, String token);
}
