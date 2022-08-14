package de.omanz.pushover.client;

import de.omanz.pushover.client.model.PushoverClientImage;
import de.omanz.pushover.client.model.PushoverClientRequest;
import de.omanz.pushover.client.model.PushoverClientResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;

@Slf4j
public class PushoverClient {
    private final RestTemplate restTemplate;
    private final PushoverApplicationConfig config;

    public PushoverClient(@Qualifier("pushover") RestTemplate restTemplate,
                          PushoverApplicationConfig config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    public PushoverClientResponse send(final PushoverClientRequest request) {
        final HttpEntity<MultiValueMap<String, HttpEntity<?>>> entity = prepareEntity(request);

        log.debug("Calling pushover client api.");
        final ResponseEntity<PushoverResponseJson> httpResponse = restTemplate.postForEntity(config.getPushoverPostUrl(), entity, PushoverResponseJson.class);
        final PushoverClientResponse response = constructResponse(httpResponse);
        log.debug("Pushover client call finished with status {}", response.getStatus());

        return response;
    }

    private HttpEntity<MultiValueMap<String, HttpEntity<?>>> prepareEntity(final PushoverClientRequest request) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        final MultiValueMap<String, HttpEntity<?>> multipartMap = new LinkedMultiValueMap<>();

        add(multipartMap, "token", config.getApplicationKey());
        add(multipartMap, "user", request.getUser());
        add(multipartMap, "message", request.getMessage());
        add(multipartMap, "device", request.getDevice());
        add(multipartMap, "html", request.getHtml());
        add(multipartMap, "monospace", request.getMonospace());
        add(multipartMap, "priority", request.getPriority());
        add(multipartMap, "sound", request.getSound());
        add(multipartMap, "timestamp", request.getTime());
        add(multipartMap, "title", request.getTitle());
        add(multipartMap, "url", request.getUrl());
        add(multipartMap, "url_title", request.getUrlTitle());
        add(multipartMap, "attachment", request.getImage());

        return new HttpEntity<>(multipartMap, headers);
    }

    private void add(final MultiValueMap<String, HttpEntity<?>> target, final String fieldName, @Nullable final String value) {
        if (value != null) {
            target.add(fieldName, new HttpEntity<>(value));
        }
    }

    private void add(final MultiValueMap<String, HttpEntity<?>> target, final String fieldName, @Nullable final Number value) {
        if (value != null) {
            target.add(fieldName, new HttpEntity<>(value));
        }
    }

    private void add(final MultiValueMap<String, HttpEntity<?>> target, final String fieldName, @Nullable final PushoverClientImage image) {
        if (image != null) {
            target.add(fieldName, toFileEntity(fieldName, image));
        }
    }

    private HttpEntity<byte[]> toFileEntity(final String fieldname, final PushoverClientImage image) {
        final ContentDisposition disposition = ContentDisposition.builder("form-data")
                .name(fieldname)
                .filename(image.getFilename())
                .build();

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(disposition);
        headers.setContentType(MediaType.valueOf(image.getMediaType()));

        return new HttpEntity<>(image.getRawContent(), headers);
    }

      private PushoverClientResponse constructResponse(final ResponseEntity<PushoverResponseJson> httpResponse) {
        final PushoverResponseJson json = httpResponse.getBody();
        Assert.notNull(json, "Unable to process Pushover API call since body is null.");

        return PushoverClientResponse.builder()
                .appLimitTotal(extractHeaderAsInt("X-Limit-App-Limit", httpResponse))
                .appLimitRemaining(extractHeaderAsInt("X-Limit-App-Remaining", httpResponse))
                .appLimitReset(extractHeaderAsInstant("X-Limit-App-Reset", httpResponse))
                .httpStatusCode(httpResponse.getStatusCodeValue())
                .user(json.getUser())
                .request(json.getRequest())
                .status(json.getStatus())
                .errors(json.getErrors())
                .build();
    }

    private Integer extractHeaderAsInt(final String key, final ResponseEntity<PushoverResponseJson> response) {
        return response.getHeaders().getOrEmpty(key)
                .stream()
                .findFirst()
                .map(Integer::parseInt)
                .orElse(null);
    }

    private Instant extractHeaderAsInstant(final String key, final ResponseEntity<PushoverResponseJson> response) {
        return response.getHeaders().getOrEmpty(key)
                .stream()
                .findFirst()
                .map(Long::parseLong)
                .map(Instant::ofEpochSecond)
                .orElse(null);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PushoverResponseJson {
        private String user;
        private String request;
        private int status;
        private List<String> errors;
    }

}
