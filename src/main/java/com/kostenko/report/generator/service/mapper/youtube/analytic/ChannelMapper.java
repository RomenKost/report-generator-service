package com.kostenko.report.generator.service.mapper.youtube.analytic;

import com.kostenko.report.generator.service.dto.youtube.analytic.ChannelDto;
import com.kostenko.report.generator.service.model.Channel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChannelMapper {
    Channel channelDtoToChannel(ChannelDto channelDto);
}
