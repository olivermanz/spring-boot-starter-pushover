package de.omanz.pushover.spring.service;

import de.omanz.pushover.client.model.PushoverClientRequest;
import de.omanz.pushover.client.model.PushoverClientResponse;
import de.omanz.pushover.client.support.ClientTestDataGenerator;
import de.omanz.pushover.spring.model.*;
import de.omanz.pushover.spring.support.SpringTestDataGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PushoverClientMapperTest {

    final PushoverClientMapper sut = new PushoverClientMapper();

    @ParameterizedTest
    @MethodSource("constructPriorities")
    void map_single_user_request(PushoverPriority priorityAsEnum, int priorityAsInt) {
        SingleUserPushoverRequest springSideRequest = SpringTestDataGenerator.createSingleUser("user1", priorityAsEnum, "somePhone", "someOtherPhone");

        PushoverClientRequest clientSideRequest = sut.map(springSideRequest);

        assertMessage(clientSideRequest, springSideRequest.getMessage());
        assertThat(clientSideRequest.getUser()).isEqualTo("user1");
        assertThat(clientSideRequest.getDevice()).isEqualTo("somePhone,someOtherPhone");
        assertThat(clientSideRequest.getPriority()).isEqualTo(priorityAsInt);
    }

    @Test
    void map_multi_user_request() {
        MultiUserPushoverRequest springSideRequest = SpringTestDataGenerator.createMultiUser("Ohau", "Maui", "Lanai");

        PushoverClientRequest clientSideRequest = sut.map(springSideRequest);

        assertMessage(clientSideRequest, springSideRequest.getMessage());
        assertThat(clientSideRequest.getUser()).isEqualTo("Ohau,Maui,Lanai");
        assertThat(clientSideRequest.getDevice()).isNull();
    }

    @Test
    void map_group_request() {
        GroupPushoverRequest springSideRequest = SpringTestDataGenerator.createGroup("groupX");

        PushoverClientRequest clientSideRequest = sut.map(springSideRequest);

        assertMessage(clientSideRequest, springSideRequest.getMessage());
        assertThat(clientSideRequest.getUser()).isEqualTo("groupX");
        assertThat(clientSideRequest.getDevice()).isNull();
    }

    private void assertMessage(PushoverClientRequest clientSideRequest, PushoverMessage springSideMessage) {
        assertThat(clientSideRequest.getMessage()).isEqualTo(springSideMessage.getMessage());
        assertThat(clientSideRequest.getTitle()).isEqualTo(springSideMessage.getMessageTitle());

        assertThat(clientSideRequest.getTime()).isEqualTo(springSideMessage.getDisplayedTime().getEpochSecond());
        assertThat(clientSideRequest.getSound()).isEqualTo(springSideMessage.getSound().getId());

        assertThat(clientSideRequest.getHtml()).isZero();
        assertThat(clientSideRequest.getMonospace()).isZero();

        assertThat(clientSideRequest.getUrl()).isEqualTo(springSideMessage.getUrl().getUrl());
        assertThat(clientSideRequest.getUrlTitle()).isEqualTo(springSideMessage.getUrl().getTitle());

        assertThat(clientSideRequest.getImage().getFilename()).isEqualTo(springSideMessage.getAttachedImage().getImageFileName());
        assertThat(clientSideRequest.getImage().getMediaType()).isEqualTo(springSideMessage.getAttachedImage().getMediaType());
        assertThat(clientSideRequest.getImage().getRawContent()).isEqualTo(springSideMessage.getAttachedImage().getRawContent());
    }

    @ParameterizedTest
    @MethodSource("constructPriorities")
    void map_client_to_spring(PushoverPriority priorityAsEnum, int priorityAsInt) {
        PushoverClientResponse clientSideResponse = ClientTestDataGenerator.createResponse(0, "User 1 does not exist.", "User 6 does not exist.");
        PushoverResponse springSideResponse = sut.map(clientSideResponse, priorityAsInt);

        assertThat(springSideResponse.getStatus()).isSameAs(PushoverResponseStatus.ERROR);
        assertThat(springSideResponse.getErrors()).containsExactlyInAnyOrderElementsOf(clientSideResponse.getErrors());
        assertThat(springSideResponse.getAppLimitRemaining()).isEqualTo(clientSideResponse.getAppLimitRemaining());
        assertThat(springSideResponse.getAppLimitTotal()).isEqualTo(clientSideResponse.getAppLimitTotal());
        assertThat(springSideResponse.getAppLimitReset()).isEqualTo(clientSideResponse.getAppLimitReset());
        assertThat(springSideResponse.getRequest()).isEqualTo(clientSideResponse.getRequest());
        assertThat(springSideResponse.getPriority()).isEqualTo(priorityAsEnum);
    }

    private static Stream<Arguments> constructPriorities() {
        return Stream.of(
                Arguments.of(PushoverPriority.LOWEST, -2),
                Arguments.of(PushoverPriority.LOW, -1),
                Arguments.of(PushoverPriority.NORMAL, 0),
                Arguments.of(PushoverPriority.HIGH, 1),
                Arguments.of(PushoverPriority.EMERGENCY, 2)
        );
    }
}