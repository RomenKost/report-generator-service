package com.kostenko.report.generator.mapper.analytic;

import com.kostenko.report.generator.dto.DTOs;
import com.kostenko.report.generator.model.analytic.Channel;
import com.kostenko.report.generator.model.Models;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(classes = ChannelMapperTest.class)
@ComponentScan(basePackageClasses = ChannelMapper.class)
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
