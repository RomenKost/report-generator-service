package com.kostenko.report.generator.service.channel;

import com.kostenko.report.generator.model.channel.ChannelId;

import java.util.List;

public interface ChannelIdProcessor {
    List<ChannelId> getChannelsIds();

    ChannelId addChannelId(ChannelId channelId);

    void deleteChannelsId(ChannelId channelId);

    List<ChannelId> addAllChannelIds(List<ChannelId> channelIds);

    void deleteAllChannelId(List<ChannelId> channelIds);
}
