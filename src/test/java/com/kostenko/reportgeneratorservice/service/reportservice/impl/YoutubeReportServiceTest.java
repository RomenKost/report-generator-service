package com.kostenko.reportgeneratorservice.service.reportservice.impl;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.kostenko.reportgeneratorservice.config.ApplicationProperties;
import com.kostenko.reportgeneratorservice.dto.DTOs;
import com.kostenko.reportgeneratorservice.logger.LoggerChecker;
import com.kostenko.reportgeneratorservice.mapper.DtoToModelMapper;
import com.kostenko.reportgeneratorservice.model.Models;
import com.kostenko.reportgeneratorservice.service.client.AnalyticClient;
import com.kostenko.reportgeneratorservice.service.reportsaver.ReportSaver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class YoutubeReportServiceTest {
    private AnalyticClient analyticClient;
    private ReportSaver reportSaver;
    private DtoToModelMapper dtoToModelMapper;

    private ListAppender<ILoggingEvent> listAppender;

    private YoutubeReportService youtubeReportService;

    @BeforeAll
    public void initialize() {
        ApplicationProperties properties = new ApplicationProperties(List.of("any_id"));

        analyticClient = mock(AnalyticClient.class);
        reportSaver = mock(ReportSaver.class);
        dtoToModelMapper = mock(DtoToModelMapper.class);

        youtubeReportService = new YoutubeReportService(analyticClient, reportSaver, dtoToModelMapper, properties);

        Logger logger = (Logger) LoggerFactory.getLogger(YoutubeReportService.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
    }

    @AfterEach
    public void clear() {
        Mockito.reset(analyticClient);
        Mockito.reset(reportSaver);
        listAppender.list.clear();
    }

    @Test
    void loadDeadYoutubeAnalyticServiceTest() {
        Mockito.when(analyticClient.isAlive())
                .thenReturn(false);

        youtubeReportService.loadAnalyticService();

        Iterator<ILoggingEvent> eventIterator = listAppender.list.listIterator();
        LoggerChecker.checkLog(eventIterator, Level.WARN, "Youtube analytic server is unavailable.");
        assertFalse(eventIterator.hasNext());
    }

    @Test
    void loadAliveYoutubeAnalyticServiceTest() throws IOException {
        String anyId = "any_id";

        Mockito.when(analyticClient.isAlive())
                .thenReturn(true);
        Mockito.when(analyticClient.getChannelDto(anyId))
                .thenReturn(DTOs.getChannel());
        Mockito.when(analyticClient.getVideoDtoArray(anyId))
                .thenReturn(DTOs.getVideos());
        Mockito.when(dtoToModelMapper.channelDtoToChannel(DTOs.getChannel()))
                .thenReturn(Models.getChannel());
        Mockito.when(dtoToModelMapper.videoDtoArrayToVideoArray(DTOs.getVideos()))
                .thenReturn(Models.getVideos());

        youtubeReportService.loadAnalyticService();

        Mockito.verify(reportSaver)
                .save(Models.getReport(anyId));


        Iterator<ILoggingEvent> eventIterator = listAppender.list.listIterator();
        LoggerChecker.checkLog(
                eventIterator, Level.INFO,
                "Youtube analytic server is available, count ids to collecting information about channels: 1"
        );
        assertFalse(eventIterator.hasNext());
    }

    @Test
    void loadDisconnectedYoutubeAnalyticServiceTest() {
        String anyId = "any_id";
        RestClientException restClientException = new RestClientException("Server disconnected.");

        Mockito.when(analyticClient.isAlive())
                .thenReturn(true);
        Mockito.when(analyticClient.getVideoDtoArray(anyId))
                .thenThrow(restClientException);

        youtubeReportService.loadAnalyticService();

        Iterator<ILoggingEvent> eventIterator = listAppender.list.listIterator();

        LoggerChecker.checkLog(
                eventIterator, Level.INFO,
                "Youtube analytic server is available, count ids to collecting information about channels: 1"
        );
        LoggerChecker.checkErrorLog(
                eventIterator,
                "Information about channel with id=" + anyId + " wasn't collected.",
                restClientException
        );

        assertFalse(eventIterator.hasNext());
    }

    @Test
    void loadYoutubeAnalyticServiceWithSavingExceptionTest() throws IOException {
        String anyId = "any_id";
        IOException savingException = new IOException("Saving exception.");

        Mockito.when(analyticClient.isAlive())
                .thenReturn(true);
        Mockito.when(analyticClient.getChannelDto(anyId))
                .thenReturn(DTOs.getChannel());
        Mockito.when(analyticClient.getVideoDtoArray(anyId))
                .thenReturn(DTOs.getVideos());
        Mockito.when(dtoToModelMapper.channelDtoToChannel(DTOs.getChannel()))
                .thenReturn(Models.getChannel());
        Mockito.when(dtoToModelMapper.videoDtoArrayToVideoArray(DTOs.getVideos()))
                .thenReturn(Models.getVideos());
        Mockito.doThrow(savingException)
                .when(reportSaver)
                .save(Models.getReport(anyId));

        youtubeReportService.loadAnalyticService();

        Iterator<ILoggingEvent> eventIterator = listAppender.list.listIterator();

        LoggerChecker.checkLog(
                eventIterator, Level.INFO,
                "Youtube analytic server is available, count ids to collecting information about channels: 1"
        );
        LoggerChecker.checkErrorLog(
                eventIterator,
                "Unavailable to save report about channel with id=" + anyId,
                savingException
        );

        assertFalse(eventIterator.hasNext());
    }
}