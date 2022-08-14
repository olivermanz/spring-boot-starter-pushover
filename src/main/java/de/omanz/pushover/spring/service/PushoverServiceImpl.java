package de.omanz.pushover.spring.service;

import de.omanz.pushover.client.PushoverClient;
import de.omanz.pushover.client.model.PushoverClientRequest;
import de.omanz.pushover.client.model.PushoverClientResponse;
import de.omanz.pushover.spring.PushoverClientMapper;
import de.omanz.pushover.spring.model.GroupPushoverRequest;
import de.omanz.pushover.spring.model.MultiUserPushoverRequest;
import de.omanz.pushover.spring.model.PushoverResponse;
import de.omanz.pushover.spring.model.SingleUserPushoverRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@RequiredArgsConstructor
@Validated
public class PushoverServiceImpl implements PushoverService {

    private final PushoverClient client;
    private final PushoverClientMapper mapper;

    @Override
    public PushoverResponse call(@Valid SingleUserPushoverRequest singleRequest) {
        return send(mapper.map(singleRequest));
    }

    @Override
    public PushoverResponse call(@Valid MultiUserPushoverRequest multiRequest) {
        return send(mapper.map(multiRequest));
    }

    @Override
    public PushoverResponse call(@Valid GroupPushoverRequest groupRequest) {
        return send(mapper.map(groupRequest));
    }

    public PushoverResponse send(PushoverClientRequest request) {
        PushoverClientResponse response = client.send(request);
        return mapper.map(response);
    }
}
