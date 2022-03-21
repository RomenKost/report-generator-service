package com.kostenko.report.generator.service.channel.impl;

import com.kostenko.report.generator.mapper.channel.ChannelIdMapper;
import com.kostenko.report.generator.model.channel.ChannelId;
import com.kostenko.report.generator.service.channel.ChannelIdProcessor;
import com.kostenko.report.generator.entity.ChannelIdEntity;
import com.kostenko.report.generator.repository.ChannelRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class DefaultChannelIdProcessor implements ChannelIdProcessor {
    private final ChannelRepository channelRepository;
    private final ChannelIdMapper channelIdMapper;

    @Override
    public List<ChannelId> getChannelsIds() {
        log.info("Loading channel ids...");
        List<ChannelId> channelIds = channelIdMapper.channelIdEntitiesToChannelIds(channelRepository.findAll());
        log.info(channelIds.size() + " channel ids was loaded.");
        return channelIds;
    }

    @Override
    public ChannelId addChannelId(ChannelId channelId) {
        log.info("Saving channel id = " + channelId.getId() + "...");
        ChannelIdEntity entity = channelIdMapper.channelIdToChannelIdEntity(channelId);
        ChannelId savedChannelId = channelIdMapper.channelIdEntityToChannelId(channelRepository.save(entity));
        log.info("Channel id = " + channelId.getId() + " was saved.");
        return savedChannelId;
    }

    @Override
    public void deleteChannelsId(ChannelId channelId) {
        log.info("Deleting channel id = " + channelId.getId() + "...");
        ChannelIdEntity entity = channelIdMapper.channelIdToChannelIdEntity(channelId);
        channelRepository.delete(entity);
        log.info("Channel id = " + channelId.getId() + " was deleted");
    }

    @Override
    public List<ChannelId> addAllChannelIds(List<ChannelId> channelIds) {
        log.info("Saving channel ids (count = " + channelIds.size() + ")...");
        List<ChannelIdEntity> entities = channelIdMapper.channelIdsToChannelIdEntities(channelIds);
        List<ChannelId> savedChannelIds = channelIdMapper.channelIdEntitiesToChannelIds(channelRepository.saveAll(entities));
        log.info(savedChannelIds.size() + " channel ids was saved.");
        return savedChannelIds;
    }

    @Override
    public void deleteAllChannelId(List<ChannelId> channelIds) {
        log.info("Deleting channel ids (count = " + channelIds.size() + ")...");
        List<ChannelIdEntity> entities = channelIdMapper.channelIdsToChannelIdEntities(channelIds);
        channelRepository.deleteAll(entities);
        log.info(channelIds.size() + " channel ids was deleted.");
    }
}
