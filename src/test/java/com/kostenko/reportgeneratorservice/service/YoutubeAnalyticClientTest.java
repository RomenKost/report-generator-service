package com.kostenko.reportgeneratorservice.service;


import com.kostenko.reportgeneratorservice.entity.URLParameters;
import com.kostenko.reportgeneratorservice.entity.URLs;
import com.kostenko.reportgeneratorservice.model.Entities;
import com.kostenko.reportgeneratorservice.model.YoutubeChannelDto;
import com.kostenko.reportgeneratorservice.model.YoutubeVideoDto;
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
    private YoutubeAnalyticClient client;

    private final String address = "any_address";
    private final String port = "any_port";

    @BeforeAll
    public void initialize() {
        restTemplate = mock(RestTemplate.class);
        client = new YoutubeAnalyticClient(restTemplate, address, port);
    }

    @Test
    void checkAliveYoutubeAnalyticServiceTest() {
        Mockito.when(restTemplate.getForObject(URLs.HEALTH.getUrl(), String.class, getAddressAndPortMap()))
                .thenReturn("everything");
        assertTrue(client.checkYoutubeAnalyticService());
    }

    @Test
    void checkDeadYoutubeAnalyticServiceTest() {
        Mockito.when(restTemplate.getForObject(URLs.HEALTH.getUrl(), String.class, getAddressAndPortMap()))
                .thenThrow(new RestClientException("any text"));
        assertFalse(client.checkYoutubeAnalyticService());
    }

    @Test
    void getYoutubeChannelDtoTest() {
        String anyId = "any_id";
        Mockito.when(restTemplate.getForObject(URLs.CHANNEL_STATISTIC.getUrl(), YoutubeChannelDto.class, getAddressPortAndId(anyId)))
                .thenReturn(Entities.getChannel());
        YoutubeChannelDto excepted = Entities.getChannel();
        YoutubeChannelDto actual = client.getYoutubeChannelDto(anyId);

        assertEquals(excepted, actual);
    }

    @Test
    void getYoutubeVideoDtoArray() {
        String anyId = "anyId";

        Mockito.when(restTemplate.getForObject(URLs.VIDEO_STATISTIC.getUrl(), YoutubeVideoDto[].class, getAddressPortAndId(anyId)))
                .thenReturn(Entities.getVideos());

        YoutubeVideoDto[] expected = Entities.getVideos();
        YoutubeVideoDto[] actual = client.getYoutubeVideoDtoArray(anyId);

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
