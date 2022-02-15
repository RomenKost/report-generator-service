package com.kostenko.reportgeneratorservice.service.client.impl;

import com.kostenko.reportgeneratorservice.entity.URLParameters;
import com.kostenko.reportgeneratorservice.entity.URLs;
import com.kostenko.reportgeneratorservice.dto.ChannelDto;
import com.kostenko.reportgeneratorservice.dto.VideoDto;
import com.kostenko.reportgeneratorservice.service.client.AnalyticClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class YoutubeAnalyticClient implements AnalyticClient {
    private final RestTemplate restTemplate;
    private final Map<String, String> addressAndPort = new HashMap<>();

    public YoutubeAnalyticClient(RestTemplate restTemplate,
                                 @Value("${youtube-analytic-service.address}") String address,
                                 @Value("${youtube-analytic-service.port}") String port) {
        this.restTemplate = restTemplate;
        addressAndPort.put(URLParameters.ADDRESS.getName(), address);
        addressAndPort.put(URLParameters.PORT.getName(), port);
    }

    @Override
    public ChannelDto getChannelDto(String id) {
        Map<String, String> parameters = new HashMap<>(addressAndPort);
        parameters.put(URLParameters.ID.getName(), id);
        return restTemplate.getForObject(
                URLs.CHANNEL_STATISTIC.getUrl(),
                ChannelDto.class, parameters
        );
    }

    @Override
    public VideoDto[] getVideoDtoArray(String id) {
        Map<String, String> parameters = new HashMap<>(addressAndPort);
        parameters.put(URLParameters.ID.getName(), id);
        return restTemplate.getForObject(URLs.VIDEO_STATISTIC.getUrl(), VideoDto[].class, parameters);
    }

    @Override
    public boolean isAlive() {
        try {
            restTemplate.getForObject(URLs.HEALTH.getUrl(), String.class, addressAndPort);
        } catch (RestClientException e) {
            return false;
        }
        return true;
    }
}
