package com.kostenko.report.generator.service.report.processor.impl;

import com.kostenko.report.generator.exception.YoutubeAnalyticServiceUnavailableException;
import com.kostenko.report.generator.exception.YoutubeAnalyticTokenExpiredException;
import com.kostenko.report.generator.model.Models;
import com.kostenko.report.generator.service.analytic.client.AnalyticClient;
import com.kostenko.report.generator.service.analytic.security.YoutubeAnalyticSecurityClient;
import com.kostenko.report.generator.service.channel.ChannelIdProcessor;
import com.kostenko.report.generator.service.report.saver.ReportSaver;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest(
        classes = YoutubeReportProcessorJob.class,
        properties = "report-generator.enabled = true"
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class YoutubeReportProcessorJobTest {
    @MockBean
    private ChannelIdProcessor channelIdProcessor;
    @MockBean
    private AnalyticClient analyticClient;
    @MockBean
    private YoutubeAnalyticSecurityClient securityClient;
    @MockBean
    private ReportSaver reportSaver;

    private YoutubeReportProcessorJob reportProcessor;

    @BeforeEach
    void initialize() {
        reportProcessor = spy(new YoutubeReportProcessorJob(
                channelIdProcessor,
                analyticClient,
                securityClient,
                reportSaver
        ));
    }

    @AfterEach
    void clear() {
        reset(reportProcessor, channelIdProcessor, analyticClient, securityClient, reportSaver);
        clearInvocations(reportProcessor, channelIdProcessor, analyticClient, securityClient, reportSaver);
        clearAllCaches();
    }

    @Test
    void processAnalyticServiceWhenAnalyticClientIsDeadThenDoNothingTest() {
        reportProcessor.processAnalyticService();

        verify(reportProcessor, times(0))
                .processId(any());
    }

    @Test
    void processAnalyticServiceWhenAnalyticClientIsAliveThenProcessIdsTest() {
        when(channelIdProcessor.getChannelsIds())
                .thenReturn(Models.getChannelIds());
        when(analyticClient.isAlive())
                .thenReturn(true);
        doNothing()
                .when(reportProcessor)
                .processId(any());

        reportProcessor.processAnalyticService();

        verify(reportProcessor, times(1))
                .processId("any id");
        verify(reportProcessor, times(1))
                .processId("another id");
    }

    @Test
    void processAnalyticServiceWhenAnalyticClientBecameUnavailableThenProcessIdsTest() {
        when(channelIdProcessor.getChannelsIds())
                .thenReturn(Models.getChannelIds());
        when(analyticClient.isAlive())
                .thenReturn(true);
        doThrow(new YoutubeAnalyticServiceUnavailableException("any message"))
                .when(reportProcessor)
                .processId("any id");

        reportProcessor.processAnalyticService();

        verify(reportProcessor, times(1))
                .processId("any id");
        verify(reportProcessor, times(0))
                .processId("another id");
    }

    @Test
    void processIdWhenChannelNotFoundThenDoNothingTest() {
        when(analyticClient.getChannel("any id", null))
                .thenReturn(Optional.empty());

        reportProcessor.processId("any id");

        verify(reportSaver, times(0))
                .save(any());
    }

    @Test
    void processIdWhenChannelFoundThenSaveReportTest() {
        when(analyticClient.getChannel("any id", null))
                .thenReturn(Optional.of(Models.getChannel()));
        when(analyticClient.getVideos("any id", null))
                .thenReturn(Models.getVideos());

        reportProcessor.processId("any id");

        verify(reportSaver, times(1))
                .save(Models.getReport());
    }

    @Test
    void processIdWhenTokenExpiredThenUpdateTokenTest() {
        when(analyticClient.getChannel("any id", null))
                .thenThrow(new YoutubeAnalyticTokenExpiredException("", new RestClientException("")));
        when(analyticClient.getChannel("any id", "any token"))
                .thenReturn(Optional.of(Models.getChannel()));
        when(analyticClient.getVideos("any id", "any token"))
                .thenReturn(Models.getVideos());
        when(securityClient.getToken())
                .thenReturn("any token");

        reportProcessor.processId("any id");

        verify(reportSaver, times(1))
                .save(Models.getReport());
    }
}