package com.kostenko.report.generator.dto;

import com.kostenko.report.generator.dto.analytic.ChannelDto;
import com.kostenko.report.generator.dto.analytic.VideoDto;
import com.kostenko.report.generator.dto.channel.ChannelIdDto;
import com.kostenko.report.generator.dto.report.ReportDto;
import lombok.experimental.UtilityClass;

import java.util.Date;
import java.util.List;

@UtilityClass
public class DTOs {
    public VideoDto[] getVideos() {
        VideoDto firstExpectedVideo = new VideoDto(
                "any video id",
                "any title",
                "any description",
                new Date(1_600_000_000_000L)
        );
        VideoDto secondExpectedVideo = new VideoDto(
                "another video id",
                "another title",
                "another description",
                new Date(1_700_000_000_000L)
        );
        return new VideoDto[]{firstExpectedVideo, secondExpectedVideo};
    }

    public ChannelDto getChannel() {
        return new ChannelDto(
                "any id",
                "any title",
                "any description",
                "any country",
                new Date(1_600_000_000_000L)
        );
    }

    public ChannelIdDto getChannelId() {
        return ChannelIdDto
                .builder()
                .id("any id")
                .build();
    }

    public List<ChannelIdDto> getChannelIds() {
        ChannelIdDto firstChannelIdDto = getChannelId();
        ChannelIdDto secondChannelIdDto = ChannelIdDto
                .builder()
                .id("another id")
                .build();
        return List.of(firstChannelIdDto, secondChannelIdDto);
    }

    public ReportDto getReport() {
        List<ReportDto.VideoDto> videoDTOs = getVideoReportDTOs();
        return ReportDto
                .builder()
                .channelId("any id")
                .country("any country")
                .description("any description")
                .publishedAt(new Date(1_600_000_000_000L))
                .title("any title")
                .videos(videoDTOs)
                .build();
    }

    public List<ReportDto.VideoDto> getVideoReportDTOs() {
        ReportDto.VideoDto firstVideoDto = ReportDto.VideoDto
                .builder()
                .videoId("any video id")
                .description("any description")
                .publishedAt(new Date(1_600_000_000_000L))
                .title("any title")
                .build();
        ReportDto.VideoDto secondVideoDto = ReportDto.VideoDto
                .builder()
                .videoId("another video id")
                .description("another description")
                .publishedAt(new Date(1_700_000_000_000L))
                .title("another title")
                .build();
        return List.of(firstVideoDto, secondVideoDto);
    }
}
