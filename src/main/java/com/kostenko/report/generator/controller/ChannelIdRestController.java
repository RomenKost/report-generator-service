package com.kostenko.report.generator.controller;

import com.kostenko.report.generator.mapper.channel.ChannelIdMapper;
import com.kostenko.report.generator.model.channel.ChannelId;
import com.kostenko.report.generator.service.channel.ChannelIdProcessor;
import com.kostenko.report.generator.dto.channel.ChannelIdDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/report/generator/v1/channels")
public class ChannelIdRestController {
    private final ChannelIdProcessor channelIdProcessor;
    private final ChannelIdMapper channelIdMapper;

    @GetMapping
    public List<ChannelIdDto> getChannelsIds() {
        List<ChannelId> channelIds = channelIdProcessor.getChannelsIds();
        return channelIdMapper.channelIdsToChannelIdDTOs(channelIds);
    }

    @PostMapping("/{channelId}")
    public ChannelIdDto addChannel(@PathVariable("channelId") String channelId) {
        ChannelId channelIdModel = new ChannelId();
        channelIdModel.setId(channelId);
        ChannelId savedChannelId = channelIdProcessor.addChannelId(channelIdModel);
        return channelIdMapper.channelIdToChannelIdDTO(savedChannelId);
    }

    @PostMapping
    public List<ChannelIdDto> addChannels(@RequestBody List<ChannelIdDto> channelIdDtoList) {
        List<ChannelId> channelIds = channelIdMapper.channelIdDTOsToChannelIds(channelIdDtoList);
        List<ChannelId> savedChannelIds = channelIdProcessor.addAllChannelIds(channelIds);
        return channelIdMapper.channelIdsToChannelIdDTOs(savedChannelIds);
    }

    @DeleteMapping("/{channelId}")
    public void deleteChannel(@PathVariable("channelId") String channelId) {
        ChannelId channelIdModel = new ChannelId();
        channelIdModel.setId(channelId);
        channelIdProcessor.deleteChannelsId(channelIdModel);
    }

    @DeleteMapping
    public void deleteChannels(@RequestBody List<ChannelIdDto> channelIdDtoList) {
        List<ChannelId> channelIds = channelIdMapper.channelIdDTOsToChannelIds(channelIdDtoList);
        channelIdProcessor.deleteAllChannelId(channelIds);
    }
}
