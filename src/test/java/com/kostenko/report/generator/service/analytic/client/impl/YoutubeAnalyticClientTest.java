package com.kostenko.report.generator.service.analytic.client.impl;

import com.kostenko.report.generator.dto.DTOs;
import com.kostenko.report.generator.dto.analytic.ChannelDto;
import com.kostenko.report.generator.dto.analytic.VideoDto;
import com.kostenko.report.generator.dto.report.ReportDto;
import com.kostenko.report.generator.mapper.analytic.ChannelMapper;
import com.kostenko.report.generator.mapper.analytic.VideoMapper;
import com.kostenko.report.generator.model.Models;
import com.kostenko.report.generator.model.analytic.Channel;
import com.kostenko.report.generator.model.analytic.Video;
import com.kostenko.report.generator.property.analytic.YoutubeAnalyticUrlsProperties;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = YoutubeAnalyticClient.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class YoutubeAnalyticClientTest {
    @MockBean
    private RestTemplate restTemplate;
    @MockBean
    private ChannelMapper channelMapper;
    @MockBean
    private VideoMapper videoMapper;
    @MockBean
    private YoutubeAnalyticUrlsProperties urlsProperties;

    private YoutubeAnalyticClient client;

    @BeforeAll
    public void initialize() {
        client = spy(new YoutubeAnalyticClient(
                restTemplate,
                channelMapper,
                videoMapper,
                urlsProperties
        ));
    }

    @AfterAll
    public void clear() {
        reset(restTemplate, channelMapper, videoMapper, urlsProperties, client);
    }

    @Test
    void isAliveTrueTest() {
        assertTrue(client.isAlive());
    }

    @Test
    void isAliveFalseTest() {
        when(urlsProperties.getHealthURL())
                .thenReturn("any health url");
        when(restTemplate.getForEntity("any health url", String.class))
                .thenThrow(new RestClientException("any text"));
        assertFalse(client.isAlive());
    }

    @Test
    void getChannelTest() {
        Channel expected = Models.getChannel();
        ChannelDto channelDto = DTOs.getChannel();
        when(urlsProperties.getChannelsURL())
                .thenReturn("get_channel/%s");
        doReturn(Optional.of(channelDto))
                .when(client)
                .getForObject("get_channel/any id", ChannelDto.class, "any token");
        when(channelMapper.channelDtoToChannel(channelDto))
                .thenReturn(expected);

        Optional<Channel> actual = client.getChannel("any id", "any token");

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void getVideosTest() {
        List<Video> expected = Models.getVideos();
        VideoDto[] videoDTOs = DTOs.getVideos();
        when(urlsProperties.getVideosURL())
                .thenReturn("%s/get_videos");
        doReturn(Optional.of(videoDTOs))
                .when(client)
                .getForObject("any id/get_videos", VideoDto[].class, "any token");
        when(videoMapper.videoDtoArrayToVideoArray(videoDTOs))
                .thenReturn(expected.toArray(new Video[0]));

        List<Video> actual = client.getVideos("any id", "any token");

        assertEquals(expected, actual);
    }
}
