package com.kostenko.reportgeneratorservice.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Report {
    private String id;

    private Channel channel;
    private Video[] videos;
}
