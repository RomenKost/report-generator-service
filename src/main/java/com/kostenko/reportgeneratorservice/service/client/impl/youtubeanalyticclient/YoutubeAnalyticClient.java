package com.kostenko.reportgeneratorservice.service.client.impl.youtubeanalyticclient;

import com.kostenko.reportgeneratorservice.dto.ChannelDto;
import com.kostenko.reportgeneratorservice.dto.VideoDto;
import com.kostenko.reportgeneratorservice.service.client.AnalyticClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class YoutubeAnalyticClient implements AnalyticClient {
    private final RestTemplate restTemplate;
    private final Map<String, String> addressAndPort = new HashMap<>();

    private final String youtubeAnalyticClientHealthUrl;
    private final String channelStatisticUrl;
    private final String videoStatisticUrl;

    public YoutubeAnalyticClient(RestTemplate restTemplate,
                                 @Value("${youtube-analytic-service.address}") String address,
                                 @Value("${youtube-analytic-service.port}") String port,

                                 @Value("${urls.health}") String youtubeAnalyticClientHealthUrl,
                                 @Value("${urls.channel-statistic}") String channelStatisticUrl,
                                 @Value("${urls.video-statistic}") String videoStatisticUrl) {
        this.restTemplate = restTemplate;

        this.youtubeAnalyticClientHealthUrl = youtubeAnalyticClientHealthUrl;
        this.channelStatisticUrl = channelStatisticUrl;
        this.videoStatisticUrl = videoStatisticUrl;

        addressAndPort.put(URLParameters.ADDRESS.getName(), address);
        addressAndPort.put(URLParameters.PORT.getName(), port);
    }

    @Override
    public ChannelDto getChannelDto(String id) {
        log.info("Collecting information about channel with id=" + id + "...");
        Map<String, String> parameters = new HashMap<>(addressAndPort);
        parameters.put(URLParameters.ID.getName(), id);
        ChannelDto channelDto = restTemplate.getForObject(channelStatisticUrl, ChannelDto.class, parameters);
        log.info("Information about channel with id=" + id + " was collected.");
        return channelDto;
    }

    @Override
    public VideoDto[] getVideoDtoArray(String id) {
        log.info("Collecting information about videos of channel with id=" + id + "...");
        Map<String, String> parameters = new HashMap<>(addressAndPort);
        parameters.put(URLParameters.ID.getName(), id);
        VideoDto[] videoDtoArray = restTemplate.getForObject(videoStatisticUrl, VideoDto[].class, parameters);
        log.info("Information about videos of channel with id=" + id + " were collected.");
        return videoDtoArray;
    }

    @Override
    public boolean isAlive() {
        try {
            restTemplate.getForObject(youtubeAnalyticClientHealthUrl, String.class, addressAndPort);
        } catch (RestClientException e) {
            return false;
        }
        return true;
    }
}
