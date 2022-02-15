package com.kostenko.reportgeneratorservice.service.reportgenerator.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kostenko.reportgeneratorservice.entity.ReportKeys;
import com.kostenko.reportgeneratorservice.model.YoutubeChannelDto;
import com.kostenko.reportgeneratorservice.model.YoutubeVideoDto;
import com.kostenko.reportgeneratorservice.service.reportgenerator.ReportGenerator;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class JSONReportGenerator implements ReportGenerator {
    @Override
    public String generateReport(YoutubeChannelDto youtubeChannelDto, YoutubeVideoDto[] youtubeVideoDtoArray) {
        Map<String, Object> map = dtoToMap(youtubeChannelDto, youtubeVideoDtoArray);
        return mapToJson(map);
    }

    private String mapToJson(Map<String, Object> map) {
        try {
            return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("This exception cannot be thrown, because HashMap is serializable.", e);
        }
    }

    private Map<String, Object> dtoToMap(YoutubeChannelDto youtubeChannelDto, YoutubeVideoDto[] youtubeVideoDtoArray) {
        Map<String, Object> map = new HashMap<>();

        map.put(ReportKeys.CHANNEL_TITLE.getName(), youtubeChannelDto.getTitle());
        map.put(ReportKeys.CHANNEL_DESCRIPTION.getName(), youtubeChannelDto.getDescription());
        map.put(ReportKeys.CHANNEL_PUBLISHED_AT.getName(), youtubeChannelDto.getPublishedAt());
        map.put(ReportKeys.CHANNEL_COUNTRY.getName(), youtubeChannelDto.getCountry());

        List<Map<String, Object>> videos =
                Arrays.stream(youtubeVideoDtoArray)
                        .map(this::videoToMap)
                        .collect(Collectors.toList());

        map.put(ReportKeys.CHANNEL_VIDEOS.getName(), videos);

        return map;
    }

    private Map<String, Object> videoToMap(YoutubeVideoDto videoDto) {
        Map<String, Object> map = new HashMap<>();

        map.put(ReportKeys.VIDEO_TITLE.getName(), videoDto.getTitle());
        map.put(ReportKeys.VIDEO_DESCRIPTION.getName(), videoDto.getDescription());
        map.put(ReportKeys.VIDEO_PUBLISHED_AT.getName(), videoDto.getPublishedAt());
        map.put(ReportKeys.VIDEO_ID.getName(), videoDto.getVideoId());

        return map;
    }
}
