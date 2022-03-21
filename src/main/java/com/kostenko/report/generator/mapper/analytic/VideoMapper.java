package com.kostenko.report.generator.mapper.analytic;

import com.kostenko.report.generator.dto.analytic.VideoDto;

import com.kostenko.report.generator.model.analytic.Video;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VideoMapper {
    Video[] videoDtoArrayToVideoArray(VideoDto[] videoDto);
}
