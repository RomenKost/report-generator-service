package com.kostenko.report.generator.entity;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class Entities {
    public ChannelIdEntity getChannelId() {
        ChannelIdEntity channelIdEntity = new ChannelIdEntity();
        channelIdEntity.setId("any id");
        return channelIdEntity;
    }

    public List<ChannelIdEntity> getChannelIds() {
        ChannelIdEntity firstChannelIdEntity = getChannelId();
        ChannelIdEntity secondChannelIdEntity = new ChannelIdEntity();
        secondChannelIdEntity.setId("another id");
        return List.of(firstChannelIdEntity, secondChannelIdEntity);
    }
}
