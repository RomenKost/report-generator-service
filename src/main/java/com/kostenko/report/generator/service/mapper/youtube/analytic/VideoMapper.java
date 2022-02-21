package com.kostenko.report.generator.service.mapper.youtube.analytic;

import com.kostenko.report.generator.service.dto.youtube.analytic.VideoDto;

import com.kostenko.report.generator.service.model.Video;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VideoMapper {
    Video[] videoDtoArrayToVideoArray(VideoDto[] videoDto);
}
