package com.kostenko.reportgeneratorservice.model;

import lombok.experimental.UtilityClass;

import java.util.Date;

@UtilityClass
public class Models {
    public Video[] getVideos() {
        Video firstExpectedVideo = new Video();

        firstExpectedVideo.setVideoId("any video id");
        firstExpectedVideo.setDescription("any description");
        firstExpectedVideo.setTitle("any title");
        firstExpectedVideo.setPublishedAt(new Date(1_600_000_000_000L));

        Video secondExpectedVideo = new Video();

        secondExpectedVideo.setVideoId("another video id");
        secondExpectedVideo.setTitle("another title");
        secondExpectedVideo.setDescription("another description");
        secondExpectedVideo.setPublishedAt(new Date(1_700_000_000_000L));

        return new Video[]{firstExpectedVideo, secondExpectedVideo};
    }

    public Channel getChannel() {
        Channel channel = new Channel();

        channel.setChannelId("any id");
        channel.setCountry("any country");
        channel.setDescription("any description");
        channel.setTitle("any title");
        channel.setPublishedAt(new Date(1_600_000_000_000L));

        return channel;
    }

    public Report getReport(String id) {
        return new Report(id, getChannel(), getVideos());
    }
}
