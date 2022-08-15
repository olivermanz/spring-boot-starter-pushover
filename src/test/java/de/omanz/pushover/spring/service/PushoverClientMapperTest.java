package de.omanz.pushover.spring.service;

import de.omanz.pushover.client.model.PushoverClientRequest;
import de.omanz.pushover.client.model.PushoverClientResponse;
import de.omanz.pushover.client.support.ClientTestDataGenerator;
import de.omanz.pushover.spring.model.*;
import de.omanz.pushover.spring.service.PushoverClientMapper;
import de.omanz.pushover.spring.support.SpringTestDataGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PushoverClientMapperTest {

    final PushoverClientMapper sut = new PushoverClientMapper();

    @Test
    void map_single_user_request() {
        SingleUserPushoverRequest springSideRequest = SpringTestDataGenerator.createSingleUser("user1", "somePhone", "someOtherPhone");

        PushoverClientRequest clientSideRequest = sut.map(springSideRequest);

        assertMessage(clientSideRequest, springSideRequest.getMessage());
        assertThat(clientSideRequest.getUser()).isEqualTo("user1");
        assertThat(clientSideRequest.getDevice()).isEqualTo("somePhone,someOtherPhone");
        assertThat(clientSideRequest.getGroup()).isNull();
    }

    @Test
    void map_multi_user_request() {
        MultiUserPushoverRequest springSideRequest = SpringTestDataGenerator.createMultiUser("Ohau", "Maui", "Lanai");

        PushoverClientRequest clientSideRequest = sut.map(springSideRequest);

        assertMessage(clientSideRequest, springSideRequest.getMessage());
        assertThat(clientSideRequest.getUser()).isEqualTo("Ohau,Maui,Lanai");
        assertThat(clientSideRequest.getDevice()).isNull();
        assertThat(clientSideRequest.getGroup()).isNull();
    }

    @Test
    void map_group_reuqest() {
        GroupPushoverRequest springSideRequest = SpringTestDataGenerator.createGroup("groupX");

        PushoverClientRequest clientSideRequest = sut.map(springSideRequest);

        assertMessage(clientSideRequest, springSideRequest.getMessage());
        assertThat(clientSideRequest.getUser()).isNull();
        assertThat(clientSideRequest.getDevice()).isNull();
        assertThat(clientSideRequest.getGroup()).isEqualTo("groupX");
    }

    private void assertMessage(PushoverClientRequest clientSideRequest, PushoverMessage springSideMessage) {
        assertThat(clientSideRequest.getMessage()).isEqualTo(springSideMessage.getMessage());
        assertThat(clientSideRequest.getTitle()).isEqualTo(springSideMessage.getMessageTitle());

        assertThat(clientSideRequest.getTime()).isEqualTo(springSideMessage.getDisplayedTime().getEpochSecond());
        assertThat(clientSideRequest.getSound()).isEqualTo(springSideMessage.getSound().getId());

        assertThat(clientSideRequest.getPriority()).isZero();
        assertThat(clientSideRequest.getHtml()).isZero();
        assertThat(clientSideRequest.getMonospace()).isZero();

        assertThat(clientSideRequest.getUrl()).isEqualTo(springSideMessage.getUrl().getUrl());
        assertThat(clientSideRequest.getUrlTitle()).isEqualTo(springSideMessage.getUrl().getTitle());

        assertThat(clientSideRequest.getImage().getFilename()).isEqualTo(springSideMessage.getAttachedImage().getImageFileName());
        assertThat(clientSideRequest.getImage().getMediaType()).isEqualTo(springSideMessage.getAttachedImage().getMediaType());
        assertThat(clientSideRequest.getImage().getRawContent()).isEqualTo(springSideMessage.getAttachedImage().getRawContent());
    }

    @Test
    void map_client_to_spring() {
        PushoverClientResponse clientSideResponse = ClientTestDataGenerator.createResponse(0, "User 1 does not exist.", "User 6 does not exist.");
        PushoverResponse springSideResponse = sut.map(clientSideResponse);

        assertThat(springSideResponse.getStatus()).isSameAs(PushoverResponseStatus.ERROR);
        assertThat(springSideResponse.getErrors()).containsExactlyInAnyOrderElementsOf(clientSideResponse.getErrors());
        assertThat(springSideResponse.getAppLimitRemaining()).isEqualTo(clientSideResponse.getAppLimitRemaining());
        assertThat(springSideResponse.getAppLimitTotal()).isEqualTo(clientSideResponse.getAppLimitTotal());
        assertThat(springSideResponse.getAppLimitReset()).isEqualTo(clientSideResponse.getAppLimitReset());
    }
}