package com.kostenko.report.generator.dto.channel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
@EqualsAndHashCode
public class ChannelIdDto {
    @JsonProperty("channel_id")
    private final String id;
}
