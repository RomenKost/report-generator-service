package com.kostenko.reportgeneratorservice.service.reportservice.impl;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.read.ListAppender;
import com.kostenko.reportgeneratorservice.config.ApplicationProperties;
import com.kostenko.reportgeneratorservice.dto.DTOs;
import com.kostenko.reportgeneratorservice.entity.Logs;
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

        checkLog(Level.WARN, Logs.UNAVAILABLE.getMessage());
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
        checkLog(Level.INFO, Logs.CREATED_REPORT.getMessage(anyId));
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

        checkErrorLog(Logs.MICROSERVICE_DISCONNECTED.getMessage(), restClientException);
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

        checkErrorLog(Logs.REPORT_SAVING_EXCEPTION.getMessage(), savingException);
    }

    private void checkLog(Level expectedLevel, String expectedMessage) {
        assertEquals(1, listAppender.list.size());
        ILoggingEvent loggingEvent = listAppender.list.get(0);

        assertEquals(expectedLevel, loggingEvent.getLevel());
        assertEquals(expectedMessage, loggingEvent.getMessage());
    }

    private void checkErrorLog(String expectedMessage, Exception expectedException) {
        assertEquals(1, listAppender.list.size());
        ILoggingEvent loggingEvent = listAppender.list.get(0);

        assertEquals(Level.ERROR, loggingEvent.getLevel());
        assertEquals(expectedMessage, loggingEvent.getMessage());

        IThrowableProxy iThrowableProxy = loggingEvent.getThrowableProxy();
        assertTrue(iThrowableProxy instanceof ThrowableProxy);
        assertEquals(expectedException, ((ThrowableProxy) iThrowableProxy).getThrowable());
    }
}