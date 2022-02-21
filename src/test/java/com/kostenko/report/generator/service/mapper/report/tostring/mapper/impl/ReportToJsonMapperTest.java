package com.kostenko.report.generator.service.mapper.report.tostring.mapper.impl;

import com.kostenko.report.generator.service.model.Models;
import com.kostenko.report.generator.service.model.Report;
import com.kostenko.report.generator.service.model.Video;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ReportToJsonMapperTest {
    private static final String REPORT_WITHOUT_VIDEOS = "{  " +
            "\"country\" : \"any country\",  " +
            "\"description\" : \"any description\",  " +
            "\"videos\" : [ ],  " +
            "\"id\" : \"any id\",  " +
            "\"published_at\" : 1600000000000,  " +
            "\"title\" : \"any title\"" +
            "}";
    private static final String REPORT_WITH_VIDEOS = "{  " +
            "\"country\" : \"any country\",  " +
            "\"description\" : \"any description\",  " +
            "\"videos\" : [ {" +
            "    \"description\" : \"any description\"," +
            "    \"id\" : \"any video id\"," +
            "    \"title\" : \"any title\"," +
            "    \"published_at\" : 1600000000000" +
            "  }, {" +
            "    \"description\" : \"another description\"," +
            "    \"id\" : \"another video id\"," +
            "    \"title\" : \"another title\"," +
            "    \"published_at\" : 1700000000000" +
            "  } ],  " +
            "\"id\" : \"any id\",  " +
            "\"published_at\" : 1600000000000,  " +
            "\"title\" : \"any title\"" +
            "}";

    private final ReportToJsonMapper reportToJsonMapper = Mappers.getMapper(ReportToJsonMapper.class);

    @Test
    void generateReportTest() {
        String actual = reportToJsonMapper.reportToString(Models.getReport("any_id"))
                .lines()
                .collect(Collectors.joining());
        assertEquals(REPORT_WITH_VIDEOS, actual);
    }

    @Test
    void generateReportWithoutVideosTest() {
        String actual = reportToJsonMapper.reportToString(new Report("any_id", Models.getChannel(), new Video[0]))
                .lines()
                .collect(Collectors.joining());
        assertEquals(REPORT_WITHOUT_VIDEOS, actual);
    }

    @Test
    void generateNullReportTest() {
        assertNull(reportToJsonMapper.reportToString(null));
    }

    @Test
    void generateReportWithNullVideosTest() {
        String actual = reportToJsonMapper.reportToString(new Report("any_id", Models.getChannel(), null))
                .lines()
                .collect(Collectors.joining());
        assertEquals(REPORT_WITHOUT_VIDEOS, actual);
    }

    @Test
    void generateReportWithoutChannelTest() {
        assertNull(reportToJsonMapper.reportToString(new Report("any_id", null, Models.getVideos())));
    }
}
