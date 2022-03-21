package com.kostenko.report.generator.service.analytic.security.impl;

import com.kostenko.report.generator.dto.security.CredentialsDto;
import com.kostenko.report.generator.dto.security.TokenDto;
import com.kostenko.report.generator.exception.YoutubeAnalyticServiceUnavailableException;
import com.kostenko.report.generator.property.analytic.YoutubeAnalyticCredentialsProperties;
import com.kostenko.report.generator.property.analytic.YoutubeAnalyticUrlsProperties;
import com.kostenko.report.generator.service.analytic.security.YoutubeAnalyticSecurityClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.springframework.web.client.HttpClientErrorException.*;
import static com.kostenko.report.generator.exception.message.ErrorMessages.*;
import static org.springframework.http.HttpMethod.POST;

@Slf4j
@Service
@AllArgsConstructor
public class DefaultYoutubeAnalyticSecurityClient implements YoutubeAnalyticSecurityClient {
    private final YoutubeAnalyticCredentialsProperties credentialsProperties;
    private final YoutubeAnalyticUrlsProperties urlsProperties;

    private final RestTemplate restTemplate;

    @Override
    public String getToken() {
        try {
            log.info("Loading token...");
            ResponseEntity<TokenDto> response = getLoginResponse();
            TokenDto token = response.getBody();
            if(token == null || token.getToken() == null) {
                throw new YoutubeAnalyticServiceUnavailableException(IMPOSSIBLE_LOAD_TOKEN);
            }
            log.info("Token was loaded.");
            return token.getToken();
        } catch (NotFound e) {
            log.error(INVALID_USERNAME, e);
            throw new YoutubeAnalyticServiceUnavailableException(INVALID_USERNAME, e);
        } catch (Forbidden e) {
            log.error(USER_DISABLED, e);
            throw new YoutubeAnalyticServiceUnavailableException(USER_DISABLED, e);
        } catch (Unauthorized e) {
            log.error(INVALID_USER_PASSWORD, e);
            throw new YoutubeAnalyticServiceUnavailableException(INVALID_USER_PASSWORD, e);
        } catch (RestClientException e) {
            log.error(YOUTUBE_ANALYTIC_SERVICE_UNAVAILABLE, e);
            throw new YoutubeAnalyticServiceUnavailableException(YOUTUBE_ANALYTIC_SERVICE_UNAVAILABLE, e);
        }
    }

    private ResponseEntity<TokenDto> getLoginResponse() {
        CredentialsDto credentialsDto = new CredentialsDto(
                credentialsProperties.getUsername(),
                credentialsProperties.getPassword()
        );
        HttpEntity<CredentialsDto> credentialsDtoHttpEntity = new HttpEntity<>(credentialsDto);
        return restTemplate.exchange(
                urlsProperties.getLoginURL(), POST, credentialsDtoHttpEntity, TokenDto.class
        );
    }
}
