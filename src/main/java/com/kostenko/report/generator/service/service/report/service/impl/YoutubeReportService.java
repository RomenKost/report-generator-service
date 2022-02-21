package com.kostenko.report.generator.service.service.report.service.impl;

import com.kostenko.report.generator.service.mapper.youtube.analytic.ChannelMapper;
import com.kostenko.report.generator.service.mapper.youtube.analytic.VideoMapper;
import com.kostenko.report.generator.service.config.ApplicationProperties;
import com.kostenko.report.generator.service.dto.youtube.analytic.ChannelDto;
import com.kostenko.report.generator.service.dto.youtube.analytic.VideoDto;
import com.kostenko.report.generator.service.model.Channel;
import com.kostenko.report.generator.service.model.Report;
import com.kostenko.report.generator.service.model.Video;
import com.kostenko.report.generator.service.service.client.youtube.analytic.AnalyticClient;
import com.kostenko.report.generator.service.service.report.saver.ReportSaver;
import com.kostenko.report.generator.service.service.report.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class YoutubeReportService implements ReportService {
    private final List<String> ids;

    private final VideoMapper videoMapper;
    private final ChannelMapper channelMapper;

    private final AnalyticClient analyticClient;
    private final ReportSaver reportSaver;

    public YoutubeReportService(AnalyticClient analyticClient,
                                ReportSaver reportSaver,
                                ApplicationProperties applicationProperties,
                                VideoMapper videoMapper,
                                ChannelMapper channelMapper) {
        this.videoMapper = videoMapper;
        this.channelMapper = channelMapper;
        this.analyticClient = analyticClient;
        this.ids = applicationProperties.getId();
        this.reportSaver = reportSaver;
    }

    @Override
    @Scheduled(fixedDelayString = "${application.delay}")
    public void loadAnalyticService() {
        if (analyticClient.isAlive()) {
            log.info("Youtube analytic server is available, count ids to collecting information about channels: " + ids.size());
            ids.forEach(this::processId);
        } else {
            log.warn("Youtube analytic server is unavailable.");
        }
    }

    private void processId(String id) {
        try {
            ChannelDto channelDto = analyticClient.getChannelDto(id);
            VideoDto[] videoDtoArray = analyticClient.getVideoDtoArray(id);

            Channel channel = channelMapper.channelDtoToChannel(channelDto);
            Video[] videos = videoMapper.videoDtoArrayToVideoArray(videoDtoArray);

            Report report = new Report(id, channel, videos);
            reportSaver.save(report);
        } catch (IOException e) {
            log.error("Unavailable to save report about channel with id=" + id, e);
        } catch (RestClientException e) {
            log.error("Information about channel with id=" + id + " wasn't collected.", e);
        }
    }
}
