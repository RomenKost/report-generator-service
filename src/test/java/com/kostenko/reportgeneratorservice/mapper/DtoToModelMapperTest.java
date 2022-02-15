package com.kostenko.reportgeneratorservice.mapper;

import com.kostenko.reportgeneratorservice.dto.DTOs;
import com.kostenko.reportgeneratorservice.dto.VideoDto;
import com.kostenko.reportgeneratorservice.model.Channel;
import com.kostenko.reportgeneratorservice.model.Models;
import com.kostenko.reportgeneratorservice.model.Video;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class DtoToModelMapperTest {
    private final DtoToModelMapper dtoToModelMapper = Mappers.getMapper(DtoToModelMapper.class);

    @Test
    void channelDtoToChannelTest() {
        Channel expected = Models.getChannel();
        Channel actual = dtoToModelMapper.channelDtoToChannel(DTOs.getChannel());
        assertEquals(expected, actual);
    }

    @Test
    void nullToChannelTest() {
        assertNull(dtoToModelMapper.channelDtoToChannel(null));
    }

    @Test
    void videoDtoArrayToVideoArrayTest() {
        Video[] expected = Models.getVideos();
        Video[] actual = dtoToModelMapper.videoDtoArrayToVideoArray(DTOs.getVideos());
        assertArrayEquals(expected, actual);
    }

    @Test
    void emptyArrayToVideoArrayTest() {
        Video[] expected = new Video[0];
        Video[] actual = dtoToModelMapper.videoDtoArrayToVideoArray(new VideoDto[0]);
        assertArrayEquals(expected, actual);
    }


    @Test
    void nullToVideoArrayTest() {
        assertNull(dtoToModelMapper.videoDtoArrayToVideoArray(null));
    }
}
