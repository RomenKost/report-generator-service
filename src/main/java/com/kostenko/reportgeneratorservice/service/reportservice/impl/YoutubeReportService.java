package com.kostenko.reportgeneratorservice.service.reportservice.impl;

import com.kostenko.reportgeneratorservice.config.ApplicationProperties;
import com.kostenko.reportgeneratorservice.entity.Logs;
import com.kostenko.reportgeneratorservice.dto.ChannelDto;
import com.kostenko.reportgeneratorservice.dto.VideoDto;
import com.kostenko.reportgeneratorservice.mapper.DtoToModelMapper;
import com.kostenko.reportgeneratorservice.model.Channel;
import com.kostenko.reportgeneratorservice.model.Report;
import com.kostenko.reportgeneratorservice.model.Video;
import com.kostenko.reportgeneratorservice.service.client.AnalyticClient;
import com.kostenko.reportgeneratorservice.service.reportsaver.ReportSaver;
import com.kostenko.reportgeneratorservice.service.reportservice.ReportService;
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

    private final DtoToModelMapper dtoToModelMapper;
    private final AnalyticClient analyticClient;
    private final ReportSaver reportSaver;

    public YoutubeReportService(AnalyticClient analyticClient,
                                ReportSaver reportSaver,
                                DtoToModelMapper dtoToModelMapper,
                                ApplicationProperties applicationProperties) {
        this.dtoToModelMapper = dtoToModelMapper;
        this.analyticClient = analyticClient;
        this.ids = applicationProperties.getId();
        this.reportSaver = reportSaver;
    }

    @Override
    @Scheduled(fixedDelayString = "${application.delay}")
    public void loadAnalyticService() {
        if (analyticClient.isAlive()) {
            ids.forEach(this::processId);
        } else {
            log.warn(Logs.UNAVAILABLE.getMessage());
        }
    }

    private void processId(String id) {
        try {
            ChannelDto channelDto = analyticClient.getChannelDto(id);
            VideoDto[] videoDtoArray = analyticClient.getVideoDtoArray(id);

            Channel channel = dtoToModelMapper.channelDtoToChannel(channelDto);
            Video[] videos = dtoToModelMapper.videoDtoArrayToVideoArray(videoDtoArray);

            Report report = new Report(id, channel, videos);
            reportSaver.save(report);

            log.info(Logs.CREATED_REPORT.getMessage(id));
        } catch (IOException e) {
            log.error(Logs.REPORT_SAVING_EXCEPTION.getMessage(), e);
        } catch (RestClientException e) {
            log.error(Logs.MICROSERVICE_DISCONNECTED.getMessage(), e);
        }
    }
}
