package com.kostenko.reportgeneratorservice.service.reportgenerator;

import com.kostenko.reportgeneratorservice.model.YoutubeChannelDto;
import com.kostenko.reportgeneratorservice.model.YoutubeVideoDto;

public interface ReportGenerator {
    String generateReport(YoutubeChannelDto channelDto, YoutubeVideoDto[] videoDtoArray);
}
