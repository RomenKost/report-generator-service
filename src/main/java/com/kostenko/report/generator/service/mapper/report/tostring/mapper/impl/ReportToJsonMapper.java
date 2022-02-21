package com.kostenko.report.generator.service.mapper.report.tostring.mapper.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kostenko.report.generator.service.mapper.report.tostring.mapper.ReportToStringMapper;
import com.kostenko.report.generator.service.model.Channel;
import com.kostenko.report.generator.service.model.Report;
import com.kostenko.report.generator.service.model.Video;
import org.mapstruct.Mapper;

import java.util.*;

@Mapper(componentModel = "spring")
public abstract class ReportToJsonMapper implements ReportToStringMapper {
    private final ObjectMapper jsonMapper = new ObjectMapper();

    @Override
    public String reportToString(Report report) {
        if (report == null || report.getChannel() == null) {
            return null;
        }
        Map<String, Object> reportMap = new HashMap<>();
        Channel channel = report.getChannel();

        reportMap.put(JsonKeys.CHANNEL_ID.getKey(), channel.getChannelId());
        reportMap.put(JsonKeys.CHANNEL_COUNTRY.getKey(), channel.getCountry());
        reportMap.put(JsonKeys.CHANNEL_DESCRIPTION.getKey(), channel.getDescription());
        reportMap.put(JsonKeys.CHANNEL_PUBLISHED_AT.getKey(), channel.getPublishedAt());
        reportMap.put(JsonKeys.CHANNEL_TITLE.getKey(), channel.getTitle());

        List<Map<String, Object>> jsonArray = new ArrayList<>();
        if (report.getVideos() != null) {
            Arrays.stream(report.getVideos())
                    .map(this::videoToJsonObject)
                    .forEach(jsonArray::add);
        }

        reportMap.put(JsonKeys.CHANNEL_VIDEOS.getKey(), jsonArray);
        try {
            return jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(reportMap);
        } catch (JsonProcessingException exception) {
            return null;
        }
    }

    private Map<String, Object> videoToJsonObject(Video video) {
        Map<String, Object> videoMap = new HashMap<>();

        videoMap.put(JsonKeys.VIDEO_ID.getKey(), video.getVideoId());
        videoMap.put(JsonKeys.VIDEO_TITLE.getKey(), video.getTitle());
        videoMap.put(JsonKeys.VIDEO_PUBLISHED_AT.getKey(), video.getPublishedAt());
        videoMap.put(JsonKeys.VIDEO_DESCRIPTION.getKey(), video.getDescription());

        return videoMap;
    }
}
