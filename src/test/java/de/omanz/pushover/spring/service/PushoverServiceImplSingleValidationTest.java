package de.omanz.pushover.spring.service;

import de.omanz.pushover.client.PushoverClient;
import de.omanz.pushover.spring.PushoverClientMapper;
import de.omanz.pushover.spring.model.GroupPushoverRequest;
import de.omanz.pushover.spring.model.MultiUserPushoverRequest;
import de.omanz.pushover.spring.model.PushoverMessage;
import de.omanz.pushover.spring.model.SingleUserPushoverRequest;
import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.function.Consumer;

import static de.omanz.pushover.spring.support.TestDataGenerator.createMessage;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ContextConfiguration(classes = ValidationAutoConfiguration.class)
@SpringBootTest(classes = PushoverServiceImpl.class)
class PushoverServiceImplSingleValidationTest {

    public static final Consumer<PushoverMessage.PushoverMessageBuilder> NOOP = builder -> {
    };

    @Autowired
    PushoverServiceImpl sut;

    @MockBean
    PushoverClient client;

    @MockBean
    PushoverClientMapper mapper;

    @Test
    @DisplayName("Given a single user request " +
            "when the user key is not set " +
            "then a validation error is raised.")
    void single_no_user() {
        final SingleUserPushoverRequest request = SingleUserPushoverRequest.builder()
                .devices(List.of("somePhone", "someOtherPhone"))
                .message(createMessage(NOOP))
                .build();

        assertThatThrownBy(() -> sut.call(request))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("userKey");
    }

    @Test
    @DisplayName("Given a single user request " +
            "when the message is not set " +
            "then a validation error is raised.")
    void single_no_message() {
        final SingleUserPushoverRequest request = SingleUserPushoverRequest.builder()
                .userKey("Honolulu")
                .devices(List.of("somePhone", "someOtherPhone"))
                .build();

        assertThatThrownBy(() -> sut.call(request))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("message");
    }

    private AbstractThrowableAssert<?, ? extends Throwable> assertConstraintViolation(ThrowableAssert.ThrowingCallable supplier) {
        return assertThatThrownBy(supplier)
                .isInstanceOf(ConstraintViolationException.class);
    }
}