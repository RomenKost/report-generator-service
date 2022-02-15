package com.kostenko.reportgeneratorservice.service;

import com.kostenko.reportgeneratorservice.config.ApplicationProperties;
import com.kostenko.reportgeneratorservice.entity.Logs;
import com.kostenko.reportgeneratorservice.model.YoutubeChannelDto;
import com.kostenko.reportgeneratorservice.model.YoutubeVideoDto;
import com.kostenko.reportgeneratorservice.service.reportgenerator.ReportGenerator;
import com.kostenko.reportgeneratorservice.service.reportsaver.ReportSaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class YoutubeReportService {
    private final List<String> ids;

    private final YoutubeAnalyticClient client;
    private final ReportGenerator generator;
    private final ReportSaver saver;
    private final Logger logger;

    public YoutubeReportService(@Autowired Logger logger,
                                @Autowired YoutubeAnalyticClient client,
                                @Autowired ApplicationProperties properties,
                                @Autowired ReportGenerator generator,
                                @Autowired ReportSaver saver) {
        this.client = client;
        this.logger = logger;

        this.ids = properties.getId();
        this.generator = generator;
        this.saver = saver;
    }

    @Scheduled(fixedDelayString = "${application.delay}")
    public void loadYoutubeAnalyticService() {
        if (client.checkYoutubeAnalyticService()) {
            ids.forEach(this::processId);
        } else {
            logger.info(Logs.UNAVAILABLE.getMessage());
        }
    }

    private void processId(String id) {
        try {
            YoutubeChannelDto youtubeChannelDto = client.getYoutubeChannelDto(id);
            YoutubeVideoDto[] youtubeVideoDtoArray = client.getYoutubeVideoDtoArray(id);

            String report = generator.generateReport(youtubeChannelDto, youtubeVideoDtoArray);
            saver.save(report, id);

            logger.info(Logs.CREATED_REPORT.getMessage(id));
        } catch (IOException e) {
            logger.log(Level.WARNING, Logs.REPORT_SAVING_EXCEPTION.getMessage(), e);
        } catch (RestClientException e) {
            logger.log(Level.WARNING, Logs.MICROSERVICE_DISCONNECTED.getMessage(), e);
        }
    }
}
