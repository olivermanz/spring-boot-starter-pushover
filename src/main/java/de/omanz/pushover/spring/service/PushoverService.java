package de.omanz.pushover.spring.service;

import de.omanz.pushover.spring.model.GroupPushoverRequest;
import de.omanz.pushover.spring.model.MultiUserPushoverRequest;
import de.omanz.pushover.spring.model.PushoverResponse;
import de.omanz.pushover.spring.model.SingleUserPushoverRequest;

import javax.validation.Valid;

/**
 * Service for sending message to the Pushover API.
 */
public interface PushoverService {

    /**
     * Send a message to multiple users (max. 50).
     * @param request  the request containing the recipients and the message payload.
     * @return  PushoverResponse representing the status of the call.
     */
    PushoverResponse call(@Valid MultiUserPushoverRequest request);

    /**
     * Send a message to single user.
     * @param request  the request containing the user key and the message payload.
     * @return  PushoverResponse representing the status of the call.
     */
    PushoverResponse call(@Valid SingleUserPushoverRequest request);

    /**
     * Send a message to group. Groups need to be defined by Pushover Account GUI or Group API.
     * Insert the group key to send to all users in that group. Device selection during group call
     * is not implemented yet.
     * @param request  the request containing the group key and the message payload.
     * @return  PushoverResponse representing the status of the call.
     */
    PushoverResponse call(@Valid GroupPushoverRequest request);
}
