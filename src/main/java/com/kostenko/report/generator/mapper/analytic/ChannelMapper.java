package com.kostenko.report.generator.mapper.analytic;

import com.kostenko.report.generator.dto.analytic.ChannelDto;
import com.kostenko.report.generator.model.analytic.Channel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChannelMapper {
    Channel channelDtoToChannel(ChannelDto channelDto);
}
