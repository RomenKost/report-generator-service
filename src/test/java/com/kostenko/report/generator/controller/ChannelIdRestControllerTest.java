package com.kostenko.report.generator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kostenko.report.generator.dto.DTOs;
import com.kostenko.report.generator.dto.channel.ChannelIdDto;
import com.kostenko.report.generator.mapper.channel.ChannelIdMapper;
import com.kostenko.report.generator.model.Models;
import com.kostenko.report.generator.model.channel.ChannelId;
import com.kostenko.report.generator.service.channel.ChannelIdProcessor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ChannelIdRestController.class)
class ChannelIdRestControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private ChannelIdProcessor channelIdProcessor;
    @MockBean
    private ChannelIdMapper channelIdMapper;

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    public void clear() {
        reset(channelIdMapper, channelIdProcessor);
    }

    @Test
    void getChannelsIdsTest() throws Exception {
        List<ChannelId> channelIds = Models.getChannelIds();
        List<ChannelIdDto> expected = DTOs.getChannelIds();
        when(channelIdProcessor.getChannelsIds())
                .thenReturn(channelIds);
        when(channelIdMapper.channelIdsToChannelIdDTOs(channelIds))
                .thenReturn(expected);

        String response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/report/generator/v1/channels")
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<ChannelIdDto> actual = Arrays.stream(objectMapper.readValue(response, ChannelIdDto[].class))
                .collect(Collectors.toList());

        assertEquals(expected, actual);
    }

    @Test
    void addChannelTest() throws Exception {
        ChannelId channelId = Models.getChannelId();
        ChannelIdDto expected = DTOs.getChannelId();
        when(channelIdProcessor.addChannelId(channelId))
                .thenReturn(channelId);
        when(channelIdMapper.channelIdToChannelIdDTO(channelId))
                .thenReturn(expected);

        String response = mockMvc.perform(
                        MockMvcRequestBuilders.post("/report/generator/v1/channels/any id")
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        ChannelIdDto actual = objectMapper.readValue(response, ChannelIdDto.class);

        verify(channelIdProcessor, times(1))
                .addChannelId(channelId);
        assertEquals(expected, actual);
    }

    @Test
    void addChannelsTest() throws Exception {
        List<ChannelIdDto> expected = DTOs.getChannelIds();
        List<ChannelId> channelIds = Models.getChannelIds();
        when(channelIdMapper.channelIdDTOsToChannelIds(expected))
                .thenReturn(channelIds);
        when(channelIdProcessor.addAllChannelIds(channelIds))
                .thenReturn(channelIds);
        when(channelIdMapper.channelIdsToChannelIdDTOs(channelIds))
                .thenReturn(expected);

        String request = objectMapper.writeValueAsString(expected);
        String response = mockMvc.perform(
                        MockMvcRequestBuilders.post("/report/generator/v1/channels")
                                .content(request)
                                .contentType(APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<ChannelIdDto> actual = Arrays.stream(objectMapper.readValue(response, ChannelIdDto[].class))
                .collect(Collectors.toList());

        verify(channelIdProcessor, times(1))
                .addAllChannelIds(channelIds);
        assertEquals(expected, actual);
    }

    @Test
    void deleteChannelTest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/report/generator/v1/channels/any id")
        ).andExpect(status().isOk());

        verify(channelIdProcessor, times(1))
                .deleteChannelsId(Models.getChannelId());
    }

    @Test
    void deleteChannelsTest() throws Exception {
        List<ChannelId> channelIds = Models.getChannelIds();
        List<ChannelIdDto> channelIdDtoList = DTOs.getChannelIds();
        when(channelIdMapper.channelIdDTOsToChannelIds(channelIdDtoList))
                .thenReturn(channelIds);

        String request = objectMapper.writeValueAsString(channelIdDtoList);
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/report/generator/v1/channels")
                        .content(request)
                        .contentType(APPLICATION_JSON)
        ).andExpect(status().isOk());

        verify(channelIdProcessor, times(1))
                .deleteAllChannelId(channelIds);
    }
}
