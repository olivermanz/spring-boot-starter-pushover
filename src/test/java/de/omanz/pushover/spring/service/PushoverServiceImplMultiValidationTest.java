package de.omanz.pushover.spring.service;

import de.omanz.pushover.client.PushoverClient;
import de.omanz.pushover.spring.PushoverClientMapper;
import de.omanz.pushover.spring.model.MultiUserPushoverRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static de.omanz.pushover.spring.support.TestDataGenerator.createMessage;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ContextConfiguration(classes = ValidationAutoConfiguration.class)
@SpringBootTest(classes = PushoverServiceImpl.class)
class PushoverServiceImplMultiValidationTest {

    @Autowired
    PushoverServiceImpl sut;

    @MockBean
    PushoverClient client;

    @MockBean
    PushoverClientMapper mapper;

    @Test
    @DisplayName("Given a multi user request " +
            "when the message is not set " +
            "then a validation error is raised.")
    void multi_no_message() {
        final MultiUserPushoverRequest request = MultiUserPushoverRequest.builder()
                .userKeys(List.of("Honolulu"))
                .build();

        assertThatThrownBy(() -> sut.call(request))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("message");
    }

    @Test
    @DisplayName("Given a multi user request " +
            "when the user list is empty " +
            "then a validation error is raised.")
    void multi_no_user() {
        final MultiUserPushoverRequest request = MultiUserPushoverRequest.builder()
                .message(createMessage())
                .build();

        assertThatThrownBy(() -> sut.call(request))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("userKeys");
    }

}