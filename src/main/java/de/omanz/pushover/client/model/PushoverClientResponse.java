package de.omanz.pushover.client.model;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Builder
@Getter
public class PushoverClientResponse {

    private final int httpStatusCode;
    private final Integer appLimitTotal;
    private final Integer appLimitRemaining;
    private final Instant appLimitReset;

    private final String user;
    private final String request;
    private final int status;

    @Builder.Default
    private final List<String> errors = Collections.emptyList();

}
