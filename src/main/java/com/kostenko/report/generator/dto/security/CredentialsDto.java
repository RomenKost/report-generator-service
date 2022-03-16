package com.kostenko.report.generator.dto.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CredentialsDto {
    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;
}
