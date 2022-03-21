package com.kostenko.report.generator.service.analytic.client.impl;

import com.kostenko.report.generator.dto.DTOs;
import com.kostenko.report.generator.dto.analytic.ChannelDto;
import com.kostenko.report.generator.dto.analytic.VideoDto;
import com.kostenko.report.generator.exception.YoutubeAnalyticServiceUnavailableException;
import com.kostenko.report.generator.exception.YoutubeAnalyticTokenExpiredException;
import com.kostenko.report.generator.mapper.analytic.ChannelMapper;
import com.kostenko.report.generator.mapper.analytic.VideoMapper;
import com.kostenko.report.generator.model.Models;
import com.kostenko.report.generator.model.analytic.Channel;
import com.kostenko.report.generator.model.analytic.Video;
import com.kostenko.report.generator.property.analytic.YoutubeAnalyticUrlsProperties;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static com.kostenko.report.generator.exception.message.ErrorMessages.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.*;

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

    @Test
    void getForObjectTest() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION, "any token");

        when(restTemplate.exchange("any url", GET, new HttpEntity<>(httpHeaders), String.class))
                .thenReturn(new ResponseEntity<>("any object", OK));

        Optional<String> actual = client.getForObject("any url", String.class, "any token");

        assertTrue(actual.isPresent());
        assertEquals("any object", actual.get());
    }

    @Test
    void getForObjectWhenWasThrownNotFoundExceptionReturnEmptyOptionalTest() {
        when(restTemplate.exchange((String) any(), any(), any(), (Class<?>) any()))
                .thenThrow(HttpClientErrorException.create(NOT_FOUND, "", new HttpHeaders(), null, null));

        Optional<String> actual = client.getForObject("any url", String.class, "any token");

        assertTrue(actual.isEmpty());
    }

    @Test
    void getForObjectWhenWasThrownUnauthorizedExceptionThrowYoutubeAnalyticTokenExpiredExceptionTest() {
        when(restTemplate.exchange((String) any(), any(), any(), (Class<?>) any()))
                .thenThrow(HttpClientErrorException.create(UNAUTHORIZED, "", new HttpHeaders(), null, null));

        YoutubeAnalyticTokenExpiredException exception = assertThrows(
                YoutubeAnalyticTokenExpiredException.class,
                () -> client.getForObject("any url", String.class, "any token")
        );

        assertEquals(TOKEN_EXPIRED, exception.getMessage());
    }

    @Test
    void getForObjectWhenWasThrownRestClientExceptionThrowYoutubeAnalyticServiceUnavailableExceptionTest() {
        when(restTemplate.exchange((String) any(), any(), any(), (Class<?>) any()))
                .thenThrow(new RestClientException(""));

        YoutubeAnalyticServiceUnavailableException exception = assertThrows(
                YoutubeAnalyticServiceUnavailableException.class,
                () -> client.getForObject("any url", String.class, "any token")
        );

        assertEquals(YOUTUBE_ANALYTIC_SERVICE_UNAVAILABLE, exception.getMessage());
    }
}
