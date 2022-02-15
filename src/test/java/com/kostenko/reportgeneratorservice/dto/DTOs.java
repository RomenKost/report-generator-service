package com.kostenko.reportgeneratorservice.dto;

import lombok.experimental.UtilityClass;

import java.util.Date;

@UtilityClass
public class DTOs {
    public VideoDto[] getVideos() {
        VideoDto firstExpectedVideo = new VideoDto();

        firstExpectedVideo.setVideoId("any video id");
        firstExpectedVideo.setDescription("any description");
        firstExpectedVideo.setTitle("any title");
        firstExpectedVideo.setPublishedAt(new Date(1_600_000_000_000L));

        VideoDto secondExpectedVideo = new VideoDto();

        secondExpectedVideo.setVideoId("another video id");
        secondExpectedVideo.setTitle("another title");
        secondExpectedVideo.setDescription("another description");
        secondExpectedVideo.setPublishedAt(new Date(1_700_000_000_000L));

        return new VideoDto[]{firstExpectedVideo, secondExpectedVideo};
    }

    public ChannelDto getChannel() {
        ChannelDto channelDto = new ChannelDto();

        channelDto.setChannelId("any id");
        channelDto.setCountry("any country");
        channelDto.setDescription("any description");
        channelDto.setTitle("any title");
        channelDto.setPublishedAt(new Date(1_600_000_000_000L));

        return channelDto;
    }
}
