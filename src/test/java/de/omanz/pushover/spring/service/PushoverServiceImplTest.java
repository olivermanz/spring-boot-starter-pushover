package de.omanz.pushover.spring.service;

import de.omanz.pushover.client.PushoverClient;
import de.omanz.pushover.client.model.PushoverClientRequest;
import de.omanz.pushover.spring.model.GroupPushoverRequest;
import de.omanz.pushover.spring.model.MultiUserPushoverRequest;
import de.omanz.pushover.spring.model.SingleUserPushoverRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PushoverServiceImplTest {

    @Mock
    PushoverClient client;
    @Mock
    PushoverClientMapper mapper;

    @Mock
    PushoverClientRequest request;

    @InjectMocks
    PushoverServiceImpl sut;

    @Test
    @DisplayName("Given a single user request " +
            "when the service method is called " +
            "then it is mapped to a client request and the client is called.")
    void call_single_user() {
        when(mapper.map(any(SingleUserPushoverRequest.class))).thenReturn(request);
        sut.call(SingleUserPushoverRequest.builder().build());

        verify(mapper).map(any(SingleUserPushoverRequest.class));
        verify(client).send(same(request));
    }

    @Test
    @DisplayName("Given a multi user request " +
            "when the service method is called " +
            "then it is mapped to a client request and the client is called.")
    void call_multi_user() {
        when(mapper.map(any(MultiUserPushoverRequest.class))).thenReturn(request);
        sut.call(MultiUserPushoverRequest.builder().build());

        verify(mapper).map(any(MultiUserPushoverRequest.class));
        verify(client).send(same(request));
    }

    @Test
    @DisplayName("Given a group request " +
            "when the service method is called " +
            "then it is mapped to a client request and the client is called.")
    void call_group() {
        when(mapper.map(any(GroupPushoverRequest.class))).thenReturn(request);
        sut.call(GroupPushoverRequest.builder().build());

        verify(mapper).map(any(GroupPushoverRequest.class));
        verify(client).send(same(request));
    }

}