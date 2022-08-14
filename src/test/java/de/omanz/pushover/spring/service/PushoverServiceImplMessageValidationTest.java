package de.omanz.pushover.spring.service;

import de.omanz.pushover.client.PushoverClient;
import de.omanz.pushover.spring.PushoverClientMapper;
import de.omanz.pushover.spring.model.PushoverMessage;
import de.omanz.pushover.spring.model.PushoverSound;
import de.omanz.pushover.spring.model.PushoverURL;
import de.omanz.pushover.spring.model.SingleUserPushoverRequest;
import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import javax.validation.ConstraintViolationException;
import java.util.function.Consumer;

import static de.omanz.pushover.spring.support.SpringTestDataGenerator.createMessage;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ContextConfiguration(classes = ValidationAutoConfiguration.class)
@SpringBootTest(classes = PushoverServiceImpl.class)
class PushoverServiceImplMessageValidationTest {
    @Autowired
    PushoverServiceImpl sut;

    @MockBean
    PushoverClient client;

    @MockBean
    PushoverClientMapper mapper;

    @Test
    @DisplayName("Given a pushover message inside a request" +
            "when the message is null, empty or too long" +
            "then a validation error is raised.")
    void message_failing() {
        violationFrom(builder -> builder.message(null))
                .hasMessageContaining("message")
                .hasMessageContaining("null");

        violationFrom(builder -> builder.message(""))
                .hasMessageContaining("message");

        violationFrom(builder -> builder.message(RandomString.make(1025)))
                .hasMessageContaining("message")
                .hasMessageContaining("1024");
    }

    @Test
    @DisplayName("Given a pushover message inside a request" +
            "when the title is too long" +
            "then a validation error is raised.")
    void title_failing() {
        violationFrom(builder -> builder.messageTitle(RandomString.make(251)))
                .hasMessageContaining("messageTitle")
                .hasMessageContaining("250");
    }

    @Test
    @DisplayName("Given a pushover url inside a message inside a request" +
            "when the url is null, empty or too long" +
            "then a validation error is raised.")
    void url_url_failing() {
        violationFrom(builder -> builder.url(PushoverURL.builder().url(null).build()))
                .hasMessageContaining("url.url");

        violationFrom(builder -> builder.url(PushoverURL.builder().url("").build()))
                .hasMessageContaining("url.url");

        violationFrom(builder -> builder.url(PushoverURL.builder().url(RandomString.make(513)).build()))
                .hasMessageContaining("url.url")
                .hasMessageContaining("512");
    }

    @Test
    @DisplayName("Given a pushover url inside a message inside a request" +
            "when the title is too long" +
            "then a validation error is raised.")
    void url_title_failing() {
        final PushoverURL url = PushoverURL.builder()
                .url(RandomString.make(120))
                .title(RandomString.make(101))
                .build();

        violationFrom(builder -> builder.url(url))
                .hasMessageContaining("url.title")
                .hasMessageContaining("100");
    }

    @Test
    @DisplayName("Given a pushover sound inside a message inside a request" +
            "when the id is null or empty" +
            "then a validation error is raised.")
    void sound_title_failing() {
        violationFrom(builder -> builder.sound(PushoverSound.custom(null)))
                .hasMessageContaining("sound.id");

        violationFrom(builder -> builder.sound(PushoverSound.custom("")))
                .hasMessageContaining("sound.id");
    }

    private AbstractThrowableAssert<?, ? extends Throwable> violationFrom(Consumer<PushoverMessage.PushoverMessageBuilder> modifier) {
        final SingleUserPushoverRequest request = SingleUserPushoverRequest.builder()
                .userKey("Honolulu")
                .message(createMessage(modifier))
                .build();

        return assertThatThrownBy(() -> sut.call(request))
                .isInstanceOf(ConstraintViolationException.class);
    }
}