package com.kostenko.report.generator.mapper.report;

import com.kostenko.report.generator.dto.report.ReportDto;
import com.kostenko.report.generator.model.Models;
import com.kostenko.report.generator.model.analytic.Video;
import com.kostenko.report.generator.model.report.Report;
import com.kostenko.report.generator.dto.DTOs;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(classes = ReportMapperTest.class)
@ComponentScan(basePackageClasses = ReportMapper.class)
class ReportMapperTest {
    @Autowired
    private ReportMapper reportMapper;

    @Test
    void reportToReportDtoTest() {
        Report report =  Models.getReport();
        ReportDto expected = DTOs.getReport();

        ReportDto actual = reportMapper.reportToReportDto(report);

        assertEquals(expected, actual);
    }

    @Test
    void reportToReportDtoWhenReportIsNullReturnNullTest() {
        assertNull(reportMapper.reportToReportDto(null));
    }

    @Test
    void videosToVideoDTOsTest() {
        List<Video> videos = Models.getVideos();
        List<ReportDto.VideoDto> expected = DTOs.getVideoReportDTOs();

        List<ReportDto.VideoDto> actual = reportMapper.videosToVideoDTOs(videos);

        assertEquals(expected, actual);
    }

    @Test
    void videosToVideoDTOsWhenVideosIsNullReturnNullTest() {
        assertNull(reportMapper.videosToVideoDTOs(null));
    }
}
