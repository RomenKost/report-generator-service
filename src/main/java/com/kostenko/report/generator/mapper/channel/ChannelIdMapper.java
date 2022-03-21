package com.kostenko.report.generator.mapper.channel;

import com.kostenko.report.generator.model.channel.ChannelId;
import com.kostenko.report.generator.dto.channel.ChannelIdDto;
import com.kostenko.report.generator.entity.ChannelIdEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChannelIdMapper {
    ChannelIdEntity channelIdToChannelIdEntity(ChannelId channelId);

    List<ChannelIdEntity> channelIdsToChannelIdEntities(List<ChannelId> channelIds);

    ChannelId channelIdEntityToChannelId(ChannelIdEntity channelIdEntity);

    List<ChannelId> channelIdEntitiesToChannelIds(List<ChannelIdEntity> channelIdEntities);

    ChannelIdDto channelIdToChannelIdDTO(ChannelId channelId);

    List<ChannelIdDto> channelIdsToChannelIdDTOs(List<ChannelId> channelIds);

    ChannelId channelIdDtoToChannelId(ChannelIdDto channelIdDto);

    List<ChannelId> channelIdDTOsToChannelIds(List<ChannelIdDto> channelIdDTOs);
}
