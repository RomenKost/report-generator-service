package com.kostenko.reportgeneratorservice.model;

import lombok.experimental.UtilityClass;

import java.util.Date;

@UtilityClass
public class Entities {
    public YoutubeVideoDto[] getVideos() {
        YoutubeVideoDto firstExpectedVideo = new YoutubeVideoDto();

        firstExpectedVideo.setVideoId("any video id");
        firstExpectedVideo.setDescription("any description");
        firstExpectedVideo.setTitle("any title");
        firstExpectedVideo.setPublishedAt(new Date(1_600_000_000_000L));

        YoutubeVideoDto secondExpectedVideo = new YoutubeVideoDto();

        secondExpectedVideo.setVideoId("another video id");
        secondExpectedVideo.setTitle("another title");
        secondExpectedVideo.setDescription("another description");
        secondExpectedVideo.setPublishedAt(new Date(1_700_000_000_000L));

        return new YoutubeVideoDto[]{firstExpectedVideo, secondExpectedVideo};
    }

    public YoutubeChannelDto getChannel() {
        YoutubeChannelDto channelDto = new YoutubeChannelDto();

        channelDto.setCountry("any country");
        channelDto.setDescription("any description");
        channelDto.setTitle("any title");
        channelDto.setPublishedAt(new Date(1_600_000_000_000L));

        return channelDto;
    }
}
