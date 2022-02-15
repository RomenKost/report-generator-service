package com.kostenko.reportgeneratorservice.mapper.reporttostringmapper.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kostenko.reportgeneratorservice.model.Channel;
import com.kostenko.reportgeneratorservice.model.Report;
import com.kostenko.reportgeneratorservice.model.Video;
import com.kostenko.reportgeneratorservice.mapper.reporttostringmapper.ReportToStringMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ReportToJsonMapper implements ReportToStringMapper {
    @Override
    @SuppressWarnings("unchecked")
    public String reportToString(Report report) {
        if (report == null || report.getChannel() == null) {
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        Channel channel = report.getChannel();

        jsonObject.put(JsonKeys.CHANNEL_ID.getKey(), channel.getChannelId());
        jsonObject.put(JsonKeys.CHANNEL_COUNTRY.getKey(), channel.getCountry());
        jsonObject.put(JsonKeys.CHANNEL_DESCRIPTION.getKey(), channel.getDescription());
        jsonObject.put(JsonKeys.CHANNEL_PUBLISHED_AT.getKey(), channel.getPublishedAt());
        jsonObject.put(JsonKeys.CHANNEL_TITLE.getKey(), channel.getTitle());

        JSONArray jsonArray = new JSONArray();
        if (report.getVideos() != null) {
            Arrays.stream(report.getVideos())
                    .map(this::videoToJsonObject)
                    .forEach(jsonArray::add);
        }

        jsonObject.put(JsonKeys.CHANNEL_VIDEOS.getKey(), jsonArray);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(jsonObject);
    }

    @SuppressWarnings("unchecked")
    private JSONObject videoToJsonObject(Video video) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(JsonKeys.VIDEO_ID.getKey(), video.getVideoId());
        jsonObject.put(JsonKeys.VIDEO_TITLE.getKey(), video.getTitle());
        jsonObject.put(JsonKeys.VIDEO_PUBLISHED_AT.getKey(), video.getPublishedAt());
        jsonObject.put(JsonKeys.VIDEO_DESCRIPTION.getKey(), video.getDescription());

        return jsonObject;
    }
}
