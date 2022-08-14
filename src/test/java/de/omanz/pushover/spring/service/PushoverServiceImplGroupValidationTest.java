package de.omanz.pushover.spring.service;

import de.omanz.pushover.client.PushoverClient;
import de.omanz.pushover.spring.PushoverClientMapper;
import de.omanz.pushover.spring.model.GroupPushoverRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import javax.validation.ConstraintViolationException;

import static de.omanz.pushover.spring.support.TestDataGenerator.createMessage;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ContextConfiguration(classes = ValidationAutoConfiguration.class)
@SpringBootTest(classes = PushoverServiceImpl.class)
class PushoverServiceImplGroupValidationTest {

    @Autowired
    PushoverServiceImpl sut;

    @MockBean
    PushoverClient client;

    @MockBean
    PushoverClientMapper mapper;

    @Test
    @DisplayName("Given a group request " +
            "when the message is not set " +
            "then a validation error is raised.")
    void group_no_message() {
        final GroupPushoverRequest request = GroupPushoverRequest.builder()
                .groupKey("Oahu")
                .build();

        assertThatThrownBy(() -> sut.call(request))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("message");
    }

    @Test
    @DisplayName("Given a group request " +
            "when the group key is not set " +
            "then a validation error is raised.")
    void group_no_group() {
        final GroupPushoverRequest request = GroupPushoverRequest.builder()
                .message(createMessage())
                .build();

        assertThatThrownBy(() -> sut.call(request))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("groupKey");
    }

}