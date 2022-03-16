package com.kostenko.report.generator.model.report;

import com.kostenko.report.generator.model.analytic.Video;
import com.kostenko.report.generator.model.analytic.Channel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Report {
    private String id;

    private Channel channel;
    private List<Video> videos;
}
