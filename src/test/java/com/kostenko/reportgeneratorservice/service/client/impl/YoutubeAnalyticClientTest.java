package com.kostenko.reportgeneratorservice.service.client.impl;


import com.kostenko.reportgeneratorservice.dto.DTOs;
import com.kostenko.reportgeneratorservice.dto.ChannelDto;
import com.kostenko.reportgeneratorservice.dto.VideoDto;
import com.kostenko.reportgeneratorservice.logger.LoggerChecker;
import com.kostenko.reportgeneratorservice.service.client.AnalyticClient;
import com.kostenko.reportgeneratorservice.service.client.impl.youtubeanalyticclient.URLParameters;
import com.kostenko.reportgeneratorservice.service.client.impl.youtubeanalyticclient.YoutubeAnalyticClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class YoutubeAnalyticClientTest {
    private RestTemplate restTemplate;
    private AnalyticClient analyticClient;

    private final String address = "any_address";
    private final String port = "any_port";

    private final String youtubeAnalyticClientHealthUrl = "Any_health_url.";
    private final String channelStatisticUrl = "Any channel statistic url.";
    private final String videoStatisticUrl = "Any video statistic url";

    @BeforeAll
    public void initialize() {
        restTemplate = mock(RestTemplate.class);
        analyticClient = new YoutubeAnalyticClient(
                restTemplate, address, port,
                youtubeAnalyticClientHealthUrl,
                channelStatisticUrl,
                videoStatisticUrl
        );
    }

    @Test
    void isAliveTrueTest() {
        Mockito.when(restTemplate.getForObject(youtubeAnalyticClientHealthUrl, String.class, getAddressAndPortMap()))
                .thenReturn("everything");
        assertTrue(analyticClient.isAlive());
    }

    @Test
    void isAliveFalseTest() {
        Mockito.when(restTemplate.getForObject(youtubeAnalyticClientHealthUrl, String.class, getAddressAndPortMap()))
                .thenThrow(new RestClientException("any text"));
        assertFalse(analyticClient.isAlive());
    }

    @Test
    void getYoutubeChannelDtoTest() {
        String anyId = "any_id";
        Mockito.when(restTemplate.getForObject(channelStatisticUrl, ChannelDto.class, getAddressPortAndId(anyId)))
                .thenReturn(DTOs.getChannel());
        ChannelDto excepted = DTOs.getChannel();
        ChannelDto actual = analyticClient.getChannelDto(anyId);

        assertEquals(excepted, actual);
    }

    @Test
    void getYoutubeVideoDtoArray() {
        String anyId = "anyId";

        Mockito.when(restTemplate.getForObject(videoStatisticUrl, VideoDto[].class, getAddressPortAndId(anyId)))
                .thenReturn(DTOs.getVideos());

        VideoDto[] expected = DTOs.getVideos();
        VideoDto[] actual = analyticClient.getVideoDtoArray(anyId);

        assertArrayEquals(expected, actual);
    }

    private Map<String, String> getAddressAndPortMap() {
        Map<String, String> parameters = new HashMap<>();

        parameters.put(URLParameters.ADDRESS.getName(), address);
        parameters.put(URLParameters.PORT.getName(), port);

        return parameters;
    }

    private Map<String, String> getAddressPortAndId(String id) {
        Map<String, String> parameters = getAddressAndPortMap();
        parameters.put(URLParameters.ID.getName(), id);
        return parameters;
    }
}
