package com.kostenko.report.generator.service.analytic.client.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kostenko.report.generator.exception.message.ErrorMessages;
import com.kostenko.report.generator.property.analytic.YoutubeAnalyticUrlsProperties;
import com.kostenko.report.generator.dto.analytic.ChannelDto;
import com.kostenko.report.generator.dto.analytic.VideoDto;
import com.kostenko.report.generator.exception.YoutubeAnalyticServiceUnavailableException;
import com.kostenko.report.generator.exception.YoutubeAnalyticTokenExpiredException;
import com.kostenko.report.generator.mapper.analytic.ChannelMapper;
import com.kostenko.report.generator.mapper.analytic.VideoMapper;
import com.kostenko.report.generator.model.analytic.Channel;
import com.kostenko.report.generator.model.analytic.Video;
import com.kostenko.report.generator.service.analytic.client.AnalyticClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

import static org.springframework.web.client.HttpClientErrorException.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpMethod.*;

@Slf4j
@Service
@AllArgsConstructor
public class YoutubeAnalyticClient implements AnalyticClient {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final RestTemplate restTemplate;

    private final ChannelMapper channelMapper;
    private final VideoMapper videoMapper;

    private final YoutubeAnalyticUrlsProperties urlsProperties;

    @Override
    public Optional<Channel> getChannel(String id, String token) {
        log.info("Collecting information about channel with id = " + id + "...");
        Optional<Channel> channel = getForObject(
                String.format(urlsProperties.getChannelsURL(), id),
                ChannelDto.class,
                token
        ).map(channelMapper::channelDtoToChannel);
        log.info("Information about channel with id = " + id + " was collected.");
        return channel;
    }

    @Override
    public List<Video> getVideos(String id, String token) {
        log.info("Collecting information about videos of channel with id = " + id + "...");
        List<Video> videos = getForObject(
                String.format(urlsProperties.getVideosURL(), id),
                VideoDto[].class,
                token
        ).map(videoMapper::videoDtoArrayToVideoArray)
                .map(Arrays::asList)
                .orElse(List.of());
        log.info("Information about videos of channel with id = " + id + " were collected.");
        return videos;
    }

    @Override
    public boolean isAlive() {
        try {
            log.info("Checking availability of youtube analytic service.");
            restTemplate.getForEntity(urlsProperties.getHealthURL(), String.class);
            log.info("Youtube analytic service is available.");
        } catch (RestClientException e) {
            log.error(ErrorMessages.YOUTUBE_ANALYTIC_SERVICE_UNAVAILABLE, e);
            return false;
        }
        return true;
    }

    <T> Optional<T> getForObject(String url, Class<T> responseType, String token) {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(AUTHORIZATION, token);
            ResponseEntity<String> response = restTemplate.exchange(url, GET, new HttpEntity<>(httpHeaders), String.class);
            return Optional.of(objectMapper.readValue(response.getBody(), responseType));
        } catch (IOException e) {
            log.error(ErrorMessages.UNSUPPORTED_RESPONSE_FORMAT, e);
            throw new YoutubeAnalyticServiceUnavailableException(ErrorMessages.UNSUPPORTED_RESPONSE_FORMAT, e);
        } catch (NotFound e) {
            log.error(ErrorMessages.CHANNEL_NOT_FOUND, e);
            return Optional.empty();
        } catch (Unauthorized e) {
            log.error(ErrorMessages.TOKEN_EXPIRED, e);
            throw new YoutubeAnalyticTokenExpiredException(token, e);
        } catch (RestClientException e) {
            log.error(ErrorMessages.YOUTUBE_ANALYTIC_SERVICE_UNAVAILABLE, e);
            throw new YoutubeAnalyticServiceUnavailableException(ErrorMessages.YOUTUBE_ANALYTIC_SERVICE_UNAVAILABLE, e);
        }
    }
}
