package com.kostenko.report.generator.service.report.processor.impl;

import com.kostenko.report.generator.exception.YoutubeAnalyticServiceUnavailableException;
import com.kostenko.report.generator.exception.YoutubeAnalyticTokenExpiredException;
import com.kostenko.report.generator.exception.message.ErrorMessages;
import com.kostenko.report.generator.model.channel.ChannelId;
import com.kostenko.report.generator.service.analytic.client.AnalyticClient;
import com.kostenko.report.generator.service.analytic.security.YoutubeAnalyticSecurityClient;
import com.kostenko.report.generator.service.channel.ChannelIdProcessor;
import com.kostenko.report.generator.model.analytic.Channel;
import com.kostenko.report.generator.model.report.Report;
import com.kostenko.report.generator.model.analytic.Video;
import com.kostenko.report.generator.service.report.saver.ReportSaver;
import com.kostenko.report.generator.service.report.processor.ReportProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@ConditionalOnProperty(
        value = "report-generator.enabled",
        matchIfMissing = true,
        havingValue = "true"
)
@RequiredArgsConstructor
public class YoutubeReportProcessorJob implements ReportProcessor {
    private final ChannelIdProcessor channelIdProcessor;

    private String token;

    private final AnalyticClient analyticClient;
    private final YoutubeAnalyticSecurityClient securityClient;
    private final ReportSaver reportSaver;

    @Override
    @Scheduled(fixedDelayString = "${report-generator.delay}")
    public void processAnalyticService() {
        List<ChannelId> ids = channelIdProcessor.getChannelsIds();
        try {
            ensureToken();
            if (analyticClient.isAlive()) {
                log.info("Count ids to collecting information about channels: " + ids.size());
                ids.stream()
                        .map(ChannelId::getId)
                        .forEach(this::processId);
                log.info("Channel ids was processed.");
            }
        } catch (YoutubeAnalyticServiceUnavailableException e) {
            log.error(ErrorMessages.IMPOSSIBLE_PROCESS_ANALYTIC_SERVICE);
        }
    }

    void processId(String id) {
        try {
            log.info("Processing information about channel with id = " + id + "...");
            Optional<Channel> optionalChannel = analyticClient.getChannel(id, token);
            if (optionalChannel.isPresent()) {
                List<Video> videos = analyticClient.getVideos(id, token);
                reportSaver.save(new Report(id, optionalChannel.get(), videos));
            }
            log.info("Information about channel with id = " + id + " was processed.");
        } catch (YoutubeAnalyticTokenExpiredException e) {
            token = securityClient.getToken();
            processId(id);
        }
    }

    private void ensureToken() {
        if (token == null) {
            token = securityClient.getToken();
        }
    }
}
