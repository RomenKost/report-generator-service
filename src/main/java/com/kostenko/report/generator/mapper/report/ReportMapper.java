package com.kostenko.report.generator.mapper.report;

import com.kostenko.report.generator.dto.report.ReportDto;
import com.kostenko.report.generator.model.report.Report;
import com.kostenko.report.generator.model.analytic.Video;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportMapper {
    @Mapping(target = "channelId", expression = "java(report.getChannel().getChannelId())")
    @Mapping(target = "title", expression = "java(report.getChannel().getTitle())")
    @Mapping(target = "description", expression = "java(report.getChannel().getDescription())")
    @Mapping(target = "country", expression = "java(report.getChannel().getCountry())")
    @Mapping(target = "publishedAt", expression = "java(report.getChannel().getPublishedAt())")
    @Mapping(target = "videos", expression = "java(videosToVideoDTOs(report.getVideos()))")
    ReportDto reportToReportDto(Report report);

    List<ReportDto.VideoDto> videosToVideoDTOs(List<Video> videos);
}
