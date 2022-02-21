package com.kostenko.report.generator.service.mapper.youtube.analytic;

import com.kostenko.report.generator.service.dto.youtube.analytic.DTOs;
import com.kostenko.report.generator.service.model.Channel;
import com.kostenko.report.generator.service.model.Models;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class ChannelMapperTest {
    @Autowired
    private ChannelMapper channelMapper;

    @Test
    void channelDtoToChannelTest() {
        Channel expected = Models.getChannel();
        Channel actual = channelMapper.channelDtoToChannel(DTOs.getChannel());
        assertEquals(expected, actual);
    }

    @Test
    void nullToChannelTest() {
        assertNull(channelMapper.channelDtoToChannel(null));
    }
}
