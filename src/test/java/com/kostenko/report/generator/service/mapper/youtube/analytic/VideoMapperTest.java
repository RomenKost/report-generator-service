package com.kostenko.report.generator.service.mapper.youtube.analytic;

import com.kostenko.report.generator.service.dto.youtube.analytic.VideoDto;
import com.kostenko.report.generator.service.model.Models;
import com.kostenko.report.generator.service.dto.youtube.analytic.DTOs;
import com.kostenko.report.generator.service.model.Channel;
import com.kostenko.report.generator.service.model.Video;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VideoMapperTest {
    @Autowired
    private VideoMapper videoMapper;

    @Test
    void videoDtoArrayToVideoArrayTest() {
        Video[] expected = Models.getVideos();
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
