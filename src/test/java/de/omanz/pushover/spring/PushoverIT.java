package de.omanz.pushover.spring;

import de.omanz.pushover.client.RecoverablePushoverClientException;
import de.omanz.pushover.spring.model.*;
import de.omanz.pushover.spring.service.PushoverService;
import lombok.SneakyThrows;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.ExpectedCount.times;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@SpringBootTest
public class PushoverIT {

    @Autowired
    PushoverService pushoverService;

    @Autowired
    TestConfig testConfig;

    @Autowired
    @Qualifier("pushover")
    RestTemplate restTemplate;

    MockRestServiceServer pushoverApiMock;

    @BeforeEach
    void init() {
        pushoverApiMock = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    void testSendSingleUser_ok() {
        pushoverApiMock.expect(times(1), requestTo("http://localhost:8080/1/messages.json"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().string(StringContains.containsString("user1001")))
                .andExpect(content().string(StringContains.containsString("Test me senseless.")))
                .andRespond(withSuccess().body("{\"status\":1,\"request\":\"" + UUID.randomUUID() + "\"}").contentType(MediaType.APPLICATION_JSON));

        final PushoverResponse response = pushoverService.call(createSingleUserTest());

        pushoverApiMock.verify();
        assertThat(response.getStatus()).isSameAs(PushoverResponseStatus.SUCCESS);

        assertThat(response.getAppLimitReset()).isNull();
        assertThat(response.getAppLimitTotal()).isNull();
        assertThat(response.getAppLimitRemaining()).isNull();
        assertThat(response.getErrors()).isEmpty();
    }

    @Test
    void testSendSingleUser_400() {
        pushoverApiMock.expect(times(1), requestTo("http://localhost:8080/1/messages.json"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withBadRequest().body("{\"errors\":[\"Error1\",\"Error2\"],\"status\":0,\"request\":\"" + UUID.randomUUID() + "\"}").contentType(MediaType.APPLICATION_JSON));

        final PushoverResponse response = pushoverService.call(createSingleUserTest());

        pushoverApiMock.verify();

        assertThat(response.getStatus()).isSameAs(PushoverResponseStatus.ERROR);
        assertThat(response.getErrors()).containsExactlyInAnyOrder("Error1", "Error2");
    }

    @Test
    void testSendSingleUser_500() {
        pushoverApiMock.expect(times(1), requestTo("http://localhost:8080/1/messages.json"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withServerError());

        assertThatThrownBy(() -> pushoverService.call(createSingleUserTest()))
                .isInstanceOf(ResourceAccessException.class)
                .hasCauseInstanceOf(RecoverablePushoverClientException.class)
                .hasMessageContaining("500");

        pushoverApiMock.verify();
    }

    private SingleUserPushoverRequest createSingleUserTest() {
        final PushoverMessage message = PushoverMessage.builder()
                .message("Test me senseless.")
                .messageTitle("Hello")
                .type(PushoverMessageType.MONOSPACE)
                .attachedImage(createPushoverImage())
                .build();

        return SingleUserPushoverRequest.builder()
                .userKey(testConfig.getUser1())
                .message(message)
                .devices(List.of("somePhone"))
                .build();
    }

    @SneakyThrows
    private PushoverImage createPushoverImage() {
        final byte[] content = getClass().getResourceAsStream("/test.png").readAllBytes();
        return PushoverImage.builder()
                .mediaType(MediaType.IMAGE_PNG_VALUE)
                .imageFileName("test.png")
                .rawContent(content)
                .build();
    }


}
