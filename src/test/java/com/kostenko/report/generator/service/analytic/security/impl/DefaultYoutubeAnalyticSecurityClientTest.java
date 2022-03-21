package com.kostenko.report.generator.service.analytic.security.impl;

import com.kostenko.report.generator.dto.security.CredentialsDto;
import com.kostenko.report.generator.dto.security.TokenDto;
import com.kostenko.report.generator.exception.YoutubeAnalyticServiceUnavailableException;
import com.kostenko.report.generator.property.analytic.YoutubeAnalyticCredentialsProperties;
import com.kostenko.report.generator.property.analytic.YoutubeAnalyticUrlsProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static com.kostenko.report.generator.exception.message.ErrorMessages.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(classes = DefaultYoutubeAnalyticSecurityClient.class)
class DefaultYoutubeAnalyticSecurityClientTest {
    @MockBean
    private YoutubeAnalyticCredentialsProperties credentialsProperties;
    @MockBean
    private YoutubeAnalyticUrlsProperties urlsProperties;
    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private DefaultYoutubeAnalyticSecurityClient securityClient;

    @Test
    void getTokenTest() {
        CredentialsDto credentialsDto = new CredentialsDto("any username", "any password");
        when(credentialsProperties.getPassword())
                .thenReturn("any password");
        when(credentialsProperties.getUsername())
                .thenReturn("any username");
        when(urlsProperties.getLoginURL())
                .thenReturn("any login url");
        when(restTemplate.exchange("any login url", POST, new HttpEntity<>(credentialsDto), TokenDto.class))
                .thenReturn(new ResponseEntity<>(new TokenDto("any token"), OK));
        String expected = "any token";

        String actual = securityClient.getToken();

        assertEquals(expected, actual);
    }

    @Test
    void getTokenWhenThrownNotFoundThenThrowYoutubeAnalyticServiceUnavailableExceptionTest() {
        when(restTemplate.exchange((String) any(), any(), any(), (Class<?>) any()))
                .thenThrow(HttpClientErrorException.create(NOT_FOUND, "", new HttpHeaders(), null, null));

        YoutubeAnalyticServiceUnavailableException exception = assertThrows(
                YoutubeAnalyticServiceUnavailableException.class,
                () -> securityClient.getToken()
        );

        assertEquals(INVALID_USERNAME, exception.getMessage());
    }

    @Test
    void getTokenWhenThrownForbiddenThenThrowYoutubeAnalyticServiceUnavailableExceptionTest() {
        when(restTemplate.exchange((String) any(), any(), any(), (Class<?>) any()))
                .thenThrow(HttpClientErrorException.create(FORBIDDEN, "", new HttpHeaders(), null, null));

        YoutubeAnalyticServiceUnavailableException exception = assertThrows(
                YoutubeAnalyticServiceUnavailableException.class,
                () -> securityClient.getToken()
        );

        assertEquals(USER_DISABLED, exception.getMessage());
    }


    @Test
    void getTokenWhenThrownUnauthorizedThenThrowYoutubeAnalyticServiceUnavailableExceptionTest() {
        when(restTemplate.exchange((String) any(), any(), any(), (Class<?>) any()))
                .thenThrow(HttpClientErrorException.create(UNAUTHORIZED, "", new HttpHeaders(), null, null));

        YoutubeAnalyticServiceUnavailableException exception = assertThrows(
                YoutubeAnalyticServiceUnavailableException.class,
                () -> securityClient.getToken()
        );

        assertEquals(INVALID_USER_PASSWORD, exception.getMessage());
    }

    @Test
    void getTokenWhenThrownRestClientExceptionThenThrowYoutubeAnalyticServiceUnavailableExceptionTest() {
        when(restTemplate.exchange((String) any(), any(), any(), (Class<?>) any()))
                .thenThrow(new RestClientException(""));

        YoutubeAnalyticServiceUnavailableException exception = assertThrows(
                YoutubeAnalyticServiceUnavailableException.class,
                () -> securityClient.getToken()
        );

        assertEquals(YOUTUBE_ANALYTIC_SERVICE_UNAVAILABLE, exception.getMessage());
    }

    @Test
    void getTokenWhenTokenIsNullThenThrowsYoutubeAnalyticServiceUnavailableExceptionTest() {
        when(restTemplate.exchange((String) any(), any(), any(), (Class<?>) any()))
                .thenReturn(new ResponseEntity<>(null, OK));

        YoutubeAnalyticServiceUnavailableException exception = assertThrows(
                YoutubeAnalyticServiceUnavailableException.class,
                () -> securityClient.getToken()
        );

        assertEquals(IMPOSSIBLE_LOAD_TOKEN, exception.getMessage());
    }
}
