package com.kostenko.report.generator.mapper.analytic;

import com.kostenko.report.generator.dto.analytic.VideoDto;
import com.kostenko.report.generator.model.Models;
import com.kostenko.report.generator.dto.DTOs;
import com.kostenko.report.generator.model.analytic.Video;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = VideoMapperTest.class)
@ComponentScan(basePackageClasses = VideoMapper.class)
class VideoMapperTest {
    @Autowired
    private VideoMapper videoMapper;

    @Test
    void videoDtoArrayToVideoArrayTest() {
        Video[] expected = Models.getVideos().toArray(new Video[0]);
        Video[] actual = videoMapper.videoDtoArrayToVideoArray(DTOs.getVideos());
        assertArrayEquals(expected, actual);
    }

    @Test
    void emptyArrayToVideoArrayTest() {
        Video[] expected = new Video[0];
        Video[] actual = videoMapper.videoDtoArrayToVideoArray(new VideoDto[0]);
        assertArrayEquals(expected, actual);
    }


    @Test
    void nullToVideoArrayTest() {
        assertNull(videoMapper.videoDtoArrayToVideoArray(null));
    }
}
