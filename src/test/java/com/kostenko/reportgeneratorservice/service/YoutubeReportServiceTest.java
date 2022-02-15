package com.kostenko.reportgeneratorservice.service;

import com.kostenko.reportgeneratorservice.config.ApplicationProperties;
import com.kostenko.reportgeneratorservice.entity.Logs;
import com.kostenko.reportgeneratorservice.model.Entities;
import com.kostenko.reportgeneratorservice.service.reportgenerator.ReportGenerator;
import com.kostenko.reportgeneratorservice.service.reportsaver.ReportSaver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class YoutubeReportServiceTest {
    private Logger logger;
    private YoutubeAnalyticClient client;
    private ReportGenerator generator;
    private ReportSaver saver;

    private YoutubeReportService youtubeReportService;

    @BeforeAll
    public void initialize() {
        ApplicationProperties properties = new ApplicationProperties(List.of("any_id", "another_id"));

        logger = mock(Logger.class);
        client = mock(YoutubeAnalyticClient.class);
        generator = mock(ReportGenerator.class);
        saver = mock(ReportSaver.class);

        youtubeReportService = new YoutubeReportService(logger, client, properties, generator, saver);
    }

    @AfterEach
    public void clear() {
        Mockito.reset(logger);
        Mockito.reset(client);
        Mockito.reset(generator);
        Mockito.reset(saver);
    }

    @Test
    void loadDeadYoutubeAnalyticServiceTest() {
        Mockito.when(client.checkYoutubeAnalyticService())
                .thenReturn(false);

        youtubeReportService.loadYoutubeAnalyticService();
        Mockito.verify(logger, times(1))
                .info(Logs.UNAVAILABLE.getMessage());
    }

    @Test
    void loadAliveYoutubeAnalyticServiceTest() throws IOException {
        String anyId = "any_id", anyReport = "any_report";

        Mockito.when(client.checkYoutubeAnalyticService())
                .thenReturn(true);
        Mockito.when(client.getYoutubeChannelDto(anyId))
                .thenReturn(Entities.getChannel());
        Mockito.when(client.getYoutubeVideoDtoArray(anyId))
                .thenReturn(Entities.getVideos());
        Mockito.when(generator.generateReport(Entities.getChannel(), Entities.getVideos()))
                .thenReturn(anyReport);

        youtubeReportService.loadYoutubeAnalyticService();
        Mockito.verify(saver, times(1))
                .save(anyReport, anyId);
        Mockito.verify(logger, times(1))
                .info(Logs.CREATED_REPORT.getMessage(anyId));

    }

    @Test
    void loadDisconnectedYoutubeAnalyticServiceTest() {
        String anyId = "any_id";
        RestClientException restClientException = new RestClientException("Server disconnected.");

        Mockito.when(client.checkYoutubeAnalyticService())
                .thenReturn(true);
        Mockito.when(client.getYoutubeVideoDtoArray(anyId))
                .thenThrow(restClientException);

        youtubeReportService.loadYoutubeAnalyticService();
        Mockito.verify(logger, times(1))
                .log(Level.WARNING, Logs.MICROSERVICE_DISCONNECTED.getMessage(), restClientException);
    }

    @Test
    void loadYoutubeAnalyticServiceWithSavingExceptionTest() throws IOException {
        String anyId = "any_id", anyReport = "any_id";
        IOException savingException = new IOException("Saving exception.");

        Mockito.when(client.checkYoutubeAnalyticService())
                .thenReturn(true);
        Mockito.when(client.getYoutubeChannelDto(anyId))
                .thenReturn(Entities.getChannel());
        Mockito.when(client.getYoutubeVideoDtoArray(anyId))
                .thenReturn(Entities.getVideos());
        Mockito.when(generator.generateReport(Entities.getChannel(), Entities.getVideos()))
                .thenReturn(anyReport);
        Mockito.doThrow(savingException)
                .when(saver).save(anyReport, anyId);

        youtubeReportService.loadYoutubeAnalyticService();
        Mockito.verify(logger, times(1))
                .log(Level.WARNING, Logs.REPORT_SAVING_EXCEPTION.getMessage(), savingException);
    }
}