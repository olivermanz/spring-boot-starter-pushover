package de.omanz.pushover.spring.model;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Builder
@Getter
public class PushoverResponse {

    private final PushoverResponseStatus status;
    private final List<String> errors;
    private final Integer appLimitTotal;
    private final Integer appLimitRemaining;
    private final Instant appLimitReset;

}
