package com.kostenko.reportgeneratorservice.service.reportgenerator;

import com.kostenko.reportgeneratorservice.model.Entities;
import com.kostenko.reportgeneratorservice.model.YoutubeVideoDto;
import com.kostenko.reportgeneratorservice.service.reportgenerator.impl.JSONReportGenerator;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class JSONReportGeneratorTest {
    private final JSONReportGenerator generator = new JSONReportGenerator();

    @Test
    void generateReportTest() {
        String expected = "{" +
                "  \"channelDescription\" : \"any description\"," +
                "  \"channelCountry\" : \"any country\"," +
                "  \"channelPublishedAt\" : 1600000000000," +
                "  \"videos\" : [ {" +
                "    \"publishedAt\" : 1600000000000," +
                "    \"description\" : \"any description\"," +
                "    \"id\" : \"any video id\"," +
                "    \"title\" : \"any title\"" +
                "  }, {" +
                "    \"publishedAt\" : 1700000000000," +
                "    \"description\" : \"another description\"," +
                "    \"id\" : \"another video id\"," +
                "    \"title\" : \"another title\"" +
                "  } ]," +
                "  \"channelTitle\" : \"any title\"" +
                "}";
        String actual = generator.generateReport(Entities.getChannel(), Entities.getVideos())
                .lines()
                .collect(Collectors.joining());

        assertEquals(expected, actual);
    }

    @Test
    void generateReportWithoutVideosTest() {
        String expected = "{" +
                "  \"channelDescription\" : \"any description\"," +
                "  \"channelCountry\" : \"any country\"," +
                "  \"channelPublishedAt\" : 1600000000000," +
                "  \"videos\" : [ ]," +
                "  \"channelTitle\" : \"any title\"" +
                "}";
        String actual = generator.generateReport(Entities.getChannel(), new YoutubeVideoDto[0])
                .lines()
                .collect(Collectors.joining());

        assertEquals(expected, actual);
    }
}
