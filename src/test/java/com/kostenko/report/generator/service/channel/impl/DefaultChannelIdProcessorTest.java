package com.kostenko.report.generator.service.channel.impl;

import com.kostenko.report.generator.entity.ChannelIdEntity;
import com.kostenko.report.generator.entity.Entities;
import com.kostenko.report.generator.mapper.channel.ChannelIdMapper;
import com.kostenko.report.generator.model.Models;
import com.kostenko.report.generator.model.channel.ChannelId;
import com.kostenko.report.generator.repository.ChannelRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = DefaultChannelIdProcessor.class)
class DefaultChannelIdProcessorTest {
    @MockBean
    private ChannelRepository channelRepository;
    @MockBean
    private ChannelIdMapper channelIdMapper;

    @Autowired
    private DefaultChannelIdProcessor channelIdProcessor;

    @AfterEach
    public void clear() {
        reset(channelRepository, channelIdMapper);
    }

    @Test
    void getChannelsIdsTest() {
        List<ChannelId> expected = Models.getChannelIds();
        List<ChannelIdEntity> channelIdEntities = Entities.getChannelIds();
        when(channelRepository.findAll())
                .thenReturn(channelIdEntities);
        when(channelIdMapper.channelIdEntitiesToChannelIds(channelIdEntities))
                .thenReturn(expected);

        List<ChannelId> actual = channelIdProcessor.getChannelsIds();

        assertEquals(expected, actual);
    }

    @Test
    void addChannelIdTest() {
        ChannelId expected = Models.getChannelId();
        ChannelIdEntity channelIdEntity = Entities.getChannelId();
        when(channelIdMapper.channelIdToChannelIdEntity(expected))
                .thenReturn(channelIdEntity);
        when(channelRepository.save(channelIdEntity))
                .thenReturn(channelIdEntity);
        when(channelIdMapper.channelIdEntityToChannelId(channelIdEntity))
                .thenReturn(expected);

        ChannelId actual = channelIdProcessor.addChannelId(expected);

        verify(channelRepository, times(1))
                .save(channelIdEntity);
        assertEquals(expected, actual);
    }

    @Test
    void deleteChannelsIdTest() {
        ChannelId channelId = Models.getChannelId();
        ChannelIdEntity channelIdEntity = Entities.getChannelId();
        when(channelIdMapper.channelIdToChannelIdEntity(channelId))
                .thenReturn(channelIdEntity);

        channelIdProcessor.deleteChannelsId(channelId);

        verify(channelRepository, times(1))
                .delete(channelIdEntity);
    }

    @Test
    void addAllChannelIdsTest() {
        List<ChannelId> expected = Models.getChannelIds();
        List<ChannelIdEntity> channelIdEntities = Entities.getChannelIds();
        when(channelIdMapper.channelIdsToChannelIdEntities(expected))
                .thenReturn(channelIdEntities);
        when(channelRepository.saveAll(channelIdEntities))
                .thenReturn(channelIdEntities);
        when(channelIdMapper.channelIdEntitiesToChannelIds(channelIdEntities))
                .thenReturn(expected);

        List<ChannelId> actual = channelIdProcessor.addAllChannelIds(expected);

        verify(channelRepository, times(1))
                .saveAll(channelIdEntities);
        assertEquals(expected, actual);
    }

    @Test
    void deleteAllChannelIdTest() {
        List<ChannelId> channelIds = Models.getChannelIds();
        List<ChannelIdEntity> channelIdEntities = Entities.getChannelIds();
        when(channelIdMapper.channelIdsToChannelIdEntities(channelIds))
                .thenReturn(channelIdEntities);

        channelIdProcessor.deleteAllChannelId(channelIds);

        verify(channelRepository, times(1))
                .deleteAll(channelIdEntities);
    }
}
