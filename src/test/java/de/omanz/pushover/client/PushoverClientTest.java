package de.omanz.pushover.client;

import de.omanz.pushover.client.model.PushoverClientRequest;
import de.omanz.pushover.client.model.PushoverClientResponse;
import de.omanz.pushover.client.support.TestDataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PushoverClientTest {

    private static final String PUSHOVER_API = "http://localhost:123/pushover";
    private static final String SOME_UUID = UUID.randomUUID().toString();
    private static final Instant SOME_TIMESTAMP = Instant.ofEpochSecond(1_000_000);

    @Mock
    RestTemplate restTemplate;

    @Mock
    PushoverApplicationConfig config;

    @InjectMocks
    PushoverClient sut;

    @BeforeEach
    void setUp() {
        when(config.getApplicationKey()).thenReturn("DONOTTELL");
        when(config.getPushoverPostUrl()).thenReturn(PUSHOVER_API);
    }

    @Test
    @DisplayName("Given a valid request " +
            "when the server returns success " +
            "then a corresponding response is constructed.")
    void send_request_ok() {
        final PushoverClientRequest request = TestDataGenerator.createMessage();
        final PushoverClient.PushoverResponseJson json = createReturnValue("singleUser", 1);
        provisionServerReturn(json, HttpStatus.OK);

        final PushoverClientResponse response = sut.send(request);

        verifyResponseReturned(response, HttpStatus.OK.value(), 1);
        final Map<String, String> httpRequest = verifyRestCall();
        verifyValuesSent(httpRequest, request);
    }

    @Test
    @DisplayName("Given an invalid request " +
            "when the server returns error " +
            "then a corresponding response is constructed.")
    void send_request_fail() {
        final PushoverClientRequest request = TestDataGenerator.createMessage();
        final PushoverClient.PushoverResponseJson json = createReturnValue("singleUser", 0);
        provisionServerReturn(json, HttpStatus.BAD_REQUEST);

        final PushoverClientResponse response = sut.send(request);

        verifyResponseReturned(response, HttpStatus.BAD_REQUEST.value(), 0);
        final Map<String, String> httpRequest = verifyRestCall();
        verifyValuesSent(httpRequest, request);
    }

    private PushoverClient.PushoverResponseJson createReturnValue(String user, int statusCode, String... errors) {
        return new PushoverClient.PushoverResponseJson(user, SOME_UUID, statusCode, List.of(errors));
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> verifyRestCall() {
        final var captor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate).postForEntity(eq(PUSHOVER_API), captor.capture(), any());

        final MultiValueMap<String, HttpEntity<?>> body = (MultiValueMap<String, HttpEntity<?>>) captor.getValue().getBody();

        assertThat(body).isNotNull();
        final Map<String, String> output = new HashMap<>();

        body.forEach((key, value) -> {
            String valueAsString = value.stream()
                    .map(entity -> String.valueOf(entity.getBody()))
                    .collect(Collectors.joining());
            output.put(key, valueAsString);
        });
        return output;
    }

    private void provisionServerReturn(PushoverClient.PushoverResponseJson response, HttpStatus status) {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("X-Limit-App-Limit", "10000");
        headers.set("X-Limit-App-Remaining", "100");
        headers.set("X-Limit-App-Reset", "" + SOME_TIMESTAMP.getEpochSecond());

        ResponseEntity<PushoverClient.PushoverResponseJson> entity = new ResponseEntity<>(response, headers, status);

        when(restTemplate.postForEntity(eq(PUSHOVER_API), any(), eq(PushoverClient.PushoverResponseJson.class))).thenReturn(entity);
    }

    private void verifyResponseReturned(PushoverClientResponse response, int httpStatus, int pushoverStatus) {
        assertThat(response.getStatus()).isEqualTo(pushoverStatus);
        assertThat(response.getHttpStatusCode()).isEqualTo(httpStatus);
        assertThat(response.getUser()).isEqualTo("singleUser");
        assertThat(response.getRequest()).isEqualTo(SOME_UUID);
        assertThat(response.getAppLimitTotal()).isEqualTo(10000);
        assertThat(response.getAppLimitRemaining()).isEqualTo(100);
        assertThat(response.getAppLimitReset()).isEqualTo(SOME_TIMESTAMP);
    }

    private void verifyValuesSent(Map<String, String> httpRequest, PushoverClientRequest request) {
        assertThat(httpRequest.get("token")).isEqualTo("DONOTTELL");
        assertThat(httpRequest.get("user")).isEqualTo(request.getUser());
        assertThat(httpRequest.get("message")).isEqualTo(request.getMessage());
        assertThat(httpRequest.get("title")).isEqualTo(request.getTitle());
        assertThat(httpRequest.get("timestamp")).isEqualTo(String.valueOf(request.getTime()));
        assertThat(httpRequest.get("url")).isEqualTo(request.getUrl());
        assertThat(httpRequest.get("url_title")).isEqualTo(request.getUrlTitle());
        assertThat(httpRequest.get("sound")).isEqualTo(request.getSound());
        assertThat(httpRequest.get("html")).isEqualTo(String.valueOf(request.getHtml()));
        assertThat(httpRequest.get("monospace")).isEqualTo(String.valueOf(request.getMonospace()));
        assertThat(httpRequest.get("priority")).isEqualTo(String.valueOf(request.getPriority()));
        assertThat(httpRequest.get("attachment")).isNotNull();
        assertThat(httpRequest.get("group")).isNull();
    }


}