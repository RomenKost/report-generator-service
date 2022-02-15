package com.kostenko.reportgeneratorservice.mapper;

import com.kostenko.reportgeneratorservice.dto.ChannelDto;
import com.kostenko.reportgeneratorservice.dto.VideoDto;
import com.kostenko.reportgeneratorservice.model.Channel;

import com.kostenko.reportgeneratorservice.model.Video;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DtoToModelMapper {
    Channel channelDtoToChannel(ChannelDto channelDto);

    Video[] videoDtoArrayToVideoArray(VideoDto[] videoDto);
}
