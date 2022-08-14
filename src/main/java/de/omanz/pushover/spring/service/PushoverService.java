package de.omanz.pushover.spring.service;

import de.omanz.pushover.spring.model.GroupPushoverRequest;
import de.omanz.pushover.spring.model.MultiUserPushoverRequest;
import de.omanz.pushover.spring.model.PushoverResponse;
import de.omanz.pushover.spring.model.SingleUserPushoverRequest;

import javax.validation.Valid;

public interface PushoverService {

    PushoverResponse call(@Valid MultiUserPushoverRequest request);

    PushoverResponse call(@Valid SingleUserPushoverRequest request);

    PushoverResponse call(@Valid GroupPushoverRequest request);
}
