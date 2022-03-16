package com.kostenko.report.generator.model;

import com.kostenko.report.generator.model.analytic.Video;
import com.kostenko.report.generator.model.channel.ChannelId;
import com.kostenko.report.generator.model.report.Report;
import com.kostenko.report.generator.model.analytic.Channel;
import lombok.experimental.UtilityClass;

import java.util.Date;
import java.util.List;

@UtilityClass
public class Models {
    public List<Video> getVideos() {
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

        return List.of(firstExpectedVideo, secondExpectedVideo);
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

    public Report getReport() {
        return new Report("any id", getChannel(), getVideos());
    }

    public ChannelId getChannelId() {
        ChannelId channelId = new ChannelId();
        channelId.setId("any id");
        return channelId;
    }

    public List<ChannelId> getChannelIds() {
        ChannelId firstChannelId = getChannelId();
        ChannelId secondChannelId = new ChannelId();
        secondChannelId.setId("another id");
        return List.of(firstChannelId, secondChannelId);
    }
}
