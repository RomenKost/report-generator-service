package com.kostenko.reportgeneratorservice.service;

import com.kostenko.reportgeneratorservice.entity.URLParameters;
import com.kostenko.reportgeneratorservice.entity.URLs;
import com.kostenko.reportgeneratorservice.model.YoutubeChannelDto;
import com.kostenko.reportgeneratorservice.model.YoutubeVideoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class YoutubeAnalyticClient {
    private final RestTemplate restTemplate;
    private final Map<String, String> addressAndPort = new HashMap<>();

    public YoutubeAnalyticClient(@Autowired RestTemplate restTemplate,
                                 @Value("${youtube-analytic-service.address}") String address,
                                 @Value("${youtube-analytic-service.port}") String port) {
        this.restTemplate = restTemplate;
        addressAndPort.put(URLParameters.ADDRESS.getName(), address);
        addressAndPort.put(URLParameters.PORT.getName(), port);
    }

    public YoutubeChannelDto getYoutubeChannelDto(String id) {
        Map<String, String> parameters = new HashMap<>(addressAndPort);
        parameters.put(URLParameters.ID.getName(), id);
        return restTemplate.getForObject(
                URLs.CHANNEL_STATISTIC.getUrl(),
                YoutubeChannelDto.class, parameters
        );
    }

    public YoutubeVideoDto[] getYoutubeVideoDtoArray(String id) {
        Map<String, String> parameters = new HashMap<>(addressAndPort);
        parameters.put(URLParameters.ID.getName(), id);
        return restTemplate.getForObject(URLs.VIDEO_STATISTIC.getUrl(), YoutubeVideoDto[].class, parameters);
    }

    public boolean checkYoutubeAnalyticService() {
        try {
            restTemplate.getForObject(URLs.HEALTH.getUrl(), String.class, addressAndPort);
        } catch (RestClientException e) {
            return false;
        }
        return true;
    }
}
